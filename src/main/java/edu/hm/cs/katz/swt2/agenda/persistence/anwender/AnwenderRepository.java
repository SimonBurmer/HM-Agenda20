package edu.hm.cs.katz.swt2.agenda.persistence.anwender;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository zum Zugriff auf gespeicherte Anwenderdaten.
 * 
 * @author Bastian Katz (mailto: bastian.katz@hm.edu)
 */
@Repository
public interface AnwenderRepository extends JpaRepository<Anwender, String> {

  /**
   * Ermittelt alle Anwender, die Administrator sind (oder alle anderen).
   * 
   * @param isAdministrator Flag zum Filtern
   * @return
   */
  List<Anwender> findByAdministrator(boolean isAdministrator);

}
