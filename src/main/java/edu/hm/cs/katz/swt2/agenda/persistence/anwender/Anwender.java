package edu.hm.cs.katz.swt2.agenda.persistence.anwender;

import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import edu.hm.cs.katz.swt2.agenda.persistence.topic.Topic;

@Entity
public class Anwender {

  @Id
  @NotNull
  private String login;

  @NotNull
  @Length(min = 6)
  private String password;

  private boolean administrator;

  @ManyToMany(mappedBy = "subscriber")
  private Collection<Topic> subscriptions = new ArrayList<>();

  public Anwender(@NotNull String login, @NotNull String password, boolean administrator) {
    super();
    this.login = login;
    this.password = password;
    this.administrator = administrator;
  }

  public Anwender() {
    // Do not remove!
  }

  public boolean isAdministrator() {
    return administrator;
  }

  public void setAdministrator(boolean administrator) {
    this.administrator = administrator;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Collection<Topic> getSubscriptions() {
    return subscriptions;
  }

  public void setSubscriptions(Collection<Topic> subscriptions) {
    this.subscriptions = subscriptions;
  }

  public void addSubscription(Topic t) {
    subscriptions.add(t);
  }

}
