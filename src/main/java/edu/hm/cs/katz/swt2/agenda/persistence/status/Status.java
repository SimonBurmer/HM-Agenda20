package edu.hm.cs.katz.swt2.agenda.persistence.status;

import edu.hm.cs.katz.swt2.agenda.StatusEnum;
import edu.hm.cs.katz.swt2.agenda.persistence.anwender.Anwender;
import edu.hm.cs.katz.swt2.agenda.persistence.task.Task;
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

  public Status() {
    // JPA benötigt einen Default-Konstruktor!
  }

  public Status(final Task task, final Anwender anwender) {
    this.task = task;
    this.anwender = anwender;
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
}
