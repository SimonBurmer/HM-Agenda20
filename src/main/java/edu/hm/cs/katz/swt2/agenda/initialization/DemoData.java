package edu.hm.cs.katz.swt2.agenda.initialization;

import edu.hm.cs.katz.swt2.agenda.common.SecurityHelper;
import edu.hm.cs.katz.swt2.agenda.common.TaskTypeEnum;
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

  private static final String TOPIC_BACKKURS_LANG =
      "Wir schütten ein Füllhorn mit Glückshormonen & süßen Geschmackserlebnissen"
          + " für Sie aus! Schmelzen Sie dahin bei einem süßen Backkurs in München"
          + " - ein Schlaraffenland für alle Naschkatzen! Bei einem Macarons-Backkurs"
          + " lernen Sie, wie Sie die hauchzarten und bildschönen französischen"
          + " Leckerbissen perfekt formen und füllen. In einem Tortendeko-Kurs"
          + " für kreative Tortenbäcker üben Sie zusammen mit einem echten Profi"
          + "-Pattisier, wie Sie Fondant färben und damit zauberhafte Motivtorten"
          + " dekorieren. Und für alle Schokoladenfans gibt es natürlich auch "
          + "unsere verführerisch süßen Pralinenkurse. Auch das Ambiente bei "
          + "unseren Backkursen in München stimmt: Ob in der wunderschönen "
          + "Schokoladengalerie Chokoin in Schwabing, in der traditionsreichen"
          + " Backstube des Café Luitpold mitten in den Münchner Innenstadt oder"
          + " in der Keks-Backschule der Wiener Keksdesignerin und Autorin Stephanie"
          + " Juliette Rinner im Westend. Immer backen Sie in von uns mit Sorgfalt"
          + " und Liebe ausgewählten, charmanten Locations.";

  private static final String TOPIC_HTML_LANG =
      "In diesem Tutorial lernen Sie die Grundlagen von HTML kennen."
          + " Und keine Sorge, diese vier Buchstaben dürfen Sie gerne "
          + "deutsch aussprechen. Wofür diese Buchstaben im Einzelnen "
          + "stehen, verraten wir Ihnen auf jeden Fall noch. Jedenfalls"
          + " sind Sie herzlich eingeladen, mit uns eine Reise durch die"
          + " Techniken des Internets zu wagen. Zunächst: Sie brauchen "
          + "keine Internetverbindung, um mit HTML Webseiten erstellen "
          + "und testen zu können. Solche Webseiten bestehen nämlich, wie"
          + " Sie feststellen werden, aus ganz normalen Dateien, die Sie"
          + " einfach auf Ihrem Computer speichern können. Um HTML-Dateien"
          + " auf dem Bildschirm anzeigen zu können, benötigen Sie dann nu"
          + "r Ihren Browser, also das Programm, mit dem Sie gerade dieses"
          + " Tutorial lesen. Die HTML-Dateien (und auch CSS-Stylesheets"
          + " ab Kapitel 7), um die es im Folgenden hauptsächlich geht,"
          + " sind reine Textdateien. Um diese Dateien bearbeiten zu können,"
          + " reicht erstmal ein einfacher Texteditor, der bei Ihrem "
          + "Betriebssystem bereits vorinstalliert ist. Beispiele: •"
          + "   Linux: KWrite, gedit •   (Mac) OS X: TextEdit •   Windows"
          + ": Editor/Notepad. Es gibt aber eine Vielzahl von oft auch "
          + "kostenlosen Code-Editoren.";

  private static final String TOPIC_CSS_LANG =
      "Das World Wide Web Konsortium (W3C) entwickelte CSS, um"
          + " zum ursprünglichen Grundgedanken von HTML zurückzukehren:"
          + " Die Trennung der Informationen von der Präsentation. Ohne"
          + " CSS ist HTML für Inhalt, Struktur und Aussehen zuständig."
          + " Ein aufgeblähter und unübersichtlicher Code entsteht. "
          + "Durch die Nutzung von CSS, ist HTML für die Struktur eines"
          + " Dokumentes zuständig - CSS dagegen ist für das Aussehen "
          + "(Design) verantwortlich. CSS wird wie schon HTML als reiner"
          + " Text \"geschrieben\". Wir erstellen also nur eine Textdatei,"
          + " in der das Design definiert wird.";

  private static final String TOPIC_BACKKURS_KURZ =
      "Bei einem Ernies-Backkurs lernen Sie, wie Sie die hauchzarten"
          + " und bildschönen französischen Leckerbissen perfekt formen und füllen.";

  private static final String TOPIC_HTML_KURZ =
      "Was muss man eigentlich können oder wissen, um nicht nur im "
          + "Internet zu surfen, sondern auch eigene Inhalte im Internet" + " zu präsentieren?";

  private static final String TOPIC_CSS_KURZ =
      "Jetzt CSS Profi werden mit Fine. Sie möchten Webdesigner werden"
          + " oder Ihr Wissen vertiefen bzw. auffrischen ?";


  private static final String TASK_LINK_ERSTELLEN_LANG =
      "Vor der Entwicklung des World Wide Web und dessen Bestandteilen, "
          + "zu denen auch HTML gehört, war es nicht möglich, Dokumente auf "
          + "elektronischem Weg einfach, schnell und strukturiert zwischen"
          + " mehreren Personen auszutauschen und miteinander effizient zu "
          + "verknüpfen. Man benötigte neben Übertragungsprotokollen auch"
          + " eine einfach zu verstehende Textauszeichnungssprache. Genau hier"
          + " lag der Ansatzpunkt von HTML. Um Forschungsergebnisse mit anderen"
          + " Mitarbeitern der Europäischen Organisation für Kernforschung (CERN)"
          + " zu teilen und von den beiden Standorten in Frankreich und in der "
          + "Schweiz aus zugänglich zu machen, entstand 1989 am CERN ein Projekt,"
          + " welches sich mit der Lösung dieser Aufgabe beschäftigte. Am 3. November"
          + " 1992 erschien die erste Version der HTML-Spezifikation.";
  private static final String TASK_HTML_TEMPLATE_ERSTELLEN_LANG =
      "Das template-Element ermöglicht es, Template-Vorlagen als "
          + "Inhaltsfragmente im HTML-Dokument anzugeben, die zwar auf ihre "
          + "Richtigkeit geparst, aber nicht gerendert werden. Erst wenn sie"
          + " benötigt werden, können sie mit appendChild() in den Elementenbau"
          + "m eingehängt werden. Sie sind eine gute Alternative zum zeitaufwendig"
          + "en Nachladen von Inhalten mit Ajax oder dem dynamischen Erzeugen mit"
          + " createElement, das bei komplexeren HTML-Strukturen schnell unbequem"
          + " und unübersichtlich wird. Eric Bidelman stellt in diesem Artikel die"
          + " Vorgängervorgehensweisen „Offscreen DOM“ und „Overloading Script“ "
          + "und ihre Nachteile vor.";
  private static final String TASK_GOOGLEHUPF_BACKEN_LANG =
      "Der Kuchenteig besteht je nach Rezept aus Mehl, Zucker, Bindemittel"
          + " (z. B. Ei) sowie Fett (Butter oder Margarine), einer Flüssigkeit"
          + " (Milch, Wasser oder Fruchtsaft), Aromen (z. B. Backaroma) und "
          + "einem Triebmittel (Backpulver oder Hefe), die miteinander vermengt "
          + "werden. Wichtige Teigarten sind Hefeteig, Mürbeteig (Knetteig) "
          + "und Rührteig. Bäckereien und Konditoreien bieten Kuchen stückweise"
          + " oder als Backblecheinheiten an. In Supermärkten gibt es von "
          + "Großbäckereien hergestellte Kuchen. Das Backen von Kuchen ist "
          + "vor allem in Europa und Nordamerika traditionell verbreitet, "
          + "während es auf anderen Kontinenten nur eine untergeordnete Rolle"
          + " spielt; in Asien sind fast ausschließlich Reiskuchen bekannt. "
          + "In China haben außerdem die Mondkuchen eine besondere Bedeutung.";
  private static final String TASK_AFFENMUFFINS_BACKEN_LANG =
      "Den Backofen auf 175 °C vorheizen. Das Muffinblech mit Papierförmchen"
          + " auslegen. In einer Schüssel das Mehl mit dem Backpulver, Natron,"
          + " Salz und Schokoladentropfen vermischen. Die Eier mit dem Zucker "
          + "schaumig schlagen. Die Butter in einem kleinen Topf schmelzen. Die "
          + "Bananen schälen und mit der Gabel zerdrücken. Butter, Joghurt und"
          + " Bananen unter das Eigemisch rühren. Das Mehlgemisch vorsichtig "
          + "unterrühren, bis ein zähflüssiger Teig entstanden ist. Den Teig in "
          + "die Förmchen füllen und im Backofen etwa 25 Minuten backen. Die Muffins"
          + " auskühlen lassen und anschließend mit der im Wasserbad erhitzten "
          + "Kuvertüre bestreichen. Ein Stäbchen in die Kuvertüre tauchen und auf"
          + " 12 Oblaten einen Mund, auf 12 weitere Oblaten die Augen zeichnen"
          + ". 12 Oblaten halbieren. In die Muffins links und rechts je einen "
          + "kleinen Schnitt machen und die halben Oblaten einstecken. Erst "
          + "die Augen, dann den Mund in die noch warme Kuvertüre auf die " + "Muffins kleben.";
  private static final String TASK_LINK_ERSTELLEN_KURZ =
      "Die Hypertext Markup Language ist eine textbasierte "
          + "Auszeichnungssprache zur Strukturierung elektronischer Dokumente"
          + " wie Texte mit Hyperlinks, Bildern und anderen Inhalten.";
  private static final String TASK_HTML_TEMPLATE_ERSTELLEN_KURZ =
      "Das template-Element ermöglicht es, Template-Vorlagen als Inhaltsfragmente"
          + " im HTML-Dokument anzugeben, die zwar auf ihre Richtigkeit geparst,"
          + " aber nicht gerendert werden.";
  private static final String TASK_GOOGLEHUPF_BACKEN_KURZ =
      "Der Kuchen gehört zu den feinen Backwaren. Es handelt sich um ein "
          + "zumeist süßes Backwerk. Man unterscheidet vor allem nach der Art"
          + " der Herstellung Blechkuchen.";
  private static final String TASK_AFFENMUFFINS_BACKEN_KURZ =
      "Diese Muffins passen perfekt zu einem tollen Kindergeburtstag. Wie"
          + " man sie zubereitet und anschließend noch in lustige Affen "
          + "verwandelt, verrät dieses Rezept";


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
    anwenderService.legeAn(LOGIN_ERNIE, "Ernie", "Ernie123*", false);
    anwenderService.legeAn(LOGIN_BERT, "Bert", "Bert123*", false);

    // Topic: HTML für Anfänger
    String htmlKursUuid =
        topicService.createTopic("HTML für Anfänger", LOGIN_FINE, TOPIC_HTML_KURZ, TOPIC_HTML_LANG);
    taskService.createTask(htmlKursUuid, "Leeres HTML-Template erstellen", LOGIN_FINE,
        TASK_HTML_TEMPLATE_ERSTELLEN_KURZ, TASK_HTML_TEMPLATE_ERSTELLEN_LANG, TaskTypeEnum.INFO);
    topicService.subscribe(htmlKursUuid, LOGIN_ERNIE);
    topicService.subscribe(htmlKursUuid, LOGIN_BERT);
    Long linkErstellenTask = taskService.createTask(htmlKursUuid, "Link erstellen", LOGIN_FINE,
        TASK_LINK_ERSTELLEN_KURZ, TASK_LINK_ERSTELLEN_LANG, TaskTypeEnum.DEFAULT);
    taskService.checkTask(linkErstellenTask, LOGIN_ERNIE);

    // Topic: CSS für Fortgeschrittene
    String cssKursUuid = topicService.createTopic("CSS für Fortgeschrittene", LOGIN_FINE,
        TOPIC_CSS_KURZ, TOPIC_CSS_LANG);
    topicService.subscribe(cssKursUuid, LOGIN_ERNIE);
    topicService.subscribe(cssKursUuid, LOGIN_BERT);

    // Topic: Ernies Backkurs
    String erniesKursUuid = topicService.createTopic("Ernies Backkurs", LOGIN_ERNIE,
        TOPIC_BACKKURS_KURZ, TOPIC_BACKKURS_LANG);
    taskService.createTask(erniesKursUuid, "Googlehupf backen", LOGIN_ERNIE,
        TASK_GOOGLEHUPF_BACKEN_KURZ, TASK_GOOGLEHUPF_BACKEN_LANG, TaskTypeEnum.MANDATORY);
    Long affenMuffinTask =
        taskService.createTask(erniesKursUuid, "Affenmuffins backen", LOGIN_ERNIE,
            TASK_AFFENMUFFINS_BACKEN_KURZ, TASK_AFFENMUFFINS_BACKEN_LANG, TaskTypeEnum.OPTIONAL);
    topicService.subscribe(erniesKursUuid, LOGIN_BERT);
    taskService.checkTask(affenMuffinTask, LOGIN_BERT);
    topicService.subscribe(erniesKursUuid, LOGIN_FINE);
  }

}
