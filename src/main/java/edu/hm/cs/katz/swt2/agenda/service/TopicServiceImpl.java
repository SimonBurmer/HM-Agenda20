package edu.hm.cs.katz.swt2.agenda.service;

import edu.hm.cs.katz.swt2.agenda.common.UuidProviderImpl;
import edu.hm.cs.katz.swt2.agenda.persistence.Topic;
import edu.hm.cs.katz.swt2.agenda.persistence.TopicRepository;
import edu.hm.cs.katz.swt2.agenda.persistence.User;
import edu.hm.cs.katz.swt2.agenda.persistence.UserRepository;
import edu.hm.cs.katz.swt2.agenda.service.dto.OwnerTopicDto;
import edu.hm.cs.katz.swt2.agenda.service.dto.SubscriberTopicDto;
import edu.hm.cs.katz.swt2.agenda.service.dto.UserDisplayDto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(rollbackFor = Exception.class)
public class TopicServiceImpl implements TopicService {

	private static final Logger LOG = LoggerFactory.getLogger(TopicServiceImpl.class);

	@Autowired
	private UuidProviderImpl uuidProvider;

	@Autowired
	private UserRepository anwenderRepository;

	@Autowired
	private TopicRepository topicRepository;

	@Autowired
	private DtoMapper mapper;

	@Override
	@PreAuthorize("#login==authentication.name OR hasRole('ROLE_ADMIN')")
	public String createTopic(String title, String login, String shortDescription, String longDescription) {

		LOG.info("Erstelle ein Topic.");
		LOG.debug("Erstelle Topic \"{}\".", title);

		// Check title, shortDescription, longDescription requirements
		if (title.length() < 10) {
			LOG.debug("Titel müssen mindestens 10 Zeichen lang sein!");
			throw new ValidationException("Titel müssen mindestens 10 Zeichen lang sein!");
		}

		if (title.length() > 60) {
			LOG.debug("Maximale Länge von 60 Zeichen überschritten!");
			throw new ValidationException("Maximale Länge von 60 Zeichen überschritten!");
		}

		if (shortDescription.length() < 100) {
			LOG.debug("Kurzbeschreibungen müssen mindestens 100 Zeichen lang sein!");
			throw new ValidationException("Kurzbeschreibungen müssen mindestens 100 Zeichen lang sein!");
		}

		if (shortDescription.length() > 200) {
			LOG.debug("Maximale Länge von 200 Zeichen überschritten!");
			throw new ValidationException("Maximale Länge von 200 Zeichen überschritten!");
		}

		if (longDescription.length() < 200) {
			LOG.debug("Kurzbeschreibungen müssen mindestens 200 Zeichen lang sein!");
			throw new ValidationException("Kurzbeschreibungen müssen mindestens 200 Zeichen lang sein!");
		}
		if (longDescription.length() > 2000) {
			LOG.debug("Maximale Länge von 2000 Zeichen überschritten!");
			throw new ValidationException("Maximale Länge von 2000 Zeichen überschritten!");
		}

		String uuid = uuidProvider.getRandomUuid();
		User creator = anwenderRepository.findById(login).get();
		Topic topic = new Topic(uuid, title, creator, shortDescription, longDescription);
		topicRepository.save(topic);
		return uuid;
	}

	@Override
	@PreAuthorize("#login==authentication.name")
	public List<OwnerTopicDto> getManagedTopics(String login, String search) {
		LOG.info("Fordere Liste aller verwalteten Topics eines Anwenders an.");
		LOG.debug("Fordere Liste aller verwalteten Topics für Anwender \"{}\" an.", login);
		User creator = anwenderRepository.findById(login).get();
		List<Topic> managedTopics = topicRepository.findByCreatorOrderByTitleAsc(creator);
		List<OwnerTopicDto> result = new ArrayList<>();
		for (Topic topic : managedTopics) {
			result.add(mapper.createManagedDto(topic));
		}

		result.removeIf(t -> !t.getTitle().contains(search));

		return result;
	}

	@Override
	@PreAuthorize("#login == authentication.name or hasRole('ROLE_ADMIN')")
	public OwnerTopicDto getManagedTopic(String topicUuid, String login) {
		LOG.info("Fordere ein verwaltetes Topic eines Anwenders an.");
		LOG.debug("Fordere ein verwaltetes Topic {} für Anwender \"{}\" an.", topicUuid, login);
		Topic topic = topicRepository.getOne(topicUuid);
		return mapper.createManagedDto(topic);
	}

	@Override
	@PreAuthorize("#login == authentication.name or hasRole('ROLE_ADMIN')")
	public SubscriberTopicDto getTopic(String topicUuid, String login) {
		LOG.info("Fordere Topic mit UUID für einen Anwender an.");
		LOG.debug("Fordere Topic {} für Anwender \"{}\" an.", topicUuid, login);
		Topic topic = topicRepository.getOne(topicUuid);
		return mapper.createDto(topic, login);
	}

