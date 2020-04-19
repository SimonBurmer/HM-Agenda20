package edu.hm.cs.katz.swt2.agenda.mvc;

import edu.hm.cs.katz.swt2.agenda.common.SecurityHelper;
import edu.hm.cs.katz.swt2.agenda.service.UserService;
import edu.hm.cs.katz.swt2.agenda.service.dto.UserDisplayDto;
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
  private UserService userService;

  @ModelAttribute("administration")
  private boolean isAdministrator(Authentication auth) {
    return SecurityHelper.isAdmin(auth);
  }

  @ModelAttribute("user")
  private UserDisplayDto user(Authentication auth) {
    if (auth != null) {
      UserDisplayDto anwenderInfo = userService.getUserInfo(auth.getName());
      return anwenderInfo;
    }
    return null;
  }

}
