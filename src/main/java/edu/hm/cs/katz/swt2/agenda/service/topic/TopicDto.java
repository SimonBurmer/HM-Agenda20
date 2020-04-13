package edu.hm.cs.katz.swt2.agenda.service.topic;

public class TopicDto {
  private String uuid;
  private String creator;
  private String title;

  // TODO: DTO types überprüfen
  public TopicDto(String uuid, String creator, String title) {
    this.uuid = uuid;
    this.creator = creator;
    this.title = title;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getCreator() {
    return creator;
  }

  public void setCreator(String creator) {
    this.creator = creator;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }
}
