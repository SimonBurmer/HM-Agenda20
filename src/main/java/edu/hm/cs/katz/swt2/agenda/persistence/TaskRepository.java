package edu.hm.cs.katz.swt2.agenda.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Repository zum Zugriff auf gespeicherte Tasks. Repostory-Interfaces erben eine unglaubliche Menge
 * hilfreicher Methoden. Weitere Methoden kann man einfach durch Benennung definierern. Spring Data
 * ergänzt die Implementierungen zur Laufzeit.
 *
 * @author Bastian Katz (mailto: bastian.katz@hm.edu)
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

  /**
   * Löscht alle Tasks zu einem Topic.
   *
   * @param topic Topic
   * @return int Anzahl der gelöschten Tasks
   */
  int deleteByTopic(Topic topic);
  
}
