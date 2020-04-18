package edu.hm.cs.katz.swt2.agenda.persistence.status;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import edu.hm.cs.katz.swt2.agenda.persistence.anwender.Anwender;
import edu.hm.cs.katz.swt2.agenda.persistence.task.Task;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {

  Status findByAnwenderAndTask(Anwender anwender, Task task);
}
