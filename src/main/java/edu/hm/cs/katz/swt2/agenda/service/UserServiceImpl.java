package edu.hm.cs.katz.swt2.agenda.service;

import static edu.hm.cs.katz.swt2.agenda.common.SecurityHelper.ADMIN_ROLES;
import static edu.hm.cs.katz.swt2.agenda.common.SecurityHelper.STANDARD_ROLES;
import edu.hm.cs.katz.swt2.agenda.persistence.User;
import edu.hm.cs.katz.swt2.agenda.persistence.UserRepository;
import edu.hm.cs.katz.swt2.agenda.service.dto.UserDisplayDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import javax.validation.ValidationException;
import org.modelmapper.ModelMapper;
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
  private ModelMapper mapper;

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
    for (User anwender : anwenderRepository.findAll()) {
      result.add(mapper.map(anwender, UserDisplayDto.class));
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
      result.add(mapper.map(anwender, UserDisplayDto.class));
    }
    return result;
  }

  @Override
  @PreAuthorize("#login == authentication.name or hasRole('ROLE_ADMIN')")
  public UserDisplayDto getUserInfo(String login) {
    LOG.info("Lese Daten eines Anwenders.");
    LOG.debug("Lese Daten des Anwenders \"{}\".", login);
    User anwender = anwenderRepository.getOne(login);
    return new UserDisplayDto(anwender.getLogin());
  }

  @Override
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public void legeAn(String login, String password, boolean isAdministrator) {

    LOG.info("Erstelle einen Anwender.");
    LOG.debug("Erstelle Anwender \"{}\" mit Passwort ***, isAdmin: {}.", login, isAdministrator);

    if (anwenderRepository.existsById(login)) {
      throw new ValidationException(
          "Name des Anwenders ist bereits vergeben. Bitte passen Sie Ihre Eingabe an!");
    }

    if (!login.equals(login.toLowerCase())) {
      throw new ValidationException(
          "Name des Anwenders darf nur aus kleingeschriebenen Buchstaben bestehen. "
              + "Bitte passen Sie Ihre Eingabe an!");
    }

    if (login.length() < 4 || login.length() > 20) {
      throw new ValidationException(
          "Der Name muss zwischen 4 und 20 Zeichen lang sein. Bitte passen Sie Ihre Eingabe an!");
    }

    if (password.length() < 8 || password.length() > 20) {
      throw new ValidationException("Das Passwort muss zwischen 8 und 20 Zeichen lang sein. "
          + "Bitte passen Sie Ihre Eingabe an!");
    }

    String leerzeichen = " ";

    if (password.contains(leerzeichen)) {
      throw new ValidationException(
          "Das Passwort darf keine Leerzeichen beinhlaten. Bitte passen Sie Ihre Eingabe an!");
    }

    Pattern pruefenAufGrossbuchstaben = Pattern.compile("[A-Z]");
    Pattern pruefenAufKleinbuchstaben = Pattern.compile("[a-z]");
    Pattern pruefenAufZahl = Pattern.compile("[0-9]");


    if (!pruefenAufGrossbuchstaben.matcher(password).find()) {
      throw new ValidationException(
          "Das Passwort muss mindestens einen Großbuchstabenbuchstaben beinhalten. "
              + "Bitte passen Sie Ihre Eingabe an!");
    }

    if (!pruefenAufKleinbuchstaben.matcher(password).find()) {
      throw new ValidationException(
          "Das Passwort muss mindestens einen Kleinbuchstabenbuchstaben beinhalten. "
              + "Bitte passen Sie Ihre Eingabe an!");
    }

    if (!pruefenAufZahl.matcher(password).find()) {
      throw new ValidationException(
          "Das Passwort muss mindestens eine Zahl beinhalten. Bitte passen Sie Ihre Eingabe an!");

    }

    Pattern pruefenAufSonderzeichen = Pattern.compile("[*?!$%&]");

    if (!pruefenAufSonderzeichen.matcher(password).find()) {
      throw new ValidationException(
          "Das Passwort muss mindestens einer dieser Sonderzeichen beinhalten: *?!$%&. "
              + "Bitte passen Sie Ihre Eingabe an!");
    }

    // Passwörter müssen Hashverfahren benennen.
    // Wir hashen nicht (noop), d.h. wir haben die
    // Passwörter im Klartext in der Datenbank (böse)
    User anwender = new User(login, "{noop}" + password, isAdministrator);
    anwenderRepository.save(anwender);
  }
}
