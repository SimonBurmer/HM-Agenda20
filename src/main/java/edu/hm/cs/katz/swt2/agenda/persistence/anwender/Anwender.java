package edu.hm.cs.katz.swt2.agenda.persistence.anwender;

import edu.hm.cs.katz.swt2.agenda.persistence.status.Status;
import edu.hm.cs.katz.swt2.agenda.persistence.topic.Topic;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

/**
 * Modellklasse für die Speicherung von Anwenderdaten. Enthält die Abbildung auf eine
 * Datenbanktabelle in Form von JPA-Annotation.
 * 
 * @author Bastian Katz (mailto: bastian.katz@hm.edu)
 */
@Entity
public class Anwender {

  @Id
  @NotNull
  @Length(min = 4, max = 32)
  @Column(length = 32)
  private String login;

  @NotNull
  @Length(min = 7, max = 32) // lenght includes "{noop}"
  @Column(length = 32)
  private String password;

  @Column(name = "ADMIN")
  private boolean administrator;

  @ManyToMany(mappedBy = "subscriber")
  private Collection<Topic> subscriptions = new ArrayList<>();

  @OneToMany(mappedBy = "anwender")
  private Collection<Status> status = new HashSet<Status>();

  /**
   * JPA-kompatibler Kostruktor. Wird nur von JPA verwendet und darf private sein.
   */
  public Anwender() {
    // JPA benötigt einen Default-Konstruktor!
  }

  /**
   * Konstruktor zum Initialisieren eines neuen Anwenders.
   * 
   * @param login login, mindestens 4 Zeichen lang
   * @param password Passwort inklusive Hash "{noop}"
   * @param administrator Flag (true für Administratorrechte)
   */
  public Anwender(String login, String password, boolean administrator) {
    super();
    this.login = login;
    this.password = password;
    this.administrator = administrator;
  }

  @Override
  public String toString() {
    return login + (administrator ? " (admin)" : "");
  }

  public void addSubscription(Topic topic) {
    subscriptions.add(topic);
  }

  public String getLogin() {
    return login;
  }

  public String getPassword() {
    return password;
  }

  public boolean isAdministrator() {
    return administrator;
  }

  public Collection<Topic> getSubscriptions() {
    return subscriptions;
  }

  public Collection<Status> getStatus() {
    return status;
  }

  /*
   * Generierte Methoden. Es ist sinnvoll, hier auf die Auswertung der Assoziationen zu verzichten,
   * da sonst unnötige Datenbankzugriffe erzeugt werden.
   */

  @Override
  public int hashCode() {
    return Objects.hash(administrator, login, password);
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
    Anwender other = (Anwender) obj;
    return administrator == other.administrator && Objects.equals(login, other.login)
        && Objects.equals(password, other.password);
  }



}
