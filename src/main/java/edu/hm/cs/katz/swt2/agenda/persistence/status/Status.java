package edu.hm.cs.katz.swt2.agenda.persistence.status;

import edu.hm.cs.katz.swt2.agenda.StatusEnum;
import edu.hm.cs.katz.swt2.agenda.persistence.anwender.Anwender;
import edu.hm.cs.katz.swt2.agenda.persistence.task.Task;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Modellklasse für die Speicherung der Beziehung zwischen Anwender und einem konkreten Task.
 * Enthält die Abbildung auf eine Datenbanktabelle in Form von JPA-Annotation.
 * 
 * @author Bastian Katz (mailto: bastian.katz@hm.edu)
 */
@Entity
public class Status {

  // Die Verwendung einer generierten ID ist deutlich einfacher zu verstehen als die
  // sauberere Lösung, aus Anwender und Task einen zusammengesetzten Schlüssel zu erstellen.
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @ManyToOne
  private Anwender anwender;

  @ManyToOne
  private Task task;

  @Enumerated(EnumType.STRING)
  private StatusEnum status = StatusEnum.NEU;

  /**
   * JPA-kompatibler Kostruktor. Wird nur von JPA verwendet und darf private sein.
   */
  public Status() {
    // JPA benötigt einen Default-Konstruktor!
  }

  /**
   * Konstruktor zum Erstellen eines neuen Status.
   * 
   * @param task Task, darf nicht <code>null</code> sein
   * @param anwender Anwender, darf nicht <code>null</code> sein
   */
  public Status(final Task task, final Anwender anwender) {
    this.task = task;
    this.anwender = anwender;
  }

  @Override
  public String toString() {
    return "Status " + status;
  }

  public Anwender getAnwender() {
    return anwender;
  }

  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(final StatusEnum status) {
    this.status = status;
  }

  public Task getTask() {
    return task;
  }

  /*
   * Generierte Methoden. Es ist sinnvoll, hier auf die Auswertung der Assoziationen zu verzichten,
   * da sonst unnötige Datenbankzugriffe erzeugt werden.
   */

  @Override
  public int hashCode() {
    return Objects.hash(id, status);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Status other = (Status) obj;
    return Objects.equals(id, other.id) && status == other.status;
  }



}
