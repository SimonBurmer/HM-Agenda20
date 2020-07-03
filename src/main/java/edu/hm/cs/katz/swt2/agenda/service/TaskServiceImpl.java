package edu.hm.cs.katz.swt2.agenda.service;

import edu.hm.cs.katz.swt2.agenda.common.ImageException;
import edu.hm.cs.katz.swt2.agenda.common.ImageUtilities;
import edu.hm.cs.katz.swt2.agenda.common.StatusEnum;
import edu.hm.cs.katz.swt2.agenda.common.TaskTypeEnum;
import edu.hm.cs.katz.swt2.agenda.mvc.Search;
import edu.hm.cs.katz.swt2.agenda.persistence.Status;
import edu.hm.cs.katz.swt2.agenda.persistence.StatusRepository;
import edu.hm.cs.katz.swt2.agenda.persistence.Task;
import edu.hm.cs.katz.swt2.agenda.persistence.TaskRepository;
import edu.hm.cs.katz.swt2.agenda.persistence.Topic;
import edu.hm.cs.katz.swt2.agenda.persistence.TopicRepository;
import edu.hm.cs.katz.swt2.agenda.persistence.User;
import edu.hm.cs.katz.swt2.agenda.persistence.UserRepository;
import edu.hm.cs.katz.swt2.agenda.service.dto.OwnerTaskDto;
import edu.hm.cs.katz.swt2.agenda.service.dto.StatusDto;
import edu.hm.cs.katz.swt2.agenda.service.dto.SubscriberTaskDto;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import javax.validation.ValidationException;
import org.apache.commons.collections4.SetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Component
@Transactional(rollbackFor = Exception.class)
public class TaskServiceImpl implements TaskService {

  private static final Logger LOG = LoggerFactory.getLogger(TaskServiceImpl.class);

  @Autowired
  private TaskRepository taskRepository;

  @Autowired
  private TopicRepository topicRepository;

  @Autowired
  private UserRepository anwenderRepository;

  @Autowired
  private StatusRepository statusRepository;

  @Autowired
  private DtoMapper mapper;

  @Override
  @PreAuthorize("#login == authentication.name or hasRole('ROLE_ADMIN')")
  public void deleteTask(Long id, String login) {
    LOG.info("Lösche Task eines Anwenders.");
    LOG.debug("\tid={} login=\"{}\"", id, login);
    Task task = taskRepository.getOne(id);
    taskRepository.delete(task);
  }

  @Override
  @PreAuthorize("#login == authentication.name or hasRole('ROLE_ADMIN')")
  public Long createTask(String uuid, String title, String login, String taskShortDescription,
      String taskLongDescription, TaskTypeEnum taskType, MultipartFile imageFile) {
    LOG.info("Erstelle einen Task.");
    LOG.debug("\ttitle=\"{}\" uuid=\"{}\" login=\"{}\" taskType={}", title, uuid, login, taskType);

    ValidationService.topicValidation(title, taskShortDescription, taskLongDescription);

    User user = anwenderRepository.getOne(login);
    Optional<Topic> optTopic = topicRepository.findById(uuid);

    Topic t = optTopic.orElseThrow(() -> {
      LOG.debug("Topic mit der Id {} ist nicht verfügbar!", login);
      return new NoSuchElementException("Topic mit gegebener Id ist nicht verfügbar!");
    });

    if (!user.equals(t.getCreator())) {
      LOG.warn("Anwender {} ist nicht berechtigt, einen Task in dem Topic {} zu erstellen.", login,
          t.getTitle());
      throw new AccessDeniedException("Kein Zugriff auf das Topic!");
    }

    if (imageFile == null || imageFile.isEmpty()) {
      byte[] fileContent = ImageUtilities.imageLoader("defaultImage.jpg");
      Task task =
          new Task(t, title, taskShortDescription, taskLongDescription, taskType, fileContent);
      taskRepository.save(task);
      return task.getId();
    } else {
      ValidationService.imageValidation(imageFile);
      try {
        Task task = new Task(t, title, taskShortDescription, taskLongDescription, taskType,
            imageFile.getBytes());
        taskRepository.save(task);
        return task.getId();
      } catch (IOException e) {
        LOG.error("Bild konnte nicht in ein byte[] umgewandelt werden!");
        throw new ImageException("Bild konnte nicht gespeichert werden!");
      }
    }
  }

