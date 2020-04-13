package edu.hm.cs.katz.swt2.agenda.persistence.anwender;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnwenderRepository extends JpaRepository<Anwender, String>{

	List<Anwender> findByAdministrator(boolean isAdministrator);

}
