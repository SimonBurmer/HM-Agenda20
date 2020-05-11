package edu.hm.cs.katz.swt2.agenda.service;

import edu.hm.cs.katz.swt2.agenda.common.UuidProviderImpl;
import edu.hm.cs.katz.swt2.agenda.persistence.Topic;
import edu.hm.cs.katz.swt2.agenda.persistence.TopicRepository;
import edu.hm.cs.katz.swt2.agenda.persistence.User;
import edu.hm.cs.katz.swt2.agenda.persistence.UserRepository;
import edu.hm.cs.katz.swt2.agenda.service.dto.OwnerTopicDto;
import edu.hm.cs.katz.swt2.agenda.service.dto.SubscriberTopicDto;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
  public String createTopic(String title, String login) {

    LOG.info("Erstelle ein Topic.");
    LOG.debug("Erstelle Topic \"{}\".", title);

    if(title.length() < 10){
      throw new ValidationException("Titel müssen mindestens 10 Zeichen lang sein!");
    }
    if(title.length() > 60){
      throw new ValidationException("Maximal Länge von 60 Zeichen überschritten!");
    }

    String uuid = uuidProvider.getRandomUuid();
    User creator = anwenderRepository.findById(login).get();
    Topic topic = new Topic(uuid, title, creator);
    topicRepository.save(topic);
    return uuid;
  }

  @Override
  @PreAuthorize("#login==authentication.name")
  public List<OwnerTopicDto> getManagedTopics(String login) {
    LOG.info("Fordere Liste aller verwalteten Topics eines Anwenders an.");
    LOG.debug("Fordere Liste aller verwalteten Topics für Anwender \"{}\" an.", login);
    User creator = anwenderRepository.findById(login).get();
    List<Topic> managedTopics = topicRepository.findByCreatorOrderByTitleAsc(creator);
    List<OwnerTopicDto> result = new ArrayList<>();
    for (Topic topic : managedTopics) {
      result.add(mapper.createManagedDto(topic));
    }
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
  public SubscriberTopicDto getTopic(String uuid, String login) {
    LOG.info("Fordere Topic mit UUID für einen Anwender an.");
    LOG.debug("Fordere Topic {} für Anwender \"{}\" an.", uuid, login);
    Topic topic = topicRepository.getOne(uuid);
    return mapper.createDto(topic);
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

  @Override
  @PreAuthorize("#login == authentication.name or hasRole('ROLE_ADMIN')")
  public List<SubscriberTopicDto> getSubscriptions(String login) {
    LOG.info("Fordere Liste aller abonnierten Topics eines Anwender an.");
    LOG.debug("Fordere Liste aller abonnierten Topics für Anwender \"{}\" an.", login);
    User creator = anwenderRepository.findById(login).get();
    Collection<Topic> subscriptions = creator.getSubscriptions();

    TopicComparator tpComp = new TopicComparator();
    List<Topic> subscriptionsList = new ArrayList<Topic>(subscriptions);
    Collections.sort(subscriptionsList, tpComp);
    
    List<SubscriberTopicDto> result = new ArrayList<>();
    for (Topic topic : subscriptionsList) {
      result.add(mapper.createDto(topic));
    }
    return result;
  }

}