  @Override
  @PreAuthorize("#login == authentication.name or hasRole('ROLE_ADMIN')")
  public Long createTask(String uuid, String title, String login, String taskShortDescription,
      String taskLongDescription, TaskTypeEnum taskType, String fileName) {
    LOG.info("Erstelle einen Demodata-Task.");
    LOG.debug("Erstelle Demodata-Task \"{}\" mit UUID \"{}\" für Anwender \"{}\".", title, uuid,
        login);

    ValidationService.topicValidation(title, taskShortDescription, taskLongDescription);
    User user = anwenderRepository.getOne(login);
    Optional<Topic> optTopic = topicRepository.findById(uuid);

    Topic t = optTopic.orElseThrow(() -> {
      LOG.debug("Topic mit der Id {} ist nicht verfügbar!", login);
      return new NoSuchElementException("Topic mit gegebener Id ist nicht verfügbar!");
    });

    if (!user.equals(t.getCreator())) {
      LOG.warn("Anwender {} ist nicht berechtigt, einen Task in dem Topic {} zu erstellen.", login,
          t.getTitle());
      throw new AccessDeniedException("Kein Zugriff auf das Topic!");
    }
    
    byte[] fileContent = ImageUtilities.imageLoader(fileName);
    Task task =
        new Task(t, title, taskShortDescription, taskLongDescription, taskType, fileContent);
    taskRepository.save(task);
    return task.getId();
  }

  @Override
  @PreAuthorize("#login == authentication.name or hasRole('ROLE_ADMIN')")
  public void updateTask(Long id, String login, String taskShortDescription,
      String taskLongDescription, TaskTypeEnum taskType, MultipartFile imageFile) {
    LOG.info("Aktualisiere einen Task.");
    LOG.debug("\tid=\"{}\" login=\"{}\" taskType={}", id, login, taskType);

    ValidationService.topicValidation(taskShortDescription, taskLongDescription);

    Task taskToUpdate = taskRepository.getOne(id);
    User changingUser = anwenderRepository.getOne(login);
    if (!changingUser.equals(taskToUpdate.getTopic().getCreator())) {
      LOG.warn("Anwender {} ist nicht berechtigt, einen Task im Topic {} zu ändern.", login,
          taskToUpdate.getTitle());
      throw new AccessDeniedException("Kein Zugriff auf das Topic!");
    }

    if (!imageFile.isEmpty()) {
      ValidationService.imageValidation(imageFile);
      try {
        taskToUpdate.setImage(imageFile.getBytes());
      } catch (IOException e) {
        LOG.error("Bild konnte nicht in ein byte[] umgewandelt werden!");
        throw new ImageException("Bild konnte nicht gespeichert werden!");
      }
    }

    taskToUpdate.setTaskShortDescription(taskShortDescription);
    taskToUpdate.setTaskLongDescription(taskLongDescription);
    taskToUpdate.setTaskType(taskType);
  }

  @Override
  @PreAuthorize("#login == authentication.name or hasRole('ROLE_ADMIN')")
  public SubscriberTaskDto getTask(Long taskId, String login) {
    LOG.info("Sehe einen Task für einen Subscriber ein.");
    LOG.debug("\ttaskId={} login=\"{}\" ein.", taskId, login);
    Task task = taskRepository.getOne(taskId);
    Topic topic = task.getTopic();
    User user = anwenderRepository.getOne(login);
    if (!(topic.getCreator().equals(user) || topic.getSubscriber().contains(user))) {
      LOG.warn("Login {} ist nicht berechtigt Task {} einzusehen!", login, taskId);
      throw new AccessDeniedException("Zugriff verweigert.");
    }
    Status status = getOrCreateStatus(taskId, login);
    return mapper.createReadDto(task, status);
  }

  @Override
  @PreAuthorize("#login == authentication.name or hasRole('ROLE_ADMIN')")
  public OwnerTaskDto getManagedTask(Long taskId, String login) {
    LOG.info("Sehe einen Task für den Owner ein.");
    LOG.debug("\ttaskId={} login=\"{}\"", taskId, login);
    Task task = taskRepository.getOne(taskId);
    Topic topic = task.getTopic();
    User createdBy = topic.getCreator();
    if (!login.equals(createdBy.getLogin())) {
      LOG.warn("Login \"{}\" ist nicht berechtigt Task {} einzusehen.", login, taskId);
      throw new AccessDeniedException("Zugriff verweigert.");
    }
    return mapper.createManagedDto(task);
  }

