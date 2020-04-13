package edu.hm.cs.katz.swt2.agenda.persistence.topic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import edu.hm.cs.katz.swt2.agenda.persistence.anwender.Anwender;
import edu.hm.cs.katz.swt2.agenda.persistence.task.Task;

@Entity
public class Topic {

  @Id
  @Column(length = 36)
  @NotNull
  private String uuid;

  @NotNull
  @Column(length = 60)
  @Length(min = 10, max = 60)
  private String title;

  @ManyToOne
  private Anwender createdBy;

  @OneToMany(mappedBy="topic")
  private Collection<Task> tasks = new ArrayList<Task>();

  @ManyToMany
  private Collection<Anwender> subscriber = new ArrayList<Anwender>();
  
  public Topic() {
    
  }
  
  public Topic(String uuid, String title, Anwender createdBy) {
    this.uuid = uuid;
    this.title = title;
    this.createdBy = createdBy;
  }


  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Anwender getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(Anwender createdBy) {
    this.createdBy = createdBy;
  }

  public Collection<Task> getTasks() {
    return Collections.unmodifiableCollection(tasks) ;
  }

  @SuppressWarnings("unused")
  private void setTasks(Collection<Task> tasks) {
    this.tasks = tasks;
  }

  public void addTask(Task t) {
    tasks.add(t);
  }

  public void register(Anwender anwender) {
    subscriber.add(anwender);
    anwender.addSubscription(this);
  }

  public Collection<Anwender> getSubscriber() {
    return subscriber;
  }

  public void setSubscriber(Collection<Anwender> subscriber) {
    this.subscriber = subscriber;
  }


}
