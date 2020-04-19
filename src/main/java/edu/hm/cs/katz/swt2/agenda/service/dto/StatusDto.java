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

  public StatusDto(StatusEnum status) {
    this.status = status;
  }

  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }
}
