package edu.hm.cs.katz.swt2.agenda.initialization;

import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import edu.hm.cs.katz.swt2.agenda.SecurityHelper;
import edu.hm.cs.katz.swt2.agenda.service.AnwenderService;
import edu.hm.cs.katz.swt2.agenda.service.task.TaskService;
import edu.hm.cs.katz.swt2.agenda.service.topic.TopicService;

@Component
@Profile("demo")
public class DemoData {

  private static final String LOGIN_FINE = "fine";

  private static final String LOGIN_ERNIE = "ernie";

  private static final String LOGIN_BERT = "bert";

  private static final Logger LOG = LoggerFactory.getLogger(DemoData.class);

  @Autowired
  AnwenderService anwenderService;

  @Autowired
  TopicService topicService;
  
  @Autowired
  TaskService taskService;
  
  @PostConstruct
  public void addData() {
    SecurityHelper.escalate();
    LOG.debug("Erzeuge Demo-Daten.");
    anwenderService.legeAn(LOGIN_ERNIE, "t", false);
    anwenderService.legeAn(LOGIN_BERT, "t", false);
    anwenderService.legeAn(LOGIN_FINE, "t", false);
    String erniesKursUuid = topicService.createTopic("Ernies Backkurs", LOGIN_ERNIE);
    String finesKursUuid = topicService.createTopic("HTML für Anfänger", LOGIN_FINE);
    topicService.createTopic("CSS für Fortgeschrittene", LOGIN_FINE);
    taskService.createTask(erniesKursUuid, "Googlehupf backen");
    taskService.createTask(erniesKursUuid, "Affenmuffins backen");
    taskService.createTask(finesKursUuid, "Leeres HTML-Template erstellen");
    taskService.createTask(finesKursUuid, "Link erstellen");
    topicService.register(erniesKursUuid, LOGIN_BERT);
    topicService.register(finesKursUuid, LOGIN_BERT);
    topicService.register(finesKursUuid, LOGIN_ERNIE);
    topicService.register(erniesKursUuid, LOGIN_BERT);
    
  }

}
