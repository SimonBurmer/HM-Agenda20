package edu.hm.cs.katz.swt2.agenda.mvc;

import edu.hm.cs.katz.swt2.agenda.SecurityHelper;
import edu.hm.cs.katz.swt2.agenda.service.user.AnwenderDisplayDto;
import edu.hm.cs.katz.swt2.agenda.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Abstrakte Basisklasse für alle Controller, sorgt dafür, dass einige Verwaltungsattribute immer an
 * die Views übertragen werden.
 * 
 * @author Bastian Katz (mailto: bastian.katz@hm.edu)
 *
 */
public abstract class AbstractController {

  @Autowired
  private UserService anwenderService;

  @ModelAttribute("administration")
  public boolean isAdministrator(Authentication auth) {
    return SecurityHelper.isAdmin(auth);
  }

  @ModelAttribute("user")
  private AnwenderDisplayDto anwender(Authentication auth) {
    if (auth != null) {
      AnwenderDisplayDto anwenderInfo = anwenderService.getAnwenderInfo(auth.getName());
      return anwenderInfo;
    }
    return null;
  }

}
