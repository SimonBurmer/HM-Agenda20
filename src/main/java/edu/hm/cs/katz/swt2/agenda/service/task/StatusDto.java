package edu.hm.cs.katz.swt2.agenda.service.task;

import edu.hm.cs.katz.swt2.agenda.StatusEnum;

public class StatusDto {

  private StatusEnum status;


  /**
   * @param status
   */
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