  @Override
  @PreAuthorize("#login == authentication.name or hasRole('ROLE_ADMIN')")
  public List<SubscriberTaskDto> getSubscribedTasks(String login, Search search) {
    LOG.info("Fordere Liste zugeordneter Tasks für einen Anwender an.");
    LOG.debug("\tlogin=\"{}\"", login);
    User user = anwenderRepository.getOne(login);
    Collection<Topic> subscriptions = user.getSubscriptions();
    List<SubscriberTaskDto> tasks = getAllTasksWithStatuses(user, subscriptions);
    if (search.hasParameters()) {
      LOG.debug("\tsearch={} isOnlyNewTasks={}", search.getSearch(), search.isOnlyNewTasks());
      for (Iterator<SubscriberTaskDto> it = tasks.iterator(); it.hasNext();) {
        SubscriberTaskDto next = it.next();
        if (!next.getTitle().toLowerCase().contains(search.getSearch().toLowerCase())) {
          it.remove();
          continue;
        }
        if (search.isOnlyNewTasks() && next.getStatus().getStatus() != StatusEnum.NEU) {
          it.remove();
        }

      }
    }
    return tasks;
  }

  private List<SubscriberTaskDto> getAllTasksWithStatuses(User user, Collection<Topic> topics) {
    Collection<Status> status = user.getStatus();
    Map<Task, Status> statusForTask = new HashMap<>();
    for (Status currentStatus : status) {
      statusForTask.put(currentStatus.getTask(), currentStatus);
    }
    List<SubscriberTaskDto> taskDtoList = new ArrayList<>();
    for (Topic t : topics) {
      for (Task task : t.getTasks()) {
        if (statusForTask.get(task) == null) {
          Status createdStatus = getOrCreateStatus(task.getId(), user.getLogin());
          statusForTask.put(task, createdStatus);
        }
        taskDtoList.add(mapper.createReadDto(task, statusForTask.get(task)));
      }
    }
    // Sortiere das Ergebnis erst nach Titel
    taskDtoList.sort(new Comparator<SubscriberTaskDto>() {
      @Override
      public int compare(SubscriberTaskDto o1, SubscriberTaskDto o2) {
        // Vergleichskriterium ist der Titel
        return o1.getTitle().compareTo(o2.getTitle());
      }
    });
    // und dann nach Status (benötigt stabilen Sortieralgorithmus)
    taskDtoList.sort(new Comparator<SubscriberTaskDto>() {
      @Override
      public int compare(SubscriberTaskDto o1, SubscriberTaskDto o2) {
        // Vergleichskriterium ist der StatusEnum nach Definitionsreihenfolge
        return o1.getStatus().getStatus().compareTo(o2.getStatus().getStatus());
      }
    });
    return taskDtoList;
  }

  @Override
  @PreAuthorize("#login == authentication.name or hasRole('ROLE_ADMIN')")
  public List<SubscriberTaskDto> getTasksForTopic(String uuid, String login) {
    LOG.info("Fordere Liste aller Tasks für Topic eines Anwenders an.");
    LOG.debug("\tuuid={} login=\"{}\"", uuid, login);
    User user = anwenderRepository.getOne(login);
    Topic topic = topicRepository.getOne(uuid);

    return getAllTasksWithStatuses(user, SetUtils.hashSet(topic));
  }

  @Override
  public List<StatusDto> getTaskStatuses(Long taskId, String login) {
    LOG.info("Fordere alle Status der Abonennten eines Tasks an.");
    LOG.debug("\ttaskId={} login=\"{}\"", taskId, login);

    List<StatusDto> statuses = new ArrayList<>();
    Task task = taskRepository.getOne(taskId);
    Topic topic = task.getTopic();
    Collection<User> users = topic.getSubscriber();

    if (!login.equals(topic.getCreator().getLogin())) {
      LOG.warn("Login \"{}\" ist nicht berechtigt Verwaltungsinformationen zu Task {} einzusehen.",
          login, taskId);
      throw new AccessDeniedException("Zugriff verweigert.");
    }

    for (User user : users) {
      Status statusForUser = getOrCreateStatus(taskId, user.getLogin());
      StatusDto statusDtoForUser =
          new StatusDto(statusForUser.getStatus(), statusForUser.getComment());
      statusDtoForUser.setUserName(user.getName());
      statuses.add(statusDtoForUser);
    }
    return statuses;
  }

