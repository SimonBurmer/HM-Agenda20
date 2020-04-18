package edu.hm.cs.katz.swt2.agenda.service.user;

import static edu.hm.cs.katz.swt2.agenda.SecurityHelper.ADMIN_ROLES;
import static edu.hm.cs.katz.swt2.agenda.SecurityHelper.STANDARD_ROLES;

import edu.hm.cs.katz.swt2.agenda.mvc.anwender.AnwenderDisplayDto;
import edu.hm.cs.katz.swt2.agenda.persistence.anwender.Anwender;
import edu.hm.cs.katz.swt2.agenda.persistence.anwender.AnwenderRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service-Klasse zur Verwaltung von Anwendern. Wird auch genutzt, um Logins zu validieren.
 * Servicemethoden sind transaktional und rollen alle Änderungen zurück, wenn eine Exception
 * auftritt. Service-Methoden sollten
 * <ul>
 * <li>keine Modell-Objekte herausreichen, um Veränderungen des Modells außerhalb des
 * transaktionalen Kontextes zu verhindern - Schnittstellenobjekte sind die DTOs (Data Transfer
 * Objects).
 * <li>die Berechtigungen überprüfen, d.h. sich nicht darauf verlassen, dass die Zugriffen über die
 * Webcontroller zulässig sind.</li>
 * </ul>
 * 
 * @author Bastian Katz (mailto: bastian.katz@hm.edu)
 */
@Component
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserDetailsService, UserService {

  private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

  @Autowired
  private AnwenderRepository anwenderRepository;

  @Autowired
  private ModelMapper mapper;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<Anwender> findeMitspieler = anwenderRepository.findById(username);
    if (findeMitspieler.isPresent()) {
      Anwender user = findeMitspieler.get();
      return new User(user.getLogin(), user.getPassword(),
          user.isAdministrator() ? ADMIN_ROLES : STANDARD_ROLES);
    } else {
      throw new UsernameNotFoundException("Anwender konnte nicht gefunden werden.");
    }
  }

  @Override
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public List<AnwenderDisplayDto> findeAlleAnwender() {
    List<AnwenderDisplayDto> result = new ArrayList<>();
    for (Anwender anwender : anwenderRepository.findAll()) {
      result.add(mapper.map(anwender, AnwenderDisplayDto.class));
    }
    return result;
  }

  @Override
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public List<AnwenderDisplayDto> findeAdmins() {

    // Das Mapping auf DTOs geht eleganter, ist dann aber schwer verständlich.
    List<AnwenderDisplayDto> result = new ArrayList<>();
    for (Anwender anwender : anwenderRepository.findByAdministrator(true)) {
      result.add(mapper.map(anwender, AnwenderDisplayDto.class));
    }
    return result;
  }


  @Override
  @PreAuthorize("#login == authentication.name or hasRole('ROLE_ADMIN')")
  public AnwenderDisplayDto getAnwenderInfo(String login) {
    LOG.debug("Lese Daten für Anwender {}.", login);
    Anwender anwender = anwenderRepository.getOne(login);
    return new AnwenderDisplayDto(anwender.getLogin());
  }

  @Override
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public void legeAn(String login, String password, boolean isAdministrator) {
    LOG.debug("Erstelle Anwender {}.", login);
    // Passwörter müssen Hashverfahren benennen.
    // Wir hashen nicht (noop), d.h. wir haben die
    // Passwörter im Klartext in der Datenbank (böse)
    Anwender anwender = new Anwender(login, "{noop}" + password, isAdministrator);
    anwenderRepository.save(anwender);
  }
}
