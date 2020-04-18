package edu.hm.cs.katz.swt2.agenda.initialization;

import edu.hm.cs.katz.swt2.agenda.SecurityHelper;
import edu.hm.cs.katz.swt2.agenda.service.task.TaskService;
import edu.hm.cs.katz.swt2.agenda.service.topic.TopicService;
import edu.hm.cs.katz.swt2.agenda.service.user.UserService;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Initialisierung von Demo-Daten. Diese Komponente erstellt beim Systemstart Anwender, Topics,
 * Abonnements usw., damit man die Anwendung mit allen Features vorführen kann.
 * 
 * @author Bastian Katz (mailto: bastian.katz@hm.edu)
 */
@Component
@Profile("demo")
public class DemoData {

  private static final String LOGIN_FINE = "fine";

  private static final String LOGIN_ERNIE = "ernie";

  private static final String LOGIN_BERT = "bert";

  private static final Logger LOG = LoggerFactory.getLogger(DemoData.class);

  @Autowired
  UserService anwenderService;

  @Autowired
  TopicService topicService;

  @Autowired
  TaskService taskService;

  /**
   * Erstellt die Demo-Daten.
   */
  @PostConstruct
  public void addData() {
    SecurityHelper.escalate();
    LOG.debug("Erzeuge Demo-Daten.");
    anwenderService.legeAn(LOGIN_ERNIE, "t", false);
    anwenderService.legeAn(LOGIN_BERT, "t", false);
    anwenderService.legeAn(LOGIN_FINE, "t", false);
    String erniesKursUuid = topicService.createTopic("Ernies Backkurs", LOGIN_ERNIE);
    taskService.createTask(erniesKursUuid, "Googlehupf backen");
    taskService.createTask(erniesKursUuid, "Affenmuffins backen");
    topicService.register(erniesKursUuid, LOGIN_BERT);
    topicService.register(erniesKursUuid, LOGIN_BERT);
    String finesKursUuid = topicService.createTopic("HTML für Anfänger", LOGIN_FINE);
    taskService.createTask(finesKursUuid, "Leeres HTML-Template erstellen");
    taskService.createTask(finesKursUuid, "Link erstellen");
    topicService.register(finesKursUuid, LOGIN_BERT);
    topicService.register(finesKursUuid, LOGIN_ERNIE);
    topicService.createTopic("CSS für Fortgeschrittene", LOGIN_FINE);

  }

}
