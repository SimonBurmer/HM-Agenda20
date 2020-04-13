package edu.hm.cs.katz.swt2.agenda.service.topic;

import java.util.List;
import edu.hm.cs.katz.swt2.agenda.service.task.ManagedTaskDto;
import edu.hm.cs.katz.swt2.agenda.service.task.TaskDto;

public interface TopicService {

  String createTopic(String title, String login);

  List<ManagedTopicDto> getManagedTopics(String login);

  ManagedTopicDto getManagedTopic(String uuid);
  TopicDto getTopic(String uuid);
  List<ManagedTaskDto> getManagedTasks(String uuid);

  void register(String uuid, String name);

  List<TaskDto> getTasks(String uuid);

  List<TopicDto> getSubscriptions(String login);



}
