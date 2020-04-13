package edu.hm.cs.katz.swt2.agenda;

import java.util.Collection;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityHelper {

  public static final String ADMIN_ROLE = "ROLE_ADMIN";
  public static final GrantedAuthority ADMIN_AUTHORITY = new SimpleGrantedAuthority(ADMIN_ROLE);
  public static final Collection<GrantedAuthority> ADMIN_ROLES =
      AuthorityUtils.createAuthorityList(ADMIN_ROLE);
  public static final Collection<GrantedAuthority> STANDARD_ROLES =
      AuthorityUtils.createAuthorityList();


  public static void escalate() {
    SecurityContext sc = SecurityContextHolder.getContext();
    if (sc.getAuthentication() == null) {

      UsernamePasswordAuthenticationToken authReq =
          new UsernamePasswordAuthenticationToken("", "", ADMIN_ROLES);
      sc.setAuthentication(authReq);
    }
  }

  public static boolean isAdmin(Authentication auth) {
    if (auth == null)
      return false;
    return auth.getAuthorities().contains(ADMIN_AUTHORITY);
  }


}
