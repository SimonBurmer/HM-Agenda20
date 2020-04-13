package edu.hm.cs.katz.swt2.agenda.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import edu.hm.cs.katz.swt2.agenda.SecurityHelper;
import edu.hm.cs.katz.swt2.agenda.service.task.TaskService;
import edu.hm.cs.katz.swt2.agenda.service.topic.ManagedTopicDto;
import edu.hm.cs.katz.swt2.agenda.service.topic.TopicDto;
import edu.hm.cs.katz.swt2.agenda.service.topic.TopicService;

@Controller
public class TopicController extends AbstractController {

  @Autowired
  TopicService topicService;
  
  @Autowired
  TaskService taskService;

  @GetMapping("/topics")
  public String topicList(Model model, Authentication auth) {
    model.addAttribute("administration", SecurityHelper.isAdmin(auth));
    model.addAttribute("managedTopics", topicService.getManagedTopics(auth.getName()));
    model.addAttribute("topics", topicService.getSubscriptions(auth.getName()));
    // Vorlage bzw. View
    return "topic-listview";
  }

  @GetMapping("/createTopic")
  public String topicCreation(Model model, Authentication auth) {
    model.addAttribute("administration", SecurityHelper.isAdmin(auth));
    model.addAttribute("newTopic", new EditableTopic());
    return "topic-creation";
  }

  @PostMapping("/topics")
  public String topicCreations(Model model, Authentication auth,
      @ModelAttribute("newTopic") EditableTopic topic) {
    topicService.createTopic(topic.getTitle(), auth.getName());    
    return "redirect:/topics";
  }
 
  // Seite nur für Management!
  @GetMapping("/topics/{uuid}/manage")
  public String topicManagement(Model model, Authentication auth, @PathVariable("uuid") String uuid) {
    model.addAttribute("administration", SecurityHelper.isAdmin(auth));
    ManagedTopicDto topic = topicService.getManagedTopic(uuid);
    model.addAttribute("topic", topic);
    model.addAttribute("tasks", topicService.getManagedTasks(uuid));
    return "topic-management";   
  }

  @GetMapping("/topics/{uuid}/register")
  public String getTaskRegistrationView(Model model, Authentication auth, @PathVariable("uuid") String uuid) {
    model.addAttribute("administration", SecurityHelper.isAdmin(auth));
    TopicDto topic = topicService.getTopic(uuid);
    model.addAttribute("topic", topic);
    return "topic-registration";
  }  
  
  @PostMapping("/topics/{uuid}/register")
  public String handleTaskRegistration(Model model, Authentication auth, @PathVariable("uuid") String uuid) {
    model.addAttribute("administration", SecurityHelper.isAdmin(auth));
    topicService.register(uuid, auth.getName());
    return "redirect:/topics/"+uuid;
  }  

  @GetMapping("/topics/{uuid}")
  // TODO: Sinnvoll nur für registrierte Anwender
  public String createTopicView(Model model, Authentication auth, @PathVariable("uuid") String uuid) {
    model.addAttribute("administration", SecurityHelper.isAdmin(auth));
    TopicDto topic = topicService.getTopic(uuid);
    model.addAttribute("topic", topic);
    model.addAttribute("tasks", topicService.getTasks(uuid));
    return "topic";
  }
  
}
