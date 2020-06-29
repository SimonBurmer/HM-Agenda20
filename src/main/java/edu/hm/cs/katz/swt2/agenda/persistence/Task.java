package edu.hm.cs.katz.swt2.agenda.persistence;

import java.util.Collection;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import edu.hm.cs.katz.swt2.agenda.common.TaskTypeEnum;
import org.hibernate.validator.constraints.Length;

/**
 * Modellklasse für die Speicherung der Aufgaben. Enthält die Abbildung auf eine Datenbanktabelle in
 * Form von JPA-Annotation.
 * 
 * @author Bastian Katz (mailto: bastian.katz@hm.edu)
 */
@Entity
public class Task {

  @Id
  @NotNull
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @NotNull
  @Length(min = 8, max = 32)
  @Column(length = 32)
  private String title;

  @NotNull
  @Column(length = 200)
  @Length(min = 100, max = 200)
  private String taskShortDescription;

  @NotNull
  @Column(length = 2000)
  @Length(min = 200, max = 2000)
  private String taskLongDescription;

  @Enumerated(EnumType.STRING)
  @NotNull
  private TaskTypeEnum taskType = TaskTypeEnum.INFO;

  @NotNull
  @ManyToOne
  private Topic topic;

  @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
  private Collection<Status> status;
  
  @NotNull
  @Lob
  private byte[] image;


  /**
   * JPA-kompatibler Kostruktor. Wird nur von JPA verwendet und darf private sein.
   */
  public Task() {
    // JPA benötigt einen Default-Konstruktor!
  }

  /**
   * Konstruktor zum Erstellen eines neuen Tasks.
   * 
   * @param topic Topic, darf nicht null sein.
   * @param title Titel, darf nicht null sein.
   */
  public Task(final Topic topic, final String title, final String taskShortDescription,
      final String taskLongDescription, TaskTypeEnum taskType, byte[] image ) {
    this.topic = topic;
    topic.addTask(this);
    this.title = title;
    this.taskShortDescription = taskShortDescription;
    this.taskLongDescription = taskLongDescription;
    this.taskType = taskType;
    this.image = image;
  }

  @Override
  public String toString() {
    return "Task \"" + title + "\"";
  }

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getTaskShortDescription() {
    return taskShortDescription;
  }

  public String getTaskLongDescription() {
    return taskLongDescription;
  }

  public Topic getTopic() {
    return topic;
  }

  public void setTaskShortDescription(String taskShortDescription) {
    this.taskShortDescription = taskShortDescription;
  }

  public void setTaskLongDescription(String taskLongDescription) {
    this.taskLongDescription = taskLongDescription;
  }

  public TaskTypeEnum getTaskType() {
    return taskType;
  }

  public void setTaskType(TaskTypeEnum taskType) {
    this.taskType = taskType;
  }
  /*
   * Standard-Methoden. Es ist sinnvoll, hier auf die Auswertung der Assoziationen zu verzichten,
   * nur die Primärschlüssel zu vergleichen und insbesonderen Getter zu verwenden, um auch mit den
   * generierten Hibernate-Proxys kompatibel zu bleiben.
   */

  @Override
  public int hashCode() {
    return Objects.hash(id, topic);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Task)) {
      return false;
    }
    Task other = (Task) obj;
    return Objects.equals(getId(), other.getId());
  }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
