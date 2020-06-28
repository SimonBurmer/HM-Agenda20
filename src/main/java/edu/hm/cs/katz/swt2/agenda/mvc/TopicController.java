package edu.hm.cs.katz.swt2.agenda.mvc;

import edu.hm.cs.katz.swt2.agenda.persistence.Topic;
import edu.hm.cs.katz.swt2.agenda.persistence.User;
import edu.hm.cs.katz.swt2.agenda.service.TaskService;
import edu.hm.cs.katz.swt2.agenda.service.TopicService;
import edu.hm.cs.katz.swt2.agenda.service.TopicServiceImpl;
import edu.hm.cs.katz.swt2.agenda.service.dto.OwnerTopicDto;
import edu.hm.cs.katz.swt2.agenda.service.dto.SubscriberTopicDto;
import net.bytebuddy.agent.builder.AgentBuilder.Listener;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
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

  @Autowired
  private TaskService taskService;

  /**
   * Erstellt die Übersicht über alle Topics des Anwenders, d.h. selbst erzeugte und abonnierte.
   */
  @GetMapping("/topics")
  public String getTopicListView(Model model, Authentication auth,
      @RequestParam(name = "search", required = false, defaultValue = "") String search) {
    model.addAttribute("managedTopics", topicService.getManagedTopics(auth.getName(), search));
    model.addAttribute("search", new Search());
    model.addAttribute("topics", topicService.getSubscriptions(auth.getName(), search));
    model.addAttribute("registration", new Registration());
    return "topic-listview";
  }

  /**
   * Erstellt das Formular zum Erstellen eines Topics.
   */
  @GetMapping("/topics/create")
  public String getTopicCreationView(Model model, Authentication auth) {
    model.addAttribute("newTopic", new SubscriberTopicDto(null, null, "", "", ""));
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
      @ModelAttribute("newTopic") SubscriberTopicDto topic, RedirectAttributes redirectAttributes) {
    try {
      topicService.createTopic(topic.getTitle(), auth.getName(), topic.getShortDescription(),
          topic.getLongDescription());
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
  public String createTopicManagementView(Model model, Authentication auth,
      @PathVariable("uuid") String uuid) {
    OwnerTopicDto topic = topicService.getManagedTopic(uuid, auth.getName());
    model.addAttribute("topic", topic);
    model.addAttribute("tasks", taskService.getManagedTasks(uuid, auth.getName()));
    return "topic-management";
  }

  /**
   * Erzeugt Anzeige für die Nachfrage beim Abonnieren eines Topics.
   */
  @GetMapping("/topics/{uuid}/register")
  public String getTaskRegistrationView(Model model, Authentication auth,
      @PathVariable("uuid") String uuid) {
    SubscriberTopicDto topic = topicService.getTopic(uuid, auth.getName());
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
    topicService.subscribe(uuid, auth.getName());
    return "redirect:/topics/" + uuid;
  }

  /**
   * Erzeugt die Anzeige für die Nachfrage beim De-Abonnieren eines Topics.
   */
  @GetMapping("/topics/{uuid}/unregister")
  public String getTaskUnRegistrationView(Model model, Authentication auth,
      @PathVariable("uuid") String uuid) {
    SubscriberTopicDto topic = topicService.getTopic(uuid, auth.getName());
    model.addAttribute("topic", topic);
    return "topic-unregistration";
  }

  /**
   * Nimmt das Abonnement (d.h. die Bestätigung auf die Nachfrage) entgegen und löscht das Abo.
   */
  @PostMapping("/topics/{uuid}/unregister")
  public String handleTaskUnRegistration(Model model, Authentication auth,
      @PathVariable("uuid") String uuid, RedirectAttributes redirectAttributes) {
    // Erst den Anwender aus den Subscribern des Topics entfernen ...
    topicService.unsubscribe(uuid, auth.getName());
    // ... dann alle Statuseinträge des Benutzers für Tasks des Topics löschen
    taskService.deleteTaskStatusesforTopic(uuid, auth.getName());
    // Danach darf getTopic() nicht mehr aufgerufen werden, da sonst die Status neu
    // erstellt werden!
    redirectAttributes.addFlashAttribute("info", "Abo wurde beendet.");
    return "redirect:/topics/";
  }

  /**
   * Erstellt Übersicht eines Topics für einen Abonennten.
   */
  @GetMapping("/topics/{uuid}")
  public String createTopicView(Model model, Authentication auth,
      @PathVariable("uuid") String uuid) {
    SubscriberTopicDto topic = topicService.getTopic(uuid, auth.getName());
    model.addAttribute("topic", topic);
    model.addAttribute("tasks", taskService.getTasksForTopic(uuid, auth.getName()));
    return "topic";
  }

  /**
   * Erstellt die Liste der Teilnehmenden eines Topics.
   */
  @GetMapping("/topics/{uuid}/subscriberlist")
  public String createSubscriberlist(Model model, Authentication auth,
      @PathVariable("uuid") String uuid) {
    List<SubscriberTopicDto> subscribers =
        topicService.getSubscribedUsersWithFinishedTasks(uuid, auth.getName());
    model.addAttribute("subscribers", subscribers);
    return "topic-subscriberlist";
  }

  /**
   * Löscht ein Topic.
   */
  @PostMapping("/topics/{uuid}/delete")
  public String deleteTopic(Authentication auth, @PathVariable("uuid") String uuid,
      RedirectAttributes redirectAttributes) {
    try {
      topicService.deleteTopic(uuid, auth.getName());
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", e.getMessage());
      return "redirect:/topics";
    }
    redirectAttributes.addFlashAttribute("success", "Topic wurde gelöscht.");
    return "redirect:/topics";
  }

  /**
   * Aktualisiert ein Topic.
   */
  @PostMapping("/topics/{uuid}/manage")
  public String handelUpdateTopic(@ModelAttribute("topic") OwnerTopicDto topic,
      @PathVariable("uuid") String uuid,
      @RequestHeader(value = "referer", required = true) String referer, Authentication auth,
      RedirectAttributes redirectAttributes) {
    try {
      topicService.updateTopic(uuid, auth.getName(), topic.getShortDescription(),
          topic.getLongDescription());
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", e.getMessage());
      return "redirect:" + referer;
    }
    redirectAttributes.addFlashAttribute("success", "Topic aktualisiert!");
    return "redirect:" + referer;
  }
}
