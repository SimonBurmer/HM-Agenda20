package edu.hm.cs.katz.swt2.agenda.service.dto;

/**
 * Transferobjekt für einfache Anzeigeinformationen von Topics. Transferobjekte sind
 * Schnittstellenobjekte der Geschäftslogik; Sie sind nicht Teil des Modells, so dass Änderungen an
 * den Transferobjekten die Überprüfungen der Geschäftslogik nicht umgehen können.
 * 
 * @see ManagedTopicDto
 * 
 * @author Bastian Katz (mailto: bastian.katz@hm.edu)
 */

public class TopicDto {
  private String uuid;
  private UserDisplayDto creator;
  private String title;

  /**
   * Konstruktor.
   */
  public TopicDto(String uuid, UserDisplayDto creator, String title) {
    this.uuid = uuid;
    this.creator = creator;
    this.title = title;
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
}
