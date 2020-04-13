package edu.hm.cs.katz.swt2.agenda.service;

import static edu.hm.cs.katz.swt2.agenda.SecurityHelper.ADMIN_ROLES;
import static edu.hm.cs.katz.swt2.agenda.SecurityHelper.STANDARD_ROLES;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import edu.hm.cs.katz.swt2.agenda.mvc.AnwenderDto;
import edu.hm.cs.katz.swt2.agenda.persistence.anwender.Anwender;
import edu.hm.cs.katz.swt2.agenda.persistence.anwender.AnwenderRepository;

@Component
@Transactional
public class AnwenderService implements UserDetailsService {

  private static final Logger LOG = LoggerFactory.getLogger(AnwenderService.class);

  @Autowired
  private AnwenderRepository anwenderRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<Anwender> findeMitspieler = anwenderRepository.findById(username);
    if (findeMitspieler.isPresent()) {
      Anwender user = findeMitspieler.get();
      return new User(user.getLogin(), user.getPassword(),
          user.isAdministrator() ? ADMIN_ROLES : STANDARD_ROLES);
    } else {
      throw new UsernameNotFoundException("");
    }
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public List<Anwender> findeAlleAnwender() {
    return anwenderRepository.findAll();
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public List<Anwender> findeAdmins() {
    return anwenderRepository.findByAdministrator(true);
  }

  @PreAuthorize("#login == authentication.name or hasRole('ROLE_ADMIN')")
  public Anwender findeAnwender(String login) {
    return anwenderRepository.getOne(login);
  }

  @PreAuthorize("#login == authentication.name or hasRole('ROLE_ADMIN')")
  public AnwenderDto getAnwenderInfo(String login) {
    Anwender anwender = anwenderRepository.getOne(login);
    return new AnwenderDto(anwender.getLogin());
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public void legeAn(String login, String password, boolean b) {
    // Passwörter müssen Hashverfahren benennen.
    // Wir hashen nicht (noop), d.h. wir haben die
    // Passwörter im Klartext in der Datenbank (böse)
    Anwender anwender = new Anwender(login, "{noop}" + password, b);
    anwenderRepository.save(anwender);
  }
}
