package edu.hm.cs.katz.swt2.agenda.mvc;

import edu.hm.cs.katz.swt2.agenda.service.user.AnwenderManagementDto;
import edu.hm.cs.katz.swt2.agenda.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller-Klasse für alle Interaktionen, die das Verwalten der Anwender betrifft. Controller
 * reagieren auf Aufrufe von URLs. Sie benennen ein View-Template (Thymeleaf-Vorlage) und stellen
 * Daten zusammen, die darin dargestellt werden.
 * 
 * @author Bastian Katz (mailto: bastian.katz@hm.edu)
 */
@Controller
public class AnwenderController extends AbstractController {

  @Autowired
  private UserService anwenderService;

  /**
   * Erzeugt eine Listenansicht mit allen Anwendern.
   */
  @GetMapping("/anwender")
  public String getAnwenderListView(Model model, Authentication auth) {
    model.addAttribute("anwender", anwenderService.findeAlleAnwender());
    return "anwender-listview";
  }

  /**
   * Erzeugt eine Formularansicht für das Erstellen eines Anwenders.
   */
  @GetMapping("/anwender/create")
  public String getAnwenderCreationView(Model model) {
    model.addAttribute("newAnwender", new AnwenderManagementDto());
    return "anwender-creation";
  }

  /**
   * Nimmt den Formularinhalt vom Formular zum Erstellen eines Anwenders entgegen und legt einen
   * entsprechenden Anwender an. Kommt es dabei zu einer Exception, wird das Erzeugungsformular
   * wieder angezeigt und eine Fehlermeldung eingeblendet. Andernfalls wird auf die Listenansicht
   * der Anwender weitergeleitet und das Anlegen in einer Einblendung bestätigt.
   */
  @PostMapping("anwender")
  public String createAnwender(Model model,
      @ModelAttribute("newAnwender") AnwenderManagementDto anwender,
      RedirectAttributes redirectAttributes) {
    try {
      anwenderService.legeAn(anwender.getLogin(), anwender.getPassword(), false);
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", e.getMessage());
      return "redirect:/anwender/create";
    }
    redirectAttributes.addFlashAttribute("success",
        "Anwender " + anwender.getLogin() + " erstellt.");
    return "redirect:/anwender";
  }
}
