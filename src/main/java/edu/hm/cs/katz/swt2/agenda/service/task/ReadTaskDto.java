package edu.hm.cs.katz.swt2.agenda.service.task;

import edu.hm.cs.katz.swt2.agenda.service.topic.TopicDto;

public class ReadTaskDto extends TaskDto {

  private StatusDto status;

  public ReadTaskDto(Long taskId, String title, TopicDto topicDto, StatusDto status) {
    super(taskId, title, topicDto);
    this.status = status;
  }

  public StatusDto getStatus() {
    return status;
  }

  public void setStatus(StatusDto status) {
    this.status = status;
  }

}
