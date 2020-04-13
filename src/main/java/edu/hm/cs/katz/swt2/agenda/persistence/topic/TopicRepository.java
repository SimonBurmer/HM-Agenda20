package edu.hm.cs.katz.swt2.agenda.persistence.topic;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import edu.hm.cs.katz.swt2.agenda.persistence.anwender.Anwender;

@Repository
public interface TopicRepository extends JpaRepository<Topic, String>{

  List<Topic> findByCreatedBy(Anwender creator);	

}
