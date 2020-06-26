package edu.hm.cs.katz.swt2.agenda.service.dto;

/**
 * Transferobjekt für Tasks mit StatusInformationen, die spezifisch für
 * Abonnenten des Topics sindsind. Transferobjekte sind Schnittstellenobjekte
 * der Geschäftslogik; Sie sind nicht Teil des Modells, so dass Änderungen an
 * den Transferobjekten die Überprüfungen der Geschäftslogik nicht umgehen
 * können.
 * 
 * @see TaskDto
 * 
 * @author Bastian Katz (mailto: bastian.katz@hm.edu)
 */
public class SubscriberTaskDto extends TaskDto {

	private StatusDto status;

	public SubscriberTaskDto(Long taskId, String title, String taskShortDescription, String taskLongDescription,
							 SubscriberTopicDto topicDto, StatusDto status, String base64Image) {
		super(taskId, title, taskShortDescription, taskLongDescription, topicDto, base64Image);
		this.status = status;
	}

	public StatusDto getStatus() {
		return status;
	}

	public void setStatus(StatusDto status) {
		this.status = status;
	}
}
