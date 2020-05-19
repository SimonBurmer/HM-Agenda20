package edu.hm.cs.katz.swt2.agenda.service;

import edu.hm.cs.katz.swt2.agenda.service.dto.OwnerTopicDto;
import edu.hm.cs.katz.swt2.agenda.service.dto.SubscriberTopicDto;
import java.util.List;

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
   */
  List<OwnerTopicDto> getManagedTopics(String login);

  /**
   * Zugriff auf ein abonniertes Topic.
   */
  SubscriberTopicDto getTopic(String topicUuid, String login);

  /**
   * Zugriff auf alle abonnierten Topics.
   */
  List<SubscriberTopicDto> getSubscriptions(String login);

  /**
   * Abonnieren eines Topics.
   */
  void subscribe(String topicUuid, String login);
}
