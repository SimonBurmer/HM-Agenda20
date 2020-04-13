package edu.hm.cs.katz.swt2.agenda.mvc;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController extends AbstractController {


  @GetMapping("/")
  public String home(Model model, Authentication auth) {
    return "index";
  }
}
