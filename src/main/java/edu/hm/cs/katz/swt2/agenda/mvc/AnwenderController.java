package edu.hm.cs.katz.swt2.agenda.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import edu.hm.cs.katz.swt2.agenda.mvc.anwender.AnwenderManagementDto;
import edu.hm.cs.katz.swt2.agenda.service.AnwenderService;

/**
 * Controller-Klasse für die
 * 
 * @author Bastian Katz (mailto: bastian.katz@hm.edu)
 */
@Controller
public class AnwenderController extends AbstractController {

  @Autowired
  private AnwenderService anwenderService;

  @GetMapping("/anwender")
  public String getAnwenderListView(Model model, Authentication auth) {
    model.addAttribute("anwender", anwenderService.findeAlleAnwender());
    return "anwender-listview";
  }

  @GetMapping("/anwender/create")
  public String getAnwenderCreationView(Model model, Authentication auth) {
    model.addAttribute("newAnwender", new AnwenderManagementDto());
    return "anwender-creation";
  }

  @PostMapping("anwender")
  public String createAnwender(Model model, Authentication auth,
      @ModelAttribute("newAnwender") AnwenderManagementDto anwender) {
    anwenderService.legeAn(anwender.getLogin(), anwender.getPassword(), false);
    return "redirect:/anwender";
  }
}
