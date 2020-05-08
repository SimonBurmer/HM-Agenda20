package edu.hm.cs.katz.swt2.agenda.service;

import edu.hm.cs.katz.swt2.agenda.service.dto.UserDisplayDto;
import java.util.List;


public interface UserService {

  List<UserDisplayDto> getAllUsers();

  List<UserDisplayDto> findeAdmins();

  UserDisplayDto getUserInfo(String login);

  void legeAn(String login, String password, boolean isAdministrator);

}
