package edu.hm.cs.katz.swt2.agenda.service;

import edu.hm.cs.katz.swt2.agenda.common.StatusEnum;
import edu.hm.cs.katz.swt2.agenda.persistence.Status;
import edu.hm.cs.katz.swt2.agenda.persistence.StatusRepository;
import edu.hm.cs.katz.swt2.agenda.persistence.Task;
import edu.hm.cs.katz.swt2.agenda.persistence.Topic;
import edu.hm.cs.katz.swt2.agenda.persistence.TopicRepository;
import edu.hm.cs.katz.swt2.agenda.persistence.User;
import edu.hm.cs.katz.swt2.agenda.service.dto.OwnerTaskDto;
import edu.hm.cs.katz.swt2.agenda.service.dto.OwnerTopicDto;
import edu.hm.cs.katz.swt2.agenda.service.dto.StatusDto;
import edu.hm.cs.katz.swt2.agenda.service.dto.SubscriberTaskDto;
import edu.hm.cs.katz.swt2.agenda.service.dto.SubscriberTopicDto;
import edu.hm.cs.katz.swt2.agenda.service.dto.UserDisplayDto;
import java.util.List;
import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Hilfskomponente zum Erstellen der Transferobjekte aus den Entities. Für diese
 * Aufgabe gibt es viele Frameworks, die aber zum Teil recht schwer zu verstehen
 * sind. Da das Mapping sonst zu viel redundantem Code führt, ist die
 * Zusammenführung aber notwendig.
 * 
 * @author Bastian Katz (mailto: bastian.katz@hm.edu)
 */
@Component
public class DtoMapper {

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private TopicRepository topicRepository;

	@Autowired
	private TaskService taskService;

	@Autowired
	private StatusRepository statusRepository;

	/**
	 * Erstellt ein {@link UserDisplayDto} aus einem {@link User}.
	 */
	public UserDisplayDto createDto(User user) {
		UserDisplayDto dto = mapper.map(user, UserDisplayDto.class);
		dto.setTopicCount(topicRepository.countByCreator(user));
		dto.setSubscriptionCount(user.getSubscriptions().size());
		return dto;
	}

	/**
	 * Erstellt ein {@link SubscriberTopicDto} aus einem {@link Topic}
	 */
	public SubscriberTopicDto createDto(Topic topic) {
		UserDisplayDto creatorDto = createDto(topic.getCreator());
		return new SubscriberTopicDto(topic.getUuid(), creatorDto, topic.getTitle(), topic.getShortDescription(),
				topic.getLongDescription());
	}

	/**
	 * Erstellt ein {@link SubscriberTopicDto} aus einem {@link Topic} und einem
	 * {@link login}. Berechnet die Anzahl der offenen Tasks des Topics.
	 * 
	 */
	public SubscriberTopicDto createDto(Topic topic, String login) {
		int amountUnfinishedTasks = 0;
		int amountFinishedTasks = 0;
		int amountTasks = 0;

		List<SubscriberTaskDto> topicTasks = taskService.getTasksForTopic(topic.getUuid(), login);
		for (SubscriberTaskDto t : topicTasks) {
			if (t.getStatus().getStatus().equals(StatusEnum.FERTIG)) {
				++amountFinishedTasks;
			}
		}

		amountTasks = topicTasks.size();
		amountUnfinishedTasks = amountTasks - amountFinishedTasks;

		UserDisplayDto creatorDto = createDto(topic.getCreator());

		SubscriberTopicDto subscriberTopicDto = new SubscriberTopicDto(topic.getUuid(), creatorDto, topic.getTitle(),
				topic.getShortDescription(), topic.getLongDescription());

		subscriberTopicDto.setAmountUnfinishedTasks(amountUnfinishedTasks);
		subscriberTopicDto.setAmountFinishedTasks(amountFinishedTasks);
		subscriberTopicDto.setAmountTasks(amountTasks);

		
		return subscriberTopicDto;
	}

	/**
	 * Erstellt ein {@link StatusDto} aus einem {@link Status}.
	 */
	public StatusDto createDto(Status status) {
		return new StatusDto(status.getStatus());
	}

	/**
	 * Erstellt ein {@link SubscriberTaskDto} aus einem {@link Task} und einem
	 * {@link Status}.
	 */
	public SubscriberTaskDto createReadDto(Task task, Status status) {
		Topic topic = task.getTopic();
		SubscriberTopicDto topicDto = createDto(topic);
		return new SubscriberTaskDto(task.getId(), task.getTitle(), task.getTaskShortDescription(),
				task.getTaskLongDescription(), topicDto, createDto(status));
	}

	/**
	 * Erstellt ein {@link OwnerTopicDto} aus einem {@link Topic}. Berechnet die
	 * Anzahl der Subscriber des Topics.
	 */
	public OwnerTopicDto createManagedDto(Topic topic) {
		int amountSubscriber = 0;
		amountSubscriber = topic.getSubscriber().size();
		OwnerTopicDto ownerTopicDto = new OwnerTopicDto(topic.getUuid(), createDto(topic.getCreator()),
				topic.getTitle(), topic.getShortDescription(), topic.getLongDescription(), topic.getSubscriber());
		ownerTopicDto.setAmountSubscriber(amountSubscriber);
		return ownerTopicDto;
	}

	/**
	 * Erstellt ein {@link OwnerTaskDto} aus einem {@link Task}. Berechnet die
	 * Anzahl der User die diesen Task abgeschlossen haben.
	 */
	public OwnerTaskDto createManagedDto(Task task) {
		int amountFinished = 0;
		List<Status> status = statusRepository.findByTask(task);
		for (Status s : status) {
			if (s.getStatus().equals(StatusEnum.FERTIG)) {
				++amountFinished;
			}
		}
		return new OwnerTaskDto(task.getId(), task.getTitle(), task.getTaskShortDescription(),
				task.getTaskLongDescription(), createDto(task.getTopic()), amountFinished);
	}

}
