package edu.hm.cs.katz.swt2.agenda.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository zum Zugriff auf gespeicherte Statusinformationen. Repostory-Interfaces erben eine
 * unglaubliche Menge hilfreicher Methoden. Weitere Methoden kann man einfach durch Benennung
 * definierern. Spring Data ergänzt die Implementierungen zur Laufzeit.
 * 
 * @author Bastian Katz (mailto: bastian.katz@hm.edu)
 */
@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {

  /**
   * Findet den Status für einen gegebenen Task und einen gegebenen Anwender.
   * 
   * @param user Anwender
   * @param task Task
   * @return Status, <code>null</code>, wenn noch kein Status existiert.
   */
  Status findByUserAndTask(User user, Task task);
  
  /**
   * Findet alle Status für einen gegebenen Task.
   * 
   * @param task Task
   * @return Status, <code>null</code>, wenn noch kein Status existiert.
   */
  List<Status> findByTask(Task task);
  
}
