package edu.hm.cs.katz.swt2.agenda.service;

import edu.hm.cs.katz.swt2.agenda.persistence.Topic;
import edu.hm.cs.katz.swt2.agenda.service.dto.TopicDto;
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
   * Erstellt ein {@link TopicDto} aus einem {@link Topic}.
   */
  public TopicDto createDto(Topic topic) {
    UserDisplayDto creatorDto = mapper.map(topic.getCreator(), UserDisplayDto.class);
    TopicDto topicDto = new TopicDto(topic.getUuid(), creatorDto, topic.getTitle());
    return topicDto;
  }
}
