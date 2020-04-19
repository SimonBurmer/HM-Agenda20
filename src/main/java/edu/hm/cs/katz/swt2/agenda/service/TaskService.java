package edu.hm.cs.katz.swt2.agenda.service;

import edu.hm.cs.katz.swt2.agenda.service.dto.ManagedTaskDto;
import edu.hm.cs.katz.swt2.agenda.service.dto.ReadTaskDto;
import edu.hm.cs.katz.swt2.agenda.service.dto.TaskDto;
import java.util.List;

public interface TaskService {

  Long createTask(String topicUuid, String title, String login);

  TaskDto getTask(Long taskId, String login);

  ManagedTaskDto getManagedTask(Long taskId, String login);

  List<ReadTaskDto> getSubscribedTasks(String login);

  void checkTask(Long taskId, String login);
  
}
