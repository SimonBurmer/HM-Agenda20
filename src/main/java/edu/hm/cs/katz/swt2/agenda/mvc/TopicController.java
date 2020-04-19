package edu.hm.cs.katz.swt2.agenda.mvc;

import edu.hm.cs.katz.swt2.agenda.service.TopicService;
import edu.hm.cs.katz.swt2.agenda.service.dto.ManagedTopicDto;
import edu.hm.cs.katz.swt2.agenda.service.dto.TopicDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


/**
 * Controller-Klasse für alle Interaktionen, die die Anzeige und Verwaltung von Topics betrifft.
 * Controller reagieren auf Aufrufe von URLs. Sie benennen ein View-Template (Thymeleaf-Vorlage) und
 * stellen Daten zusammen, die darin dargestellt werden. Dafür verwenden Sie Methoden der
 * Service-Schicht.
 * 
 * @author Bastian Katz (mailto: bastian.katz@hm.edu)
 */
@Controller
public class TopicController extends AbstractController {

  @Autowired
  private TopicService topicService;

  /**
   * Erstellt die Übersicht über alle Topics des Anwenders, d.h. selbst erzeugte und abonnierte.
   */
  @GetMapping("/topics")
  public String getTopicListView(Model model, Authentication auth) {
    model.addAttribute("managedTopics", topicService.getManagedTopics(auth.getName()));
    model.addAttribute("topics", topicService.getSubscriptions(auth.getName()));
    return "topic-listview";
  }

  /**
   * Erstellt das Formular zum Erstellen eines Topics.
   */
  @GetMapping("/topics/create")
  public String getTopicCreationView(Model model, Authentication auth) {
    model.addAttribute("newTopic", new TopicDto(null, null, ""));
    return "topic-creation";
  }

  /**
   * Nimmt den Formularinhalt vom Formular zum Erstellen eines Topics entgegen und legt einen
   * entsprechendes Topic an. Kommt es dabei zu einer Exception, wird das Erzeugungsformular wieder
   * angezeigt und eine Fehlermeldung eingeblendet. Andernfalls wird auf die Übersicht der Topics
   * weitergeleitet und das Anlegen in einer Einblendung bestätigt.
   */
  @PostMapping("/topics")
  public String handleTopicCreation(Model model, Authentication auth,
      @ModelAttribute("newTopic") TopicDto topic, RedirectAttributes redirectAttributes) {
    try {
      topicService.createTopic(topic.getTitle(), auth.getName());
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", e.getMessage());
      return "redirect:/topics/create";
    }
    redirectAttributes.addFlashAttribute("success", "Topic " + topic.getTitle() + " angelegt.");
    return "redirect:/topics";
  }

  /**
   * Erzeugt Anzeige eines Topics mit Informationen für den Ersteller.
   */
  @GetMapping("/topics/{uuid}/manage")
  public String createTopicManagementView(Model model, @PathVariable("uuid") String uuid) {
    ManagedTopicDto topic = topicService.getManagedTopic(uuid);
    model.addAttribute("topic", topic);
    model.addAttribute("tasks", topicService.getManagedTasks(uuid));
    return "topic-management";
  }

  /**
   * Erzeugt Anzeige für die Nachfrage beim Abonnieren eines Topics.
   */
  @GetMapping("/topics/{uuid}/register")
  public String getTaskRegistrationView(Model model, Authentication auth,
      @PathVariable("uuid") String uuid) {
    TopicDto topic = topicService.getTopic(uuid, auth.getName());
    model.addAttribute("topic", topic);
    return "topic-registration";
  }

  /**
   * Nimmt das Abonnement (d.h. die Bestätigung auf die Nachfrage) entgegen und erstellt ein
   * Abonnement.
   */
  @PostMapping("/topics/{uuid}/register")
  public String handleTaskRegistration(Model model, Authentication auth,
      @PathVariable("uuid") String uuid) {
    topicService.register(uuid, auth.getName());
    return "redirect:/topics/" + uuid;
  }

  /**
   * Erstellt Übersicht eines Topics für einen Abonennten.
   */
  @GetMapping("/topics/{uuid}")
  public String createTopicView(Model model, Authentication auth,
      @PathVariable("uuid") String uuid) {
    TopicDto topic = topicService.getTopic(uuid, auth.getName());
    model.addAttribute("topic", topic);
    model.addAttribute("tasks", topicService.getTasks(uuid, auth.getName()));
    return "topic";
  }
}
