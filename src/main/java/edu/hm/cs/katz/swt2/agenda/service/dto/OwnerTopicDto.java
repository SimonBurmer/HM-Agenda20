package edu.hm.cs.katz.swt2.agenda.service.dto;

import java.util.Collection;
import edu.hm.cs.katz.swt2.agenda.persistence.User;

/**
 * Transferobjekt für Topics mit Metadaten, die nur für Verwalter eines Topics (d.h. Eigentümer des
 * Topics) sichtbar sind. Transferobjekte sind Schnittstellenobjekte der Geschäftslogik; Sie sind
 * nicht Teil des Modells, so dass Änderungen an den Transferobjekten die Überprüfungen der
 * Geschäftslogik nicht umgehen können.
 * 
 * @see SubscriberTopicDto
 * 
 * @author Bastian Katz (mailto: bastian.katz@hm.edu)
 */
public class OwnerTopicDto extends SubscriberTopicDto {

  private Collection<User> subscriber;

  private int amountSubscriber;

  public OwnerTopicDto(String uuid, UserDisplayDto user, String title, String shortDescription,
      String longDescription, Collection<User> subscriber) {
    super(uuid, user, title, shortDescription, longDescription);
    this.subscriber = subscriber;
  }

  public void setSubscriber(Collection<User> subscriber) {
    this.subscriber = subscriber;
  }

  public int getAmountSubscriber() {
    return amountSubscriber;
  }

  public void setAmountSubscriber(int amountSubscriber) {
    this.amountSubscriber = amountSubscriber;
  }

  public String getKey() {
    return getUuid().substring(28);
  }

  public Collection<User> getSubscriber() {
    return subscriber;
  }

}
