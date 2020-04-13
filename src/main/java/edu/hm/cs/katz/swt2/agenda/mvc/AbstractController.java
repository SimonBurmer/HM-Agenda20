package edu.hm.cs.katz.swt2.agenda.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ModelAttribute;
import edu.hm.cs.katz.swt2.agenda.SecurityHelper;
import edu.hm.cs.katz.swt2.agenda.service.AnwenderService;

public abstract class AbstractController {

  @Autowired
  AnwenderService anwenderService;
  
  @ModelAttribute("administration")
  public boolean isAdministrator(Authentication auth) {
    return SecurityHelper.isAdmin(auth);
  }

  @ModelAttribute("user")
  private AnwenderDto anwender(Authentication auth) {
    if (auth != null) {
      return anwenderService.getAnwenderInfo(auth.getName());
    }
    return null;
  }

}
