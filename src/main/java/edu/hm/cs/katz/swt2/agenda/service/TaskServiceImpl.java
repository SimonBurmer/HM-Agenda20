package edu.hm.cs.katz.swt2.agenda.service;

import edu.hm.cs.katz.swt2.agenda.common.StatusEnum;
import edu.hm.cs.katz.swt2.agenda.persistence.Status;
import edu.hm.cs.katz.swt2.agenda.persistence.StatusRepository;
import edu.hm.cs.katz.swt2.agenda.persistence.Task;
import edu.hm.cs.katz.swt2.agenda.persistence.TaskRepository;
import edu.hm.cs.katz.swt2.agenda.persistence.Topic;
import edu.hm.cs.katz.swt2.agenda.persistence.TopicRepository;
import edu.hm.cs.katz.swt2.agenda.persistence.User;
import edu.hm.cs.katz.swt2.agenda.persistence.UserRepository;
import edu.hm.cs.katz.swt2.agenda.service.dto.OwnerTaskDto;
import edu.hm.cs.katz.swt2.agenda.service.dto.SubscriberTaskDto;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.SetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
  public Long createTask(String uuid, String titel, String login) {
    LOG.info("Erstelle einen Task.");
    LOG.debug("Erstelle Task \"{}\" mit UUID \"{}\" für Anwender \"{}\".", titel, uuid, login);
    Topic t = topicRepository.findById(uuid).get();
    Task task = new Task(t, titel);
    taskRepository.save(task);
    return task.getId();
  }

  @Override
  @PreAuthorize("#login == authentication.name or hasRole('ROLE_ADMIN')")
  public SubscriberTaskDto getTask(Long taskId, String login) {
    LOG.info("Sehe einen Task für einen Subscriber ein.");
    LOG.debug("Sehe Task {} für Subscriber \"{}\" ein.", taskId, login);
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
    LOG.debug("Sehe Task {} für Owner \"{}\" ein.", taskId, login);
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
  public List<SubscriberTaskDto> getSubscribedTasks(String login) {
    LOG.info("Fordere Liste zugeordneter Tasks für einen Anwender an.");
    LOG.debug("Fordere Liste zugeordneter Tasks für Anwender \"{}\" an.", login);
    User user = anwenderRepository.getOne(login);
    Collection<Topic> topics = user.getSubscriptions();
    return extracted(user, topics);
  }

  private List<SubscriberTaskDto> extracted(User user, Collection<Topic> topics) {
    Collection<Status> status = user.getStatus();
    Map<Task, Status> statusForTask = new HashMap<>();
    for (Status currentStatus : status) {
      statusForTask.put(currentStatus.getTask(), currentStatus);
    }

    List<SubscriberTaskDto> result = new ArrayList<>();

    for (Topic t : topics) {
      for (Task task : t.getTasks()) {
        if (statusForTask.get(task) == null) {
          Status createdStatus = getOrCreateStatus(task.getId(), user.getLogin());
          statusForTask.put(task, createdStatus);
        }
        result.add(mapper.createReadDto(task, statusForTask.get(task)));
      }
    }

    // Sortiere das Ergebnis erst nach Titel
    result.sort(new Comparator<SubscriberTaskDto>() {
      @Override
      public int compare(SubscriberTaskDto o1, SubscriberTaskDto o2) {
        // Vergleichskriterium ist der Titel
        return o1.getTitle().compareTo(o2.getTitle());
      }
    });
    // und dann nach Status (benötigt stabilen Sortieralgorithmus)
    result.sort(new Comparator<SubscriberTaskDto>() {
      @Override
      public int compare(SubscriberTaskDto o1, SubscriberTaskDto o2) {
        // Vergleichskriterium ist der StatusEnum nach Definitionsreihenfolge
        return o1.getStatus().getStatus().compareTo(o2.getStatus().getStatus());
      }
    });

    return result;
  }

  @Override
  @PreAuthorize("#login == authentication.name or hasRole('ROLE_ADMIN')")
  public List<SubscriberTaskDto> getTasksForTopic(String uuid, String login) {
    LOG.info("Fordere Liste aller Tasks für Topic eines Anwenders an.");
    LOG.debug("Fordere Liste aller Tasks für Topic mit UUID {} und Anwender \"{}\" an.",
            uuid, login);
    User user = anwenderRepository.getOne(login);
    Topic topic = topicRepository.getOne(uuid);

    return extracted(user, SetUtils.hashSet(topic));
  }


  @Override
  @PreAuthorize("#login == authentication.name or hasRole('ROLE_ADMIN')")
  public void checkTask(Long taskId, String login) {
    LOG.info("Setze Status von Task auf FERTIG.");
    LOG.debug("Setze Status von Task {} für Login \"{}\" auf FERTIG.", taskId, login);
    Status status = getOrCreateStatus(taskId, login);
    status.setStatus(StatusEnum.FERTIG);
    LOG.debug("Status von {} und Anwender {} gesetzt auf {}.", status.getTask(),
        status.getUser(), status.getStatus());
  }

  @Override
  @PreAuthorize("#login == authentication.name or hasRole('ROLE_ADMIN')")
  public List<OwnerTaskDto> getManagedTasks(String uuid, String login) {
    LOG.info("Fordere verwaltete Tasks Liste für ein Topic eines Anwenders an.");
    LOG.debug("Fordere verwaltete Tasks Liste für Topic {} und Anwender \"{}\" an.", uuid, login);
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
}
