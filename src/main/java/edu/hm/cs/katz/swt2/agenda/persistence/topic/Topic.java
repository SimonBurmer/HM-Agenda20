package edu.hm.cs.katz.swt2.agenda.persistence.topic;

import edu.hm.cs.katz.swt2.agenda.persistence.anwender.Anwender;
import edu.hm.cs.katz.swt2.agenda.persistence.task.Task;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

@Entity
public class Topic {

  @Id
  @Column(length = 36)
  @NotNull
  @Length(min = 36, max = 36)
  private String uuid;

  @NotNull
  @Column(length = 60)
  @Length(min = 10, max = 60)
  private String title;

  @ManyToOne
  @NotNull
  private Anwender createdBy;

  @OneToMany(mappedBy = "topic")
  private Collection<Task> tasks = new ArrayList<Task>();

  @ManyToMany
  private Collection<Anwender> subscriber = new ArrayList<Anwender>();

  /**
   * JPA-kompatibler Kostruktor. Wird nur von JPA verwendet und darf private sein.
   */
  public Topic() {
    // JPA benötigt einen Default-Konstruktor!
  }

  /**
   * Konstruktor zur Erzeugung eines neuen Topics.
   * 
   * @param uuid UUID, muss eindeutig sein.
   * @param title Titel, zwischen 10 und 60 Zeichen.
   * @param createdBy Anwender, dem das Topic zugeordnet ist.
   */
  public Topic(final String uuid, final String title, final Anwender createdBy) {
    this.uuid = uuid;
    this.title = title;
    this.createdBy = createdBy;
  }

  @Override
  public String toString() {
    return "Topic " + title;
  }

  public String getUuid() {
    return uuid;
  }

  public String getTitle() {
    return title;
  }

  public Anwender getCreatedBy() {
    return createdBy;
  }

  public Collection<Task> getTasks() {
    return Collections.unmodifiableCollection(tasks);
  }

  public void addTask(Task t) {
    tasks.add(t);
  }

  public void register(Anwender anwender) {
    subscriber.add(anwender);
    anwender.addSubscription(this);
  }

  public Collection<Anwender> getSubscriber() {
    return Collections.unmodifiableCollection(subscriber);
  }

  /*
   * Generierte Methoden. Es ist sinnvoll, hier auf die Auswertung der Assoziationen zu verzichten,
   * da sonst unnötige Datenbankzugriffe erzeugt werden.
   */

  @Override
  public int hashCode() {
    return Objects.hash(title, uuid);
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
    Topic other = (Topic) obj;
    return Objects.equals(title, other.title) && Objects.equals(uuid, other.uuid);
  }



}
