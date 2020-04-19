package edu.hm.cs.katz.swt2.agenda.service;

import edu.hm.cs.katz.swt2.agenda.common.UuidProviderImpl;
import edu.hm.cs.katz.swt2.agenda.persistence.Task;
import edu.hm.cs.katz.swt2.agenda.persistence.Topic;
import edu.hm.cs.katz.swt2.agenda.persistence.TopicRepository;
import edu.hm.cs.katz.swt2.agenda.persistence.User;
import edu.hm.cs.katz.swt2.agenda.persistence.UserRepository;
import edu.hm.cs.katz.swt2.agenda.service.dto.ManagedTaskDto;
import edu.hm.cs.katz.swt2.agenda.service.dto.ManagedTopicDto;
import edu.hm.cs.katz.swt2.agenda.service.dto.TaskDto;
import edu.hm.cs.katz.swt2.agenda.service.dto.TopicDto;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
  UuidProviderImpl uuidProvider;
  
  @Autowired
  UserRepository anwenderRepository;
  
  @Autowired
  TopicRepository topicRepository;
  
  @Override
  @PreAuthorize("#login==authentication.name OR hasRole('ROLE_ADMIN')")
  public String createTopic(String title, String login) {
    LOG.debug("Erstelle Topic {}.", title);
    //TODO: Validation   
    String uuid = uuidProvider.getRandomUuid();
    User creator = anwenderRepository.findById(login).get();
    Topic topic = new Topic(uuid, title, creator);
    topicRepository.save(topic);
    return uuid;
  }
  
  @Override
  @PreAuthorize("#login==authentication.name")
  public List<ManagedTopicDto> getManagedTopics(String login) {
    User creator = anwenderRepository.findById(login).get();
    List<Topic> managedTopics = topicRepository.findByCreator(creator);
    List<ManagedTopicDto> result = new ArrayList<>();
    for (Topic t: managedTopics) {
      result.add(new ManagedTopicDto(t.getUuid(), creator.getLogin(), t.getTitle()));
    }
    return result;
  }

  @Override
  public ManagedTopicDto getManagedTopic(String uuid) {
    Topic topic = topicRepository.getOne(uuid);
    return new ManagedTopicDto(uuid, topic.getCreator().getLogin(), topic.getTitle());    
  }

  @Override
  public List<ManagedTaskDto> getManagedTasks(String uuid) {
    List<ManagedTaskDto> result = new ArrayList<>();
    Topic topic = topicRepository.getOne(uuid);
    TopicDto topicDto =
        new TopicDto(topic.getUuid(), topic.getCreator().getLogin(), topic.getTitle());
    for (Task t : topic.getTasks()) {
      result.add(new ManagedTaskDto(t.getId(), t.getTitle(), topicDto));
    }
    return result;
  }

  @Override
  public TopicDto getTopic(String uuid, String login) {
    Topic topic = topicRepository.getOne(uuid);
    // TODO: Nur für Abonnenten!
    return new TopicDto(uuid, topic.getCreator().getLogin(), topic.getTitle());
  }

  @Override
  public void register(String uuid, String login) {
    Topic topic = topicRepository.getOne(uuid);
    User anwender = anwenderRepository.getOne(login);
    topic.register(anwender);
    
  }

  @Override
  public List<TaskDto> getTasks(String uuid, String login) {
    // TODO: Authorisierung überprüfen
    List<TaskDto> result = new ArrayList<>();
    Topic topic = topicRepository.getOne(uuid);
    TopicDto topicDto =
        new TopicDto(topic.getUuid(), topic.getCreator().getLogin(), topic.getTitle());
    for (Task t : topic.getTasks()) {
      result.add(new TaskDto(t.getId(), t.getTitle(), topicDto));
    }
    return result;
  }

  @Override
  public List<TopicDto> getSubscriptions(String login) {
    User creator = anwenderRepository.findById(login).get();
    Collection<Topic> subscriptions = creator.getSubscriptions();
    List<TopicDto> result = new ArrayList<>();
    for (Topic t : subscriptions) {
      result.add(new TopicDto(t.getUuid(), creator.getLogin(), t.getTitle()));
    }
    return result;
  }
  
}
