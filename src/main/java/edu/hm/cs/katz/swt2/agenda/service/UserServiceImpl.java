package edu.hm.cs.katz.swt2.agenda.service;

import static edu.hm.cs.katz.swt2.agenda.common.SecurityHelper.ADMIN_ROLES;
import static edu.hm.cs.katz.swt2.agenda.common.SecurityHelper.STANDARD_ROLES;

import edu.hm.cs.katz.swt2.agenda.persistence.StatusRepository;
import edu.hm.cs.katz.swt2.agenda.persistence.Task;
import edu.hm.cs.katz.swt2.agenda.persistence.TaskRepository;
import edu.hm.cs.katz.swt2.agenda.persistence.Topic;
import edu.hm.cs.katz.swt2.agenda.persistence.TopicRepository;
import edu.hm.cs.katz.swt2.agenda.persistence.User;
import edu.hm.cs.katz.swt2.agenda.persistence.UserRepository;
import edu.hm.cs.katz.swt2.agenda.service.dto.UserDisplayDto;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
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
  StatusRepository statusRepository;

  @Autowired
  TopicRepository topicRepository;

  @Autowired
  TaskRepository taskRepository;

  @Autowired
  private DtoMapper mapper;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    LOG.info("Fordere Details für einen Anwender an.");
    LOG.debug("\tusername=\"{}\"", username);

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
    LOG.debug("\tlogin=\"{}\"", login);
    User anwender = anwenderRepository.getOne(login);
    return mapper.createDto(anwender);
  }

  @Override
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public void legeAn(String login, String name, String password, boolean isAdministrator) {

    LOG.info("Erstelle einen Anwender.");
    LOG.debug("\tlogin=\"{}\" name=\"{}\" password=\"{}\" isAdministrator={}", login, name,
        password, isAdministrator);

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

  @Override
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public void deleteUser(String login) {
    LOG.info("Lösche einen Anwender.");
    LOG.debug("\tlogin=\"{}\"", login);

    if (!anwenderRepository.existsById(login)) {
      LOG.debug("Zu löschender Anwender existiert nicht!");
      throw new ValidationException(
          "Zu löschender Anwender existiert nicht!");
    }
    if (login.equals("admin")) {
      LOG.debug("Admin Account kann nicht gelöscht werden!");
      throw new ValidationException(
          "Admin Account kann nicht gelöscht werden!");
    }

    User userToDelete = anwenderRepository.getOne(login);

    // Abonnierte Topics und Task-Statuses löschen
    Collection<Topic> subscribedTopics = userToDelete.getSubscriptions();
    LOG.debug("\tsubscribedTopics={}", subscribedTopics);
    for (Iterator<Topic> iterator = subscribedTopics.iterator(); iterator.hasNext();) {
      Topic subscribedTopic = iterator.next();
      LOG.debug("\t\tsubscribedTopic={}", subscribedTopic);
      subscribedTopic.getSubscriberModifiable().remove(userToDelete);
      Collection<Task> subscribedTasks = subscribedTopic.getTasks();
      LOG.debug("\t\tsubscribedTasks={}", subscribedTasks);
      int numberOfDeletedStatuses = 0;
      for (Task task : subscribedTasks) {
        numberOfDeletedStatuses += statusRepository.deleteByUserAndTask(userToDelete, task);
      }
      LOG.debug("\t\t{} eigene Task Status gelöscht", numberOfDeletedStatuses);
      iterator.remove();
    }

    // Eigene Topics, Abonnements, Abonnenten-Status und Tasks löschen
    List<Topic> managedTopics = topicRepository.findByCreatorOrderByTitleAsc(userToDelete);
    LOG.debug("\tmanagedTopics={}", managedTopics);
    for (Iterator<Topic> managedTopicIterator = managedTopics.iterator();
         managedTopicIterator.hasNext();) {
      Topic managedTopic = managedTopicIterator.next();
      LOG.debug("\t\tmanagedTopic={}", managedTopic);
      Collection<User> subscribers = managedTopic.getSubscriberModifiable();
      for (Iterator<User> subscriberIterator = subscribers.iterator();
           subscriberIterator.hasNext();) {
        User subscribedUser = subscriberIterator.next();
        LOG.debug("\t\tsubscribedUser={}", subscribedUser);
        subscribedUser.getSubscriptions().remove(managedTopic);
        Collection<Task> subscriberTasks = managedTopic.getTasks();
        LOG.debug("\t\tsubscriberTasks={}", subscriberTasks);
        int numberOfDeletedStatuses = 0;
        for (Task task : subscriberTasks) {
          numberOfDeletedStatuses += statusRepository.deleteByUserAndTask(subscribedUser, task);
        }
        LOG.debug("\t\t{} Subscriber Task Status gelöscht", numberOfDeletedStatuses);
        subscriberIterator.remove();
      }
      taskRepository.deleteByTopic(managedTopic);
      LOG.debug("\tAlle managed Tasks gelöscht");
      topicRepository.delete(managedTopic);
      LOG.debug("\tAlle managed Topics gelöscht");
      managedTopicIterator.remove();
    }
    // User löschen
    anwenderRepository.delete(userToDelete);
    LOG.debug("\tAnwender gelöscht");
  }
}
