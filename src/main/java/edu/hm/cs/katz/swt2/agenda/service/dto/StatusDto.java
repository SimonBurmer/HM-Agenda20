package edu.hm.cs.katz.swt2.agenda.service.dto;

import edu.hm.cs.katz.swt2.agenda.common.StatusEnum;

/**
 * Transferobjekt für Statusinformationen zu Tasks, die spezifisch für Abonnenten des Topics sind.
 * Transferobjekte sind Schnittstellenobjekte der Geschäftslogik; Sie sind nicht Teil des Modells,
 * so dass Änderungen an den Transferobjekten die Überprüfungen der Geschäftslogik nicht umgehen
 * können.
 * 
 * @see TaskDto
 * 
 * @author Bastian Katz (mailto: bastian.katz@hm.edu)
 */
public class StatusDto {

  private StatusEnum status;
  private String comment;
  private String userName;

  public StatusDto(StatusEnum status, String comment) {
    this.status = status;
    this.comment = comment;
    this.userName = "";
  }

  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }
}
