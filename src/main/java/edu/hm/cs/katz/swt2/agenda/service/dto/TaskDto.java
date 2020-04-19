package edu.hm.cs.katz.swt2.agenda.service.dto;

/**
 * Transferobjekt für einfache Anzeigeinformationen von Tasks. Transferobjekte sind
 * Schnittstellenobjekte der Geschäftslogik; Sie sind nicht Teil des Modells, so dass Änderungen an
 * den Transferobjekten die Überprüfungen der Geschäftslogik nicht umgehen können.
 * 
 * @see OwnerTaskDto
 * @see SubscriberTaskDto
 * 
 * @author Bastian Katz (mailto: bastian.katz@hm.edu)
 */
public class TaskDto {
  Long id;
  String title;
  SubscriberTopicDto topic;

  /**
   * Konstruktor.
   */
  public TaskDto(Long id, String title, SubscriberTopicDto topicDto) {
    this.id = id;
    this.title = title;
    this.topic = topicDto;
  }

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public SubscriberTopicDto getTopic() {
    return topic;
  }

  public void setTopic(SubscriberTopicDto topic) {
    this.topic = topic;
  }


}
