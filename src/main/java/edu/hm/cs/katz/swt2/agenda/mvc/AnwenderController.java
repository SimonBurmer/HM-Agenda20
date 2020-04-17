package edu.hm.cs.katz.swt2.agenda.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import edu.hm.cs.katz.swt2.agenda.service.AnwenderService;

/**
 * @author katz
 *
 */
@Controller
public class AnwenderController extends AbstractController {

  @Autowired
  AnwenderService anwenderService;

  @GetMapping("/anwender")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public String home(Model model, Authentication auth) {
    model.addAttribute("anwender", anwenderService.findeAlleAnwender());
    model.addAttribute("administration",
        auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
    // Vorlage bzw. View
    return "anwender-listview";
  }

}