	@Override
	@PreAuthorize("#login == authentication.name or hasRole('ROLE_ADMIN')")
	public List<SubscriberTopicDto> getSubscribedUsersWithFinishedTasks(String topicUuid, String login) {
		LOG.info("Fordere Abbonenten eines Topics an.");
		LOG.debug("Fordere Abbonenten {} für Topic \"{}\" an.", topicUuid, login);

		Topic topic = topicRepository.getOne(topicUuid);
		Collection<User> subscribers = topic.getSubscriber();
		System.out.println("Test1");
		List<SubscriberTopicDto> subscribersWithFinishedTaks = new ArrayList<>();
		System.out.println("Test2");
		for (User u : subscribers) {
			System.out.println("Test3");
			SubscriberTopicDto subscriberTopic = mapper.createDto(topic, u.getLogin());
			System.out.println("Test3.1");
			subscriberTopic.setSubscriberForTopic(mapper.createDto(u));
			System.out.println("Test4");
			subscribersWithFinishedTaks.add(subscriberTopic);
		}
		System.out.println("Test5");
		subscribersWithFinishedTaks.sort(new Comparator<SubscriberTopicDto>() {

			@Override
			public int compare(SubscriberTopicDto left, SubscriberTopicDto right) {

				if ((left.getAmountFinishedTasks() - right.getAmountFinishedTasks()) < 0) {
					return -1;
				} else if ((left.getAmountFinishedTasks() - right.getAmountFinishedTasks()) > 0) {
					return 1;
				}
				return 0;
			}
		});
		

		return subscribersWithFinishedTaks;

	}

	@Override
	@PreAuthorize("#login == authentication.name or hasRole('ROLE_ADMIN')")
	public void subscribe(String topicUuid, String login) {
		LOG.info("Abonniere ein Topic für einen Anwender.");
		LOG.debug("Abonniere Topic {} für Anwender \"{}\".", topicUuid, login);
		Topic topic = topicRepository.getOne(topicUuid);
		User anwender = anwenderRepository.getOne(login);
		topic.register(anwender);
	}

	@PreAuthorize("#login == authentication.name or hasRole('ROLE_ADMIN')")
	@Override
	public void unsubscribe(String topicUuid, String login) {
		LOG.info("De-Abonniere ein Topic für einen Anwender.");
		LOG.debug("De-Abonniere Topic {} für Anwender \"{}\".", topicUuid, login);
		Topic topic = topicRepository.getOne(topicUuid);
		User anwender = anwenderRepository.getOne(login);
		topic.unregister(anwender);
	}

	@Override
	@PreAuthorize("#login == authentication.name or hasRole('ROLE_ADMIN')")
	public List<SubscriberTopicDto> getSubscriptions(String login, String search) {
		LOG.info("Fordere Liste aller abonnierten Topics eines Anwender an.");
		LOG.debug("Fordere Liste aller abonnierten Topics für Anwender \"{}\" an.", login);
		User creator = anwenderRepository.findById(login).get();
		Collection<Topic> subscriptions = creator.getSubscriptions();

		TopicComparator tpComp = new TopicComparator();
		List<Topic> subscriptionsList = new ArrayList<Topic>(subscriptions);
		Collections.sort(subscriptionsList, tpComp);

		List<SubscriberTopicDto> result = new ArrayList<>();
		for (Topic topic : subscriptionsList) {
			result.add(mapper.createDto(topic, login));
		}

		result.removeIf(t -> !t.getTitle().contains(search));

		return result;
	}

	@Override
	@PreAuthorize("#login == authentication.name or hasRole('ROLE_ADMIN')")
	public void deleteTopic(String topicUuid, String login) {
		LOG.info("Löschen eins Topics von einem Anwender.");
		LOG.debug("Lösche Topic {} von Anwender \"{}\".", topicUuid, login);
		Topic topic = topicRepository.getOne(topicUuid);
		User creator = anwenderRepository.getOne(login);
		if (!topic.getCreator().equals(creator)) {
			throw new AccessDeniedException("Kein Zugriff auf das Topic!");
		}
		topicRepository.delete(topic);
	}

	@Override
	@PreAuthorize("#login == authentication.name or hasRole('ROLE_ADMIN')")
	public void updateTopic(String topicUuid, String login, String shortDescription, String longDescription) {
		LOG.info("Update eine Topic von einem Anwender.");
		LOG.debug("Update Topic {} von Anwender \"{}\".", topicUuid, login);
		Topic topic = topicRepository.getOne(topicUuid);
		User creator = anwenderRepository.getOne(login);
		if (!topic.getCreator().equals(creator)) {
			throw new AccessDeniedException("Kein Zugriff auf das Topic!");
		}
		if (shortDescription.length() < 100) {
			LOG.debug("Kurzbeschreibungen müssen mindestens 100 Zeichen lang sein!");
			throw new ValidationException("Kurzbeschreibungen müssen mindestens 100 Zeichen lang sein!");
		}

		if (shortDescription.length() > 200) {
			LOG.debug("Maximale Länge von 200 Zeichen überschritten!");
			throw new ValidationException("Maximale Länge von 200 Zeichen überschritten!");
		}

		if (longDescription.length() < 200) {
			LOG.debug("Kurzbeschreibungen müssen mindestens 200 Zeichen lang sein!");
			throw new ValidationException("Kurzbeschreibungen müssen mindestens 200 Zeichen lang sein!");
		}
		if (longDescription.length() > 2000) {
			LOG.debug("Maximale Länge von 2000 Zeichen überschritten!");
			throw new ValidationException("Maximale Länge von 2000 Zeichen überschritten!");
		}

		topic.setShortDescription(shortDescription);
		topic.setLongDescription(longDescription);
	}

	@Override
	public String getTopicUuid(String key) {
		LOG.info("Uuid auflösen für Key {}.", key);
		if (key.length() < 8) {
			throw new ValidationException("Kurzschlüssel ist zu kurz. Ein Kurzschlüssel hat 8 Zeichen");
		}

		if (key.length() >= 9) {
			throw new ValidationException("Kurzschlüssel ist zu lang. Ein Kurzschlüssel hat 8 Zeichen");
		}
		Topic topic = topicRepository.findByUuidEndingWith(key);

		if (topic == null) {
			throw new ValidationException("Zu diesem Kurzschlüssel gibt es kein Topic!");
		}
		return topic.getUuid();
	}
}
