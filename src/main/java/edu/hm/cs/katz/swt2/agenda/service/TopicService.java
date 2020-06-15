package edu.hm.cs.katz.swt2.agenda.service;

import edu.hm.cs.katz.swt2.agenda.service.dto.OwnerTopicDto;
import edu.hm.cs.katz.swt2.agenda.service.dto.SubscriberTopicDto;
import edu.hm.cs.katz.swt2.agenda.service.dto.UserDisplayDto;

import java.util.List;
import java.util.Map;

/**
 * Serviceklasse für Verarbeitung von Topics.
 * 
 * @author Bastian Katz (mailto: bastian.katz@hm.edu)
 */
public interface TopicService {

	/**
	 * Erstellt ein neues Topic.
	 */
	String createTopic(String title, String login, String shortDescription, String longDescription);

	/**
	 * Zugriff auf ein eigenes Topic.
	 */
	OwnerTopicDto getManagedTopic(String topicUuid, String login);

	/**
	 * Zugriff auf alle eigenen Topics.
	 * 
	 * @param search
	 */
	List<OwnerTopicDto> getManagedTopics(String login, String search);

	/**
	 * Zugriff auf ein abonniertes Topic.
	 */
	SubscriberTopicDto getTopic(String topicUuid, String login);

	/**
	 * Zugriff auf alle abonnierten Topics.
	 * 
	 * @param search
	 */
	List<SubscriberTopicDto> getSubscriptions(String login, String search);

	/**
	 * Abonnieren eines Topics.
	 */
	void subscribe(String topicUuid, String login);

	/**
	 * De-Abonnieren eines Topics.
	 */
	void unsubscribe(String topicUuid, String login);

	/**
	 * Löschen eines Topics.
	 */
	void deleteTopic(String topicUuid, String login);

	/**
	 * Aktualisieren eines Topics.
	 */
	void updateTopic(String topicUuid, String login, String shortDescription, String longDescriptionn);

	/**
	 * Liefert Key des Topics.
	 */
	String getTopicUuid(String key);

	/**
	 * Liefert eine Map von Abbonenten eines Topics mit dazugehörenden abgeschlossenen Tasks.
	 */
	Map<SubscriberTopicDto, Integer> getSubscribedUsersWithFinishedTasks(String topicUuid, String login);
}
