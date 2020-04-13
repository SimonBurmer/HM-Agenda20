package edu.hm.cs.katz.swt2.agenda.service.task;

import edu.hm.cs.katz.swt2.agenda.service.topic.TopicDto;

public class TaskDto {
  Long id;
  String title;
  TopicDto topic;
  
  public TaskDto(Long id, String title, TopicDto topicDto) {
    this.id = id;
    this.title = title;
    this.topic = topicDto;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public TopicDto getTopic() {
    return topic;
  }

  public void setTopic(TopicDto topic) {
    this.topic = topic;
  }
  
  
}
