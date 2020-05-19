package edu.hm.cs.katz.swt2.agenda.initialization;

import edu.hm.cs.katz.swt2.agenda.common.SecurityHelper;
import edu.hm.cs.katz.swt2.agenda.service.TaskService;
import edu.hm.cs.katz.swt2.agenda.service.TopicService;
import edu.hm.cs.katz.swt2.agenda.service.UserService;
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
  @SuppressWarnings("unused")
  public void addData() {
    SecurityHelper.escalate(); // admin rights
    LOG.debug("Erzeuge Demo-Daten.");

    anwenderService.legeAn(LOGIN_FINE, "Fine", "Fine123*", false);
    anwenderService.legeAn(LOGIN_ERNIE,"Ernie", "Ernie123*", false);
    anwenderService.legeAn(LOGIN_BERT,"Bert", "Bert123*", false);

    String htmlKursUuid = topicService.createTopic("HTML für Anfänger", LOGIN_FINE,
        "Was muss man eigentlich können oder wissen, um nicht nur im Internet zu surfen, sondern auch eigene Inhalte im Internet zu präsentieren?",
        "In diesem Tutorial lernen Sie die Grundlagen von HTML kennen. Und keine Sorge, diese vier Buchstaben dürfen Sie gerne deutsch aussprechen. Wofür diese Buchstaben im Einzelnen stehen, verraten wir Ihnen auf jeden Fall noch. Jedenfalls sind Sie herzlich eingeladen, mit uns eine Reise durch die Techniken des Internets zu wagen.\n"
            + "\n"
            + "Zunächst: Sie brauchen keine Internetverbindung, um mit HTML Webseiten erstellen und testen zu können. Solche Webseiten bestehen nämlich, wie Sie feststellen werden, aus ganz normalen Dateien, die Sie einfach auf Ihrem Computer speichern können.\n"
            + "Um HTML-Dateien auf dem Bildschirm anzeigen zu können, benötigen Sie dann nur Ihren Browser, also das Programm, mit dem Sie gerade dieses Tutorial lesen. Die HTML-Dateien (und auch CSS-Stylesheets ab Kapitel 7), um die es im Folgenden hauptsächlich geht, sind reine Textdateien. Um diese Dateien bearbeiten zu können, reicht erstmal ein einfacher Texteditor, der bei Ihrem Betriebssystem bereits vorinstalliert ist.\n"
            + "Beispiele:\n" + "•   Linux: KWrite, gedit\n" + "•   (Mac) OS X: TextEdit\n"
            + "•   Windows: Editor/Notepad\n"
            + "Es gibt aber eine Vielzahl von oft auch kostenlosen Code-Editoren.\n" + "");
    
    topicService.subscribe(htmlKursUuid, LOGIN_ERNIE);
    topicService.subscribe(htmlKursUuid, LOGIN_BERT);
    Long linkErstellenTask = taskService.createTask(htmlKursUuid, "Link erstellen", LOGIN_FINE);
    taskService.checkTask(linkErstellenTask, LOGIN_ERNIE);
    taskService.createTask(htmlKursUuid, "Leeres HTML-Template erstellen", LOGIN_FINE);

    String cssKursUuid = topicService.createTopic("CSS für Fortgeschrittene", LOGIN_FINE,
        "Was muss man eigentlich können oder wissen, um nicht nur im Internet zu surfen, sondern auch eigene Inhalte im Internet zu präsentieren?",
        "In diesem Tutorial lernen Sie die Grundlagen von HTML kennen. Und keine Sorge, diese vier Buchstaben dürfen Sie gerne deutsch aussprechen. Wofür diese Buchstaben im Einzelnen stehen, verraten wir Ihnen auf jeden Fall noch. Jedenfalls sind Sie herzlich eingeladen, mit uns eine Reise durch die Techniken des Internets zu wagen.\n"
            + "\n"
            + "Zunächst: Sie brauchen keine Internetverbindung, um mit HTML Webseiten erstellen und testen zu können. Solche Webseiten bestehen nämlich, wie Sie feststellen werden, aus ganz normalen Dateien, die Sie einfach auf Ihrem Computer speichern können.\n"
            + "Um HTML-Dateien auf dem Bildschirm anzeigen zu können, benötigen Sie dann nur Ihren Browser, also das Programm, mit dem Sie gerade dieses Tutorial lesen. Die HTML-Dateien (und auch CSS-Stylesheets ab Kapitel 7), um die es im Folgenden hauptsächlich geht, sind reine Textdateien. Um diese Dateien bearbeiten zu können, reicht erstmal ein einfacher Texteditor, der bei Ihrem Betriebssystem bereits vorinstalliert ist.\n"
            + "Beispiele:\n" + "•   Linux: KWrite, gedit\n" + "•   (Mac) OS X: TextEdit\n"
            + "•   Windows: Editor/Notepad\n"
            + "Es gibt aber eine Vielzahl von oft auch kostenlosen Code-Editoren.\n" + "");
    String erniesKursUuid = topicService.createTopic("Ernies Backkurs", LOGIN_ERNIE,
        "Was muss man eigentlich können oder wissen, um nicht nur im Internet zu surfen, sondern auch eigene Inhalte im Internet zu präsentieren?",
        "In diesem Tutorial lernen Sie die Grundlagen von HTML kennen. Und keine Sorge, diese vier Buchstaben dürfen Sie gerne deutsch aussprechen. Wofür diese Buchstaben im Einzelnen stehen, verraten wir Ihnen auf jeden Fall noch. Jedenfalls sind Sie herzlich eingeladen, mit uns eine Reise durch die Techniken des Internets zu wagen.\n"
            + "\n"
            + "Zunächst: Sie brauchen keine Internetverbindung, um mit HTML Webseiten erstellen und testen zu können. Solche Webseiten bestehen nämlich, wie Sie feststellen werden, aus ganz normalen Dateien, die Sie einfach auf Ihrem Computer speichern können.\n"
            + "Um HTML-Dateien auf dem Bildschirm anzeigen zu können, benötigen Sie dann nur Ihren Browser, also das Programm, mit dem Sie gerade dieses Tutorial lesen. Die HTML-Dateien (und auch CSS-Stylesheets ab Kapitel 7), um die es im Folgenden hauptsächlich geht, sind reine Textdateien. Um diese Dateien bearbeiten zu können, reicht erstmal ein einfacher Texteditor, der bei Ihrem Betriebssystem bereits vorinstalliert ist.\n"
            + "Beispiele:\n" + "•   Linux: KWrite, gedit\n" + "•   (Mac) OS X: TextEdit\n"
            + "•   Windows: Editor/Notepad\n"
            + "Es gibt aber eine Vielzahl von oft auch kostenlosen Code-Editoren.\n" + "");
    taskService.createTask(erniesKursUuid, "Googlehupf backen", LOGIN_ERNIE);
    Long affenMuffinTask =
        taskService.createTask(erniesKursUuid, "Affenmuffins backen", LOGIN_ERNIE);
    topicService.subscribe(erniesKursUuid, LOGIN_BERT);
    taskService.checkTask(affenMuffinTask, LOGIN_BERT);
  }

}
