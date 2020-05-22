package edu.hm.cs.katz.swt2.agenda.persistence;

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
  private User creator;

  @NotNull
  @Column(length = 200)
  @Length(min = 100, max = 200)
  private String shortDescription;

  @NotNull
  @Column(length = 2000)
  @Length(min = 200, max = 2000)
  private String longDescription;

  @OneToMany(mappedBy = "topic")
  private Collection<Task> tasks = new ArrayList<Task>();

  @ManyToMany
  private Collection<User> subscriber = new ArrayList<User>();

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
  public Topic(final String uuid, final String title, final User createdBy,
      final String shortDescription, final String longDescription) {
    this.uuid = uuid;
    this.title = title;
    this.creator = createdBy;
    this.shortDescription = shortDescription;
    this.longDescription = longDescription;
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

  public User getCreator() {
    return creator;
  }

  public String getShortDescription() {
    return shortDescription;
  }

  public String getLongDescription() {
    return longDescription;
  }

  public Collection<Task> getTasks() {
    return Collections.unmodifiableCollection(tasks);
  }

  public void addTask(Task t) {
    tasks.add(t);
  }

  public void register(User anwender) {
    subscriber.add(anwender);
    anwender.addSubscription(this);
  }

  public Collection<User> getSubscriber() {
    return Collections.unmodifiableCollection(subscriber);
  }

  /*
   * Standard-Methoden. Es ist sinnvoll, hier auf die Auswertung der Assoziationen zu verzichten,
   * nur die Primärschlüssel zu vergleichen und insbesonderen Getter zu verwenden, um auch mit den
   * generierten Hibernate-Proxys kompatibel zu bleiben.
   */

  @Override
  public int hashCode() {
    return Objects.hash(uuid);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Topic)) {
      return false;
    }
    Topic other = (Topic) obj;
    return Objects.equals(getUuid(), other.getUuid());
  }

}
