package edu.hm.cs.katz.swt2.agenda.mvc;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller-Klasse für die Landing-Page. Controller reagieren auf Aufrufe von URLs. Sie benennen
 * ein View-Template (Thymeleaf-Vorlage) und stellen Daten zusammen, die darin dargestellt werden.
 * Dafür verwenden Sie Methoden der Service-Schicht.
 * 
 * @author Bastian Katz (mailto: bastian.katz@hm.edu)
 */

@Controller
public class IndexController extends AbstractController {

  /**
   * Erstellt die Landing-Page.
   */
  @GetMapping("/")
  public String getIndexView(Model model, Authentication auth) {
    return "index";
  }
}
