package edu.hm.cs.katz.swt2.agenda.mvc;

import edu.hm.cs.katz.swt2.agenda.common.TaskTypeEnum;
import edu.hm.cs.katz.swt2.agenda.service.TaskService;
import edu.hm.cs.katz.swt2.agenda.service.TopicService;
import edu.hm.cs.katz.swt2.agenda.service.dto.OwnerTaskDto;
import edu.hm.cs.katz.swt2.agenda.service.dto.OwnerTopicDto;
import edu.hm.cs.katz.swt2.agenda.service.dto.StatusDto;
import edu.hm.cs.katz.swt2.agenda.service.dto.SubscriberTaskDto;
import edu.hm.cs.katz.swt2.agenda.service.dto.TaskDto;
import java.util.List;
import javax.validation.ValidationException;
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

@Controller
public class TaskController extends AbstractController {

  @Autowired
  private TopicService topicService;

  @Autowired
  private TaskService taskService;

  /**
   * Ertellt das Formular zur Erfassung eines neuen Tasks.
   */
  @GetMapping("/topics/{uuid}/createTask")
  public String getTaskCreationView(Model model, Authentication auth,
      @PathVariable("uuid") String uuid) {
    OwnerTopicDto topic = topicService.getManagedTopic(uuid, auth.getName());
    model.addAttribute("topic", topic);
    model.addAttribute("newTask", new TaskDto(null, "", "", "", TaskTypeEnum.DEFAULT, topic));
    return "task-creation";
  }

  /**
   * Verarbeitet die Erstellung eines Tasks.
   */
  @PostMapping("/topics/{uuid}/createTask")
  public String handleTaskCreation(Model model, Authentication auth,
      @PathVariable("uuid") String uuid, @ModelAttribute("newTask") TaskDto newTask,
      RedirectAttributes redirectAttributes) {
    try {
      taskService.createTask(uuid, newTask.getTitle(), auth.getName(),
          newTask.getTaskShortDescription(), newTask.getTaskLongDescription(),
          newTask.getTaskType());
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", e.getMessage());
      return "redirect:/topics/" + uuid + "/createTask";
    }
    redirectAttributes.addFlashAttribute("success",
        "Task \"" + newTask.getTitle() + "\" erstellt.");
    return "redirect:/topics/" + uuid + "/manage";
  }

  /**
   * Erstellt die Taskansicht für Abonnenten.
   */
  @GetMapping("tasks/{id}")
  public String getSubscriberTaskView(Model model, Authentication auth,
      @PathVariable("id") Long id) {
    SubscriberTaskDto task = taskService.getTask(id, auth.getName());
    model.addAttribute("task", task);
    model.addAttribute("status", task.getStatus());
    return "task";
  }

  /**
   * Erstellt die Taskansicht für den Verwalter/Ersteller eines Topics.
   */
  @GetMapping("tasks/{id}/manage")
  public String getManagerTaskView(Model model, Authentication auth, @PathVariable("id") Long id) {
    OwnerTaskDto task = taskService.getManagedTask(id, auth.getName());
    List<StatusDto> statuses = taskService.getTaskStatuses(id, auth.getName());
    model.addAttribute("task", task);
    model.addAttribute("statuses", statuses);
    return "task-management";
  }

  /**
   * Lösche einen Task.
   */
  @PostMapping("tasks/{id}/delete")
  public String handleDeletion(Authentication auth, @PathVariable("id") Long id,
      RedirectAttributes redirectAttributes) {
    TaskDto task = taskService.getManagedTask(id, auth.getName());
    try {
      taskService.deleteTask(id, auth.getName());
    } catch (Exception e) {
      // Zeige die Fehlermeldung der Exception als Flash Attribut an.
      redirectAttributes.addFlashAttribute("error", e.getMessage());
      return "redirect:/topics/" + task.getTopic().getUuid() + "/manage";
    }
    // Zeige die erfolgreiche Löschung als Flash Attribut an.
    redirectAttributes.addFlashAttribute("success", "Task gelöscht");
    return "redirect:/topics/" + task.getTopic().getUuid() + "/manage";

  }

  /**
   * Aktualisiert die Kurz- und Langbeschreibung eines Tasks.
   */
  @PostMapping("tasks/{id}/manage")
  public String handleUpdate(@ModelAttribute("task") TaskDto task, Authentication auth,
      @PathVariable("id") Long id,
      @RequestHeader(value = "referer", required = true) String referer,
      RedirectAttributes redirectAttributes) {
    try {
      taskService.updateTask(id, auth.getName(), task.getTaskShortDescription(),
          task.getTaskLongDescription(), task.getTaskType());
    } catch (Exception e) {
      // Zeige die Fehlermeldung der Exception als Flash Attribut an.
      redirectAttributes.addFlashAttribute("error", e.getMessage());
      return "redirect:" + referer;
    }
    // Zeige die erfolgreiche Aktualisierung als Flash Attribut an.
    redirectAttributes.addFlashAttribute("success", "Task aktualisiert!");
    return "redirect:" + referer;
  }

  /**
   * Verarbeitet die Markierung eines Tasks als "Done".
   */
  @PostMapping("tasks/{id}/check")
  public String handleTaskChecking(Authentication auth, @PathVariable("id") Long id,
      @RequestHeader(value = "referer", required = true) String referer) {
    taskService.checkTask(id, auth.getName());
    return "redirect:" + referer;
  }

  /**
   * Aktualisiert den Kommentar zu einem Task.
   */
  @PostMapping("tasks/{id}/comment")
  public String handleTaskComment(Model model, Authentication auth, @PathVariable("id") Long id,
      @RequestHeader(value = "referer", required = true) String referer,
      @ModelAttribute("comment") StatusDto comment, RedirectAttributes redirectAttributes) {
    try {
      taskService.updateComment(id, auth.getName(), comment.getComment());
    } catch (ValidationException e) {
      redirectAttributes.addFlashAttribute("error", e.getMessage());
      return "redirect:" + referer;
    }
    redirectAttributes.addFlashAttribute("success", "Kommentar gespeichert!");
    return "redirect:" + referer;
  }

  /**
   * Verarbeitet die Markierung eines Tasks als "Zurückgesetzt".
   */
  @PostMapping("tasks/{id}/reset")
  public String handleTaskReset(Authentication auth, @PathVariable("id") Long id,
      @RequestHeader(value = "referer", required = true) String referer) {
    taskService.resetTask(id, auth.getName());
    return "redirect:" + referer;
  }

  /**
   * Erstellt die Übersicht aller Tasks abonnierter Topics für einen Anwender.
   */
  @GetMapping("tasks")
  public String getSubscriberTaskListView(Model model, Authentication auth,
      @RequestParam(name = "search", required = false, defaultValue = "") String searchParameter,
      @RequestParam(name = "onlyNewTasks", required = false,
          defaultValue = "no") String onlyNewTasksParameter) {
    Search search = new Search(searchParameter, onlyNewTasksParameter);
    List<SubscriberTaskDto> tasks = taskService.getSubscribedTasks(auth.getName(), search);
    model.addAttribute("search", search);
    model.addAttribute("tasks", tasks);
    return "task-listview";
  }

}
