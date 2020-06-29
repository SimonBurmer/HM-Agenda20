package edu.hm.cs.katz.swt2.agenda.service.dto;

import edu.hm.cs.katz.swt2.agenda.common.TaskTypeEnum;
import java.util.List;

/**
 * Transferobjekt für Tasks mit Metadaten, die nur für Verwalter eines Tasks (d.h. Eigentümer des
 * Topics) sichtbar sind. Transferobjekte sind Schnittstellenobjekte der Geschäftslogik; Sie sind
 * nicht Teil des Modells, so dass Änderungen an den Transferobjekten die Überprüfungen der
 * Geschäftslogik nicht umgehen können.
 * 
 * @see TaskDto
 * 
 * @author Bastian Katz (mailto: bastian.katz@hm.edu)
 */
public class OwnerTaskDto extends TaskDto {

  private int amountFinished;
  private List<StatusDto> statuses;

  /**
   * Transfer DTO.
   */
  public OwnerTaskDto(Long id, String title, String taskShortDescription,
      String taskLongDescription, TaskTypeEnum taskType, SubscriberTopicDto topicDto,
      int amountCheckt, List<StatusDto> statuses,  String base64Image) {
    super(id, title, taskShortDescription, taskLongDescription, taskType, topicDto, base64Image);
    this.amountFinished = amountCheckt;
    this.statuses = statuses;
  }

  public int getAmountFinished() {
    return amountFinished;
  }

  public List<StatusDto> getStatuses() {
    return statuses;
  }

  public void setStatuses(List<StatusDto> statuses) {
    this.statuses = statuses;
  }
}

