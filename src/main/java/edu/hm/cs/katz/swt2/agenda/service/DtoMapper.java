package edu.hm.cs.katz.swt2.agenda.service;

import edu.hm.cs.katz.swt2.agenda.persistence.Status;
import edu.hm.cs.katz.swt2.agenda.persistence.Task;
import edu.hm.cs.katz.swt2.agenda.persistence.Topic;
import edu.hm.cs.katz.swt2.agenda.persistence.User;
import edu.hm.cs.katz.swt2.agenda.service.dto.OwnerTaskDto;
import edu.hm.cs.katz.swt2.agenda.service.dto.OwnerTopicDto;
import edu.hm.cs.katz.swt2.agenda.service.dto.StatusDto;
import edu.hm.cs.katz.swt2.agenda.service.dto.SubscriberTaskDto;
import edu.hm.cs.katz.swt2.agenda.service.dto.SubscriberTopicDto;
import edu.hm.cs.katz.swt2.agenda.service.dto.UserDisplayDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Hilfskomponente zum Erstellen der Transferobjekte aus den Entities. Für diese Aufgabe gibt es
 * viele Frameworks, die aber zum Teil recht schwer zu verstehen sind. Da das Mapping sonst zu viel
 * redundantem Code führt, ist die Zusammenführung aber notwendig.
 * 
 * @author Bastian Katz (mailto: bastian.katz@hm.edu)
 */
@Component
public class DtoMapper {

  @Autowired
  private ModelMapper mapper;

  /**
   * Erstellt ein {@link UserDisplayDto} aus einem {@link User}.
   */
  public UserDisplayDto createDto(User user) {
    return new UserDisplayDto(user.getLogin());
  }

  /**
   * Erstellt ein {@link SubscriberTopicDto} aus einem {@link Topic}.
   */
  public SubscriberTopicDto createDto(Topic topic) {
    UserDisplayDto creatorDto = mapper.map(topic.getCreator(), UserDisplayDto.class);
    SubscriberTopicDto topicDto =
        new SubscriberTopicDto(topic.getUuid(), creatorDto, topic.getTitle());
    return topicDto;
  }

  /**
   * Erstellt ein {@link StatusDto} aus einem {@link Status}.
   */
  public StatusDto createDto(Status status) {
    return new StatusDto(status.getStatus());
  }

  /**
   * Erstellt ein {@link SubscriberTaskDto} aus einem {@link Task} und einem {@link Status}.
   */
  public SubscriberTaskDto createReadDto(Task task, Status status) {
    Topic topic = task.getTopic();
    SubscriberTopicDto topicDto = createDto(topic);
    return new SubscriberTaskDto(task.getId(), task.getTitle(), topicDto, createDto(status));
  }

  /**
   * Erstellt ein {@link OwnerTopicDto} aus einem {@link Topic}.
   */
  public OwnerTopicDto createManagedDto(Topic topic) {
    return new OwnerTopicDto(topic.getUuid(), createDto(topic.getCreator()), topic.getTitle());
  }

  public OwnerTaskDto createManagedDto(Task task) {
    return new OwnerTaskDto(task.getId(), task.getTitle(), createDto(task.getTopic()));
  }

}
