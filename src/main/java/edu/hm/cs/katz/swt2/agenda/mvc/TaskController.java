package edu.hm.cs.katz.swt2.agenda.mvc;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import edu.hm.cs.katz.swt2.agenda.SecurityHelper;
import edu.hm.cs.katz.swt2.agenda.service.task.ManagedTaskDto;
import edu.hm.cs.katz.swt2.agenda.service.task.ReadTaskDto;
import edu.hm.cs.katz.swt2.agenda.service.task.TaskDto;
import edu.hm.cs.katz.swt2.agenda.service.task.TaskService;
import edu.hm.cs.katz.swt2.agenda.service.topic.ManagedTopicDto;
import edu.hm.cs.katz.swt2.agenda.service.topic.TopicService;

@Controller
public class TaskController extends AbstractController {

  @Autowired
  TopicService topicService;

  @Autowired
  TaskService taskService;

  @GetMapping("/topics/{uuid}/createTask")
  public String getTaskCreationView(Model model, Authentication auth,
      @PathVariable("uuid") String uuid) {
    model.addAttribute("administration", SecurityHelper.isAdmin(auth));
    ManagedTopicDto topic = topicService.getManagedTopic(uuid);
    model.addAttribute("topic", topic);
    model.addAttribute("newTask", new TaskCreation());

    return "task-creation";
  }

  @PostMapping("/topics/{uuid}/createTask")
  public String handleTaskCreation(Model model, Authentication auth,
      @PathVariable("uuid") String uuid, @ModelAttribute("newTask") TaskCreation newTask) {
    taskService.createTask(uuid, newTask.getTitel());
    return "redirect:/topics/" + uuid + "/manage";
  }

  @GetMapping("tasks/{id}")
  public String getSubscriberTaskView(Model model, Authentication auth,
      @PathVariable("id") Long id) {
    TaskDto task = taskService.getTask(id, auth.getName());
    model.addAttribute("task", task);
    return "task";
  }
  
  @GetMapping("tasks/{id}/manage")
  public String getManagerTaskView(Model model, Authentication auth,
      @PathVariable("id") Long id) {
    ManagedTaskDto task = taskService.getManagedTask(id, auth.getName());
    model.addAttribute("task", task);
    return "task-management";
  }

  @PostMapping("tasks/{id}/check")
  public String checkTask(Model model, Authentication auth,
      @PathVariable("id") Long id) {
    taskService.checkTask(id, auth.getName());
    return "redirect:/tasks";
  }

  @GetMapping("tasks")
  public String getSubscriberTaskListView(Model model, Authentication auth) {
    List<ReadTaskDto> tasks = taskService.getSubscribedTasks(auth.getName());
    model.addAttribute("tasks", tasks);
    return "tasks";
  }
}
