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
import edu.hm.cs.katz.swt2.agenda.service.dto.ManagedTaskDto;
import edu.hm.cs.katz.swt2.agenda.service.dto.ReadTaskDto;
import edu.hm.cs.katz.swt2.agenda.service.dto.StatusDto;
import edu.hm.cs.katz.swt2.agenda.service.dto.TaskDto;
import edu.hm.cs.katz.swt2.agenda.service.dto.TopicDto;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
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
  public Long createTask(String uuid, String titel, String login) {
    Topic t = topicRepository.findById(uuid).get();
    Task task = new Task(t, titel);
    taskRepository.save(task);
    return task.getId();
  }


  @Override
  public TaskDto getTask(Long id, String name) {
    Task task = taskRepository.getOne(id);
    Topic topic = task.getTopic();
    TopicDto topicDto = mapper.createDto(topic);
    User anwender = anwenderRepository.getOne(name);
    if (!(topic.getCreator().equals(anwender) || topic.getSubscriber().contains(anwender))) {
      throw new RuntimeException("Access!");
    }
    return new TaskDto(task.getId(), task.getTitle(), topicDto);
  }

  @Override
  public ManagedTaskDto getManagedTask(Long id, String name) {
    Task task = taskRepository.getOne(id);
    Topic topic = task.getTopic();
    User anwender = anwenderRepository.getOne(name);
    User createdBy = topic.getCreator();
    if (!anwender.equals(createdBy)) {
      throw new AccessDeniedException("Zugriff verweigert.");
    }
    return new ManagedTaskDto(task.getId(), task.getTitle(), mapper.createDto(topic));
  }


  @Override
  public List<ReadTaskDto> getSubscribedTasks(String login) {
    User anwender = anwenderRepository.getOne(login);
    Collection<Topic> topics = anwender.getSubscriptions();
    Collection<Status> status = anwender.getStatus();
    Map<Task, Status> statusForTask = new HashMap<>();
    for (Status currentStatus : status) {
      statusForTask.put(currentStatus.getTask(), currentStatus);
    }

    List<ReadTaskDto> result = new ArrayList<>();

    for (Topic t : topics) {
      TopicDto topicDto = mapper.createDto(t);

      for (Task task : t.getTasks()) {
        if (statusForTask.get(task) == null) {
          Status createdStatus = getOrCreateStatus(task.getId(), login);
          statusForTask.put(task, createdStatus);
        }
        StatusDto statusDto = new StatusDto(statusForTask.get(task).getStatus());
        result.add(new ReadTaskDto(task.getId(), task.getTitle(), topicDto, statusDto));
      }
    }
    return result;
  }


  @Override
  // TODO: Auth
  public void checkTask(Long taskId, String login) {
    Status status = getOrCreateStatus(taskId, login);
    status.setStatus(StatusEnum.FERTIG);
    LOG.debug("Status von Task {} und Anwender {} gesetzt auf {}", status.getTask(),
        status.getUser(), status.getStatus());
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
