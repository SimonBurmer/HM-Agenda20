package edu.hm.cs.katz.swt2.agenda.service.task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import edu.hm.cs.katz.swt2.agenda.StatusEnum;
import edu.hm.cs.katz.swt2.agenda.persistence.anwender.Anwender;
import edu.hm.cs.katz.swt2.agenda.persistence.anwender.AnwenderRepository;
import edu.hm.cs.katz.swt2.agenda.persistence.status.Status;
import edu.hm.cs.katz.swt2.agenda.persistence.status.StatusRepository;
import edu.hm.cs.katz.swt2.agenda.persistence.task.Task;
import edu.hm.cs.katz.swt2.agenda.persistence.task.TaskRepository;
import edu.hm.cs.katz.swt2.agenda.persistence.topic.Topic;
import edu.hm.cs.katz.swt2.agenda.persistence.topic.TopicRepository;
import edu.hm.cs.katz.swt2.agenda.service.topic.TopicDto;

@Component
@Transactional
public class TaskServiceImpl implements TaskService {

  private static final Logger LOG = LoggerFactory.getLogger(TaskServiceImpl.class);


  @Autowired
  private TaskRepository taskRepository;

  @Autowired
  private TopicRepository topicRepository;

  @Autowired
  private AnwenderRepository anwenderRepository;

  @Autowired
  private StatusRepository statusRepository;

  @Override
  public Long createTask(String uuid, String titel) {
    Topic t = topicRepository.findById(uuid).get();
    Task task = new Task(t, titel);
    taskRepository.save(task);
    return task.getId();
  }


  @Override
  public TaskDto getTask(Long id, String name) {
    Task task = taskRepository.getOne(id);
    Topic topic = task.getTopic();
    TopicDto topicDto =
        new TopicDto(topic.getUuid(), topic.getCreatedBy().getLogin(), topic.getTitle());
    Anwender anwender = anwenderRepository.getOne(name);
    if (!(topic.getCreatedBy().equals(anwender) || topic.getSubscriber().contains(anwender))) {
      throw new RuntimeException("Access!");
    }
    return new TaskDto(task.getId(), task.getTitle(), topicDto);
  }

  @Override
  public ManagedTaskDto getManagedTask(Long id, String name) {
    Task task = taskRepository.getOne(id);
    Topic topic = task.getTopic();
    TopicDto topicDto =
        new TopicDto(topic.getUuid(), topic.getCreatedBy().getLogin(), topic.getTitle());
    Anwender anwender = anwenderRepository.getOne(name);
    if (!(topic.getCreatedBy().equals(anwender) || topic.getSubscriber().contains(anwender))) {
      throw new RuntimeException("Access!");
    }
    return new ManagedTaskDto(task.getId(), task.getTitle(), topicDto);
  }


  @Override
  public List<ReadTaskDto> getSubscribedTasks(String login) {
    Anwender anwender = anwenderRepository.getOne(login);
    Collection<Topic> topics = anwender.getSubscriptions();
    Collection<Status> status = anwender.getStatus();
    Map<Task, Status> statusForTask = new HashMap<>();
    for (Status aStatus : status) {
      statusForTask.put(aStatus.getTask(), aStatus);
    }

    List<ReadTaskDto> result = new ArrayList<>();

    for (Topic t : topics) {
      TopicDto topicDto = new TopicDto(t.getUuid(), t.getCreatedBy().getLogin(), t.getTitle());

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
        status.getAnwender(), status.getStatus());
  }


  private Status getOrCreateStatus(Long taskId, String login) {
    Anwender anwender = anwenderRepository.getOne(login);
    Task task = taskRepository.getOne(taskId);
    Status status = statusRepository.findByAnwenderAndTask(anwender, task);
    if (status == null) {
      status = new Status(task, anwender);
      statusRepository.save(status);
    }
    return status;
  }


}
