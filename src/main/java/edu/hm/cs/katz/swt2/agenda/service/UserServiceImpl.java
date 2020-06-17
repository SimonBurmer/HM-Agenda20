package edu.hm.cs.katz.swt2.agenda.service;

import static edu.hm.cs.katz.swt2.agenda.common.SecurityHelper.ADMIN_ROLES;
import static edu.hm.cs.katz.swt2.agenda.common.SecurityHelper.STANDARD_ROLES;
import edu.hm.cs.katz.swt2.agenda.persistence.User;
import edu.hm.cs.katz.swt2.agenda.persistence.UserRepository;
import edu.hm.cs.katz.swt2.agenda.service.dto.UserDisplayDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
  private UserRepository anwenderRepository;

  @Autowired
  private DtoMapper mapper;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    LOG.info("Fordere Details für einen Anwender an.");
    LOG.debug("Fordere Details für Anwender \"{}\" an.", username);

    Optional<User> findeMitspieler = anwenderRepository.findById(username);
    if (findeMitspieler.isPresent()) {
      User user = findeMitspieler.get();
      return new org.springframework.security.core.userdetails.User(user.getLogin(),
          user.getPassword(), user.isAdministrator() ? ADMIN_ROLES : STANDARD_ROLES);
    } else {
      LOG.debug("Anwender \"{}\" konnte nicht gefunden werden.", username);
      throw new UsernameNotFoundException("Anwender konnte nicht gefunden werden.");
    }
  }

  @Override
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public List<UserDisplayDto> getAllUsers() {
    LOG.info("Erstelle eine Liste aller Anwender.");
    List<UserDisplayDto> result = new ArrayList<>();
    for (User anwender : anwenderRepository.findAllByOrderByLoginAsc()) {
      result.add(mapper.createDto(anwender));
    }
    return result;
  }

  @Override
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public List<UserDisplayDto> findeAdmins() {
    LOG.info("Erstelle eine Liste aller Admins.");
    // Das Mapping auf DTOs geht eleganter, ist dann aber schwer verständlich.
    List<UserDisplayDto> result = new ArrayList<>();
    for (User anwender : anwenderRepository.findByAdministrator(true)) {
      result.add(mapper.createDto(anwender));
    }
    return result;
  }

  @Override
  @PreAuthorize("#login == authentication.name or hasRole('ROLE_ADMIN')")
  public UserDisplayDto getUserInfo(String login) {
    LOG.info("Lese Daten eines Anwenders.");
    LOG.debug("Lese Daten des Anwenders \"{}\".", login);
    User anwender = anwenderRepository.getOne(login);
    return mapper.createDto(anwender);
  }

  @Override
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public void legeAn(String login, String name, String password, boolean isAdministrator) {
    
    LOG.info("Erstelle einen Anwender.");
    LOG.debug("Erstelle Anwender \"{}\" mit Passwort ***, isAdmin: {}.", login, isAdministrator);
    
    if (anwenderRepository.existsById(login)) {
      LOG.debug("Name des Anwenders ist bereits vergeben. Bitte passen Sie Ihre Eingabe an!");
      throw new ValidationException(
          "Name des Anwenders ist bereits vergeben. Bitte passen Sie Ihre Eingabe an!");
    }
    
    ValidationService.userValidation(login, name, password);

    // Passwörter müssen Hashverfahren benennen.
    // Wir hashen nicht (noop), d.h. wir haben die
    // Passwörter im Klartext in der Datenbank (böse)
    User anwender = new User(login, name, "{noop}" + password, isAdministrator);
    anwenderRepository.save(anwender);
  }
}
