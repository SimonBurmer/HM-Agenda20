package edu.hm.cs.katz.swt2.agenda.service;

import edu.hm.cs.katz.swt2.agenda.common.TaskTypeEnum;
import edu.hm.cs.katz.swt2.agenda.mvc.Search;
import edu.hm.cs.katz.swt2.agenda.service.dto.OwnerTaskDto;
import edu.hm.cs.katz.swt2.agenda.service.dto.StatusDto;
import edu.hm.cs.katz.swt2.agenda.service.dto.SubscriberTaskDto;
import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 * Serviceklasse für Verarbeitung von Tasks.
 * 
 * @author Bastian Katz (mailto: bastian.katz@hm.edu)
 */
public interface TaskService {

  /**
   * Lösche einen Task.
   */
  void deleteTask(Long id, String login);

  /**
   * Erstellt einen neuen Task.
   */
  Long createTask(String topicUuid, String title, String login, String taskShortDescription,
      String taskLongDescription, TaskTypeEnum taskType, MultipartFile imageFile);

  /**
   * Erstellt einen neuen Demodata-Task (Lädt DemoImages mittels String fileName).
   */
  Long createTask(String topicUuid, String title, String login, String taskShortDescription,
      String taskLongDescription, TaskTypeEnum taskType, String fileName);

  /**
   * Aktualisiert einen Task.
   */
  void updateTask(Long id, String login, String taskShortDescription, String taskLongDescription,
      TaskTypeEnum taskType, MultipartFile imageFile) throws IOException;

  /**
   * Zugriff auf einen Task (priviligierte Sicht für Ersteller des Topics).
   */
  OwnerTaskDto getManagedTask(Long taskId, String login);

  /**
   * Zugriff auf alle Tasks eines eigenen Topics.
   */
  List<OwnerTaskDto> getManagedTasks(String topicUuid, String login);

  /**
   * Zugriff auf einen Task (Abonnentensicht).
   */
  SubscriberTaskDto getTask(Long taskId, String login);

  /**
   * Zugriff auf alle Tasks abonnierter Topics.
   * 
   */
  List<SubscriberTaskDto> getSubscribedTasks(String login, Search search);

  /**
   * Zugriff auf alle Tasks eines abonnierten Topics.
   */
  List<SubscriberTaskDto> getTasksForTopic(String topicUuid, String login);

  /**
   * Zugriff auf alle Status eines verwalteten Tasks.
   */
  List<StatusDto> getTaskStatuses(Long taskId, String login);

  /**
   * Markiert einen Task für einen Abonnenten als "done".
   */
  void checkTask(Long taskId, String login);

  /**
   * Aktualisiere den Kommentar zu einem Task.
   */
  void updateComment(Long taskId, String login, String comment);

  /**
   * Setzt den Status eines Tasks für einen Abonnenten zurück auf NEU.
   */
  void resetTask(Long taskId, String login);

  /**
   * Löscht für einen Abonnenten alle Status für Tasks eines Topics.
   */
  void deleteTaskStatusesforTopic(String topicUuid, String login);

  /**
   * Löscht alle Tasks eines Topics.
   */
  void deleteTasksFromTopic(String topicUuid, String login);
}
