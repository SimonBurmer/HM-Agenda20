package edu.hm.cs.katz.swt2.agenda.persistence.topic;

import edu.hm.cs.katz.swt2.agenda.persistence.anwender.Anwender;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository zum Zugriff auf gespeicherte Topics. Repostory-Interfaces erben eine unglaubliche
 * Menge hilfreicher Methoden. Weitere Methoden kann man einfach durch Benennung definierern. Spring
 * Data ergänzt die Implementierungen zur Laufzeit.
 * 
 * @author Bastian Katz (mailto: bastian.katz@hm.edu)
 */
@Repository
public interface TopicRepository extends JpaRepository<Topic, String> {

  /**
   * Findet alle Topics zu einem gegebenen Anwender.
   * 
   * @param creator Anwender
   * @return
   */
  List<Topic> findByCreatedBy(Anwender creator);

}
