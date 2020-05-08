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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.ValidationException;

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

    if(titel.length() < 8){
      throw new ValidationException("Titel müssen mindestens 8 Zeichen lang sein!");
    }
    if(titel.length() > 32){
      throw new ValidationException("Maximal Länge von 32 Zeichen überschritten!");
    }
    Topic t = topicRepository.findById(uuid).get();
    Task task = new Task(t, titel);
    taskRepository.save(task);
    return task.getId();
  }

  @Override
  @PreAuthorize("#login == authentication.name or hasRole('ROLE_ADMIN')")
  public SubscriberTaskDto getTask(Long taskId, String login) {
    Task task = taskRepository.getOne(taskId);
    Topic topic = task.getTopic();
    User user = anwenderRepository.getOne(login);
    if (!(topic.getCreator().equals(user) || topic.getSubscriber().contains(user))) {
      throw new AccessDeniedException("Zugriff verweigert.");
    }
    Status status = getOrCreateStatus(taskId, login);
    return mapper.createReadDto(task, status);
  }

  @Override
  @PreAuthorize("#login == authentication.name or hasRole('ROLE_ADMIN')")
  public OwnerTaskDto getManagedTask(Long taskId, String login) {
    Task task = taskRepository.getOne(taskId);
    Topic topic = task.getTopic();
    User createdBy = topic.getCreator();
    if (!login.equals(createdBy.getLogin())) {
      throw new AccessDeniedException("Zugriff verweigert.");
    }
    return mapper.createManagedDto(task);
  }

  @Override
  @PreAuthorize("#login == authentication.name or hasRole('ROLE_ADMIN')")
  public List<SubscriberTaskDto> getSubscribedTasks(String login) {
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
    return result;
  }

  @Override
  @PreAuthorize("#login == authentication.name or hasRole('ROLE_ADMIN')")
  public List<SubscriberTaskDto> getTasksForTopic(String uuid, String login) {
    User user = anwenderRepository.getOne(login);
    Topic topic = topicRepository.getOne(uuid);

    return extracted(user, SetUtils.hashSet(topic));
  }


  @Override
  @PreAuthorize("#login == authentication.name or hasRole('ROLE_ADMIN')")
  public void checkTask(Long taskId, String login) {
    Status status = getOrCreateStatus(taskId, login);
    status.setStatus(StatusEnum.FERTIG);
    LOG.debug("Status von Task {} und Anwender {} gesetzt auf {}", status.getTask(),
        status.getUser(), status.getStatus());
  }

  @Override
  @PreAuthorize("#login == authentication.name or hasRole('ROLE_ADMIN')")
  public List<OwnerTaskDto> getManagedTasks(String uuid, String login) {
    List<OwnerTaskDto> result = new ArrayList<>();
    Topic topic = topicRepository.getOne(uuid);
    for (Task task : topic.getTasks()) {
      result.add(mapper.createManagedDto(task));
    }
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
