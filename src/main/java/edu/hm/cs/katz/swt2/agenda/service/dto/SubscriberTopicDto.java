package edu.hm.cs.katz.swt2.agenda.service.dto;

/**
 * Transferobjekt für einfache Anzeigeinformationen von Topics. Transferobjekte sind
 * Schnittstellenobjekte der Geschäftslogik; Sie sind nicht Teil des Modells, so dass Änderungen an
 * den Transferobjekten die Überprüfungen der Geschäftslogik nicht umgehen können.
 * 
 * @see OwnerTopicDto
 * 
 * @author Bastian Katz (mailto: bastian.katz@hm.edu)
 */
public class SubscriberTopicDto {
  private String uuid;
  private UserDisplayDto creator;
  private UserDisplayDto subscriberForTopic;
  private String title;
  private String shortDescription;
  private String longDescription;
  private int amountUnfinishedTasks;
  private int amountFinishedTasks;
  private int amountTasks;

  /**
   * Konstruktor mit amountUnfinishedTasks.
   */
  public SubscriberTopicDto(String uuid, UserDisplayDto creator, String title,
      String shortDescription, String longDescription) {
    this.uuid = uuid;
    this.creator = creator;
    this.title = title;
    this.shortDescription = shortDescription;
    this.longDescription = longDescription;
  }

  public String getUuid() {
    return uuid;
  }

  public UserDisplayDto getCreator() {
    return creator;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getShortDescription() {
    return shortDescription;
  }

  public void setShortDescription(String shortDescription) {
    this.shortDescription = shortDescription;
  }

  public String getLongDescription() {
    return longDescription;
  }

  public void setLongDescription(String longDescription) {
    this.longDescription = longDescription;
  }

  public int getAmountUnfinishedTasks() {
    return amountUnfinishedTasks;
  }

  public void setAmountUnfinishedTasks(int amountUnfinishedTasks) {
    this.amountUnfinishedTasks = amountUnfinishedTasks;
  }

  public int getAmountTasks() {
    return amountTasks;
  }

  public void setAmountTasks(int amountTasks) {
    this.amountTasks = amountTasks;
  }

  public int getAmountFinishedTasks() {
    return amountFinishedTasks;
  }

  public void setAmountFinishedTasks(int amountFinishedTasks) {
    this.amountFinishedTasks = amountFinishedTasks;
  }

  public UserDisplayDto getSubscriberForTopic() {
    return subscriberForTopic;
  }

  public void setSubscriberForTopic(UserDisplayDto subscriberForTopic) {
    this.subscriberForTopic = subscriberForTopic;
  }

}
