package edu.hm.cs.katz.swt2.agenda.service.task;

import java.util.List;

public interface TaskService {

  Long createTask(String uuid, String string);

  TaskDto getTask(Long id, String name);

  ManagedTaskDto getManagedTask(Long id, String name);

  List<ReadTaskDto> getSubscribedTasks(String name);

  void checkTask(Long id, String name);
  
}
