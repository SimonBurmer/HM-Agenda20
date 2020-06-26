package edu.hm.cs.katz.swt2.agenda.service.dto;


/**
 * Transferobjekt für einfache Anzeigeinformationen von Tasks. Transferobjekte
 * sind Schnittstellenobjekte der Geschäftslogik; Sie sind nicht Teil des
 * Modells, so dass Änderungen an den Transferobjekten die Überprüfungen der
 * Geschäftslogik nicht umgehen können.
 * 
 * @see OwnerTaskDto
 * @see SubscriberTaskDto
 * 
 * @author Bastian Katz (mailto: bastian.katz@hm.edu)
 */
public class TaskDto {
	Long id;
	String title;
	String taskShortDescription;
	String taskLongDescription;
	SubscriberTopicDto topic;
	String base64Image;

    /*
	* Konstruktor.
	*/
	public TaskDto(Long id, String title, String taskShortDescription, String taskLongDescription,
			SubscriberTopicDto topicDto, String base64Image) {
		this.id = id;
		this.title = title;
		this.topic = topicDto;
		this.taskShortDescription = taskShortDescription;
		this.taskLongDescription = taskLongDescription;
		this.base64Image = base64Image;
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

	public String getTaskShortDescription() {
		return taskShortDescription;
	}

	public void setTaskShortDescription(String taskShortDescription) {
		this.taskShortDescription = taskShortDescription;
	}

	public String getTaskLongDescription() {
		return taskLongDescription;
	}

	public void setTaskLongDescription(String taskLongDescription) {
		this.taskLongDescription = taskLongDescription;
	}

	public SubscriberTopicDto getTopic() {
		return topic;
	}

	public void setTopic(SubscriberTopicDto topic) {
		this.topic = topic;
	}
	
	public String getBase64Image() {
	    return base64Image;
	}
}
