package edu.hm.cs.katz.swt2.agenda;

import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class UuidProviderImpl {

  public String getRandomUuid() {
    return UUID.randomUUID().toString();
  }
}
