package edu.hm.cs.katz.swt2.agenda.persistence.task;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import edu.hm.cs.katz.swt2.agenda.persistence.topic.Topic;

@Entity
public class Task {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;
  
  private String title;
  
  @ManyToOne
  private Topic topic; 
  
  public Task() {
    
  }
  
  public Task(Topic topic, String title) {
    this.topic = topic;
    topic.addTask(this);
    this.title = title;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Topic getTopic() {
    return topic;
  }

  public void setTopic(Topic topic) {
    this.topic = topic;
  }


}
