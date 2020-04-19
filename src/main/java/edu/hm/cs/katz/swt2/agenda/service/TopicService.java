package edu.hm.cs.katz.swt2.agenda.service;

import edu.hm.cs.katz.swt2.agenda.service.dto.ManagedTaskDto;
import edu.hm.cs.katz.swt2.agenda.service.dto.ManagedTopicDto;
import edu.hm.cs.katz.swt2.agenda.service.dto.TaskDto;
import edu.hm.cs.katz.swt2.agenda.service.dto.TopicDto;
import java.util.List;

public interface TopicService {

  String createTopic(String title, String login);

  List<ManagedTopicDto> getManagedTopics(String login);

  ManagedTopicDto getManagedTopic(String uuid);

  TopicDto getTopic(String uuid, String login);

  List<ManagedTaskDto> getManagedTasks(String uuid);

  void register(String uuid, String name);

  List<TaskDto> getTasks(String uuid, String login);

  List<TopicDto> getSubscriptions(String login);
}
