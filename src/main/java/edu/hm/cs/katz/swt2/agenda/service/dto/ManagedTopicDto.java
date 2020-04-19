package edu.hm.cs.katz.swt2.agenda.service.dto;

/**
 * Transferobjekt für Topics mit Metadaten, die nur für Verwalter eines Topics (d.h. Eigentümer des
 * Topics) sichtbar sind. Transferobjekte sind Schnittstellenobjekte der Geschäftslogik; Sie sind
 * nicht Teil des Modells, so dass Änderungen an den Transferobjekten die Überprüfungen der
 * Geschäftslogik nicht umgehen können.
 * 
 * @see TopicDto
 * 
 * @author Bastian Katz (mailto: bastian.katz@hm.edu)
 */
public class ManagedTopicDto extends TopicDto {

  public ManagedTopicDto(String uuid, String login, String title) {
    super(uuid, login, title);
  }
}
