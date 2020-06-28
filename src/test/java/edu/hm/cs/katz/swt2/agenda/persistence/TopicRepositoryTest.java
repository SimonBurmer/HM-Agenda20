package edu.hm.cs.katz.swt2.agenda.persistence;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class TopicRepositoryTest {

  @Autowired
  UserRepository userRepository;

  @Autowired
  TopicRepository topicRepository;

  private final String UUID_PREFIX = "12345678901234567890123456789012345";

  @Test
  public void topicRepositoryDeliversTopicsOrdered() {
    User testUser = new User("testlogin", "TestUser", "testPassword", false);
    userRepository.save(testUser);

    String shortDescription100 =
        "Test Test Test Test Test Test Test Test Test Test Test Test Test Test Test Test Test Test Test Test ";
    String longDescription200 =
        "Test Test Test Test Test Test Test Test Test Test Test Test Test Test Test Test Test Test Test Test Test Test Test Test Test Test Test Test Test Test Test Test Test Test Test Test Test Test Test Test ";

    Topic f = new Topic(UUID_PREFIX + "f", "f TopicTitle", testUser, shortDescription100,
        longDescription200);
    Topic a = new Topic(UUID_PREFIX + "a", "a TopicTitle", testUser, shortDescription100,
        longDescription200);
    Topic d = new Topic(UUID_PREFIX + "d", "d TopicTitle", testUser, shortDescription100,
        longDescription200);
    topicRepository.save(a);
    topicRepository.save(d);
    topicRepository.save(f);

    List<Topic> topicsToTest = topicRepository.findByCreatorOrderByTitleAsc(testUser);

    assertEquals(3, topicsToTest.size());
    assertEquals(a, topicsToTest.get(0));
    assertEquals(d, topicsToTest.get(1));
    assertEquals(f, topicsToTest.get(2));
  }

}
