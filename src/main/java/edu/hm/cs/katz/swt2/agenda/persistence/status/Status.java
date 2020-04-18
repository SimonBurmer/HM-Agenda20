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

@Entity
public class Status {

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

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Anwender getAnwender() {
    return anwender;
  }

  public void setAnwender(Anwender anwender) {
    this.anwender = anwender;
  }

  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public Task getTask() {
    return task;
  }

  public void setTask(Task task) {
    this.task = task;
  }

}
