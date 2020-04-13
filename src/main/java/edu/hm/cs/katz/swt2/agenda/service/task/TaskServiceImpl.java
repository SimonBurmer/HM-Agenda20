package edu.hm.cs.katz.swt2.agenda.service.task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import edu.hm.cs.katz.swt2.agenda.persistence.anwender.Anwender;
import edu.hm.cs.katz.swt2.agenda.persistence.anwender.AnwenderRepository;
import edu.hm.cs.katz.swt2.agenda.persistence.task.Task;
import edu.hm.cs.katz.swt2.agenda.persistence.task.TaskRepository;
import edu.hm.cs.katz.swt2.agenda.persistence.topic.Topic;
import edu.hm.cs.katz.swt2.agenda.persistence.topic.TopicRepository;
import edu.hm.cs.katz.swt2.agenda.service.topic.TopicDto;

@Component
@Transactional
public class TaskServiceImpl implements TaskService {

  @Autowired
  TaskRepository taskRepository;

  @Autowired
  TopicRepository topicRepository;

  @Autowired
  AnwenderRepository anwenderRepository;

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
  public List<TaskDto> getSubscribedTasks(String name) {
    Anwender anwender = anwenderRepository.getOne(name);
    Collection<Topic> topics = anwender.getSubscriptions();
    List<TaskDto> result = new ArrayList<>();

    for (Topic t : topics) {
      TopicDto topicDto = new TopicDto(t.getUuid(), t.getCreatedBy().getLogin(), t.getTitle());
      for (Task task : t.getTasks()) {
        result.add(new TaskDto(task.getId(), task.getTitle(), topicDto));
      }
    }
    return result;
  }
}
