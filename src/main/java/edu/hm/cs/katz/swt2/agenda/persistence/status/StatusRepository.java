package edu.hm.cs.katz.swt2.agenda.persistence.status;

import edu.hm.cs.katz.swt2.agenda.persistence.anwender.Anwender;
import edu.hm.cs.katz.swt2.agenda.persistence.task.Task;
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
   * @param anwender Anwender
   * @param task Task
   * @return Status, <code>null</code>, wenn noch kein Status existiert.
   */
  Status findByAnwenderAndTask(Anwender anwender, Task task);
}