  @Override
  @PreAuthorize("#login == authentication.name or hasRole('ROLE_ADMIN')")
  public void checkTask(Long taskId, String login) {
    LOG.info("Setze Status von Task auf FERTIG.");
    LOG.debug("\ttaskID={} login=\"{}\"", taskId, login);
    Status status = getOrCreateStatus(taskId, login);
    status.setStatus(StatusEnum.FERTIG);
    LOG.debug("Status von {} für Anwender {} gesetzt auf {}.", status.getTask(), status.getUser(),
        status.getStatus());
  }

  @Override
  @PreAuthorize("#login == authentication.name or hasRole('ROLE_ADMIN')")
  public void updateComment(Long taskId, String login, String comment) {
    LOG.info("Aktualisiere den Kommentar zu einem Task eines Anwenders.");
    String commentLogString = comment;
    if (comment.length() > 10) {
      commentLogString = comment.substring(0, 9);
    }
    LOG.debug("\t taskId={} login={} comment:{}...", taskId, login, commentLogString);

    // Input Validation
    if (comment.length() > 500) {
      LOG.debug("Kommentare dürfen maximal 500 Zeichen lang sein!");
      throw new ValidationException("Kommentare dürfen maximal 500 Zeichen lang sein!");
    }
    Status statusToUpdate = getOrCreateStatus(taskId, login);
    statusToUpdate.setComment(comment);
  }

  @Override
  @PreAuthorize("#login == authentication.name or hasRole('ROLE_ADMIN')")
  public void resetTask(Long taskId, String login) {
    LOG.info("Setze Status von Task auf NEU.");
    LOG.debug("\ttaskId={} login=\"{}\"", taskId, login);
    Status status = getOrCreateStatus(taskId, login);
    status.setStatus(StatusEnum.NEU);
    LOG.debug("Status von {} für Anwender {} gesetzt auf {}.", status.getTask(), status.getUser(),
        status.getStatus());
  }

  @Override
  @PreAuthorize("#login == authentication.name or hasRole('ROLE_ADMIN')")
  public List<OwnerTaskDto> getManagedTasks(String uuid, String login) {
    LOG.info("Fordere verwaltete Tasks Liste für ein Topic eines Anwenders an.");
    LOG.debug("\tuuid={} login=\"{}\"", uuid, login);
    List<OwnerTaskDto> result = new ArrayList<>();
    Topic topic = topicRepository.getOne(uuid);
    for (Task task : topic.getTasks()) {
      result.add(mapper.createManagedDto(task));
    }
    // Sortiere das Ergebnis nach Titel
    result.sort(new Comparator<OwnerTaskDto>() {
      @Override
      public int compare(OwnerTaskDto o1, OwnerTaskDto o2) {
        // Vergleichskriterium ist der Titel
        return o1.getTitle().compareTo(o2.getTitle());
      }
    });
    return result;
  }

  private Status getOrCreateStatus(Long taskId, String login) {
    User user = anwenderRepository.getOne(login);
    Task task = taskRepository.getOne(taskId);
    Status status = statusRepository.findByUserAndTask(user, task);
    if (status == null) {
      status = new Status(task, user);
      statusRepository.save(status);
    }
    return status;
  }

  @Override
  @PreAuthorize("#login == authentication.name or hasRole('ROLE_ADMIN')")
  public void deleteTaskStatusesforTopic(String topicUuid, String login) {
    User user = anwenderRepository.getOne(login);
    Topic topic = topicRepository.getOne(topicUuid);
    Collection<Task> tasks = topic.getTasks();
    int numberOfDeletedStatuses = 0;
    for (Task task : tasks) {
      numberOfDeletedStatuses += statusRepository.deleteByUserAndTask(user, task);
    }
    LOG.debug("{} Status gelöscht", numberOfDeletedStatuses);
  }

  @Override
  @PreAuthorize("#login == authentication.name or hasRole('ROLE_ADMIN')")
  public void deleteTasksFromTopic(String topicUuid, String login) {
    Topic topic = topicRepository.getOne(topicUuid);
    if (!login.equals(topic.getCreator().getLogin())) {
      LOG.warn("Login \"{}\" ist nicht berechtigt Tasks zu löschen.",
          login);
      throw new AccessDeniedException("Zugriff verweigert.");
    }
    taskRepository.deleteByTopic(topic);
  }


}
