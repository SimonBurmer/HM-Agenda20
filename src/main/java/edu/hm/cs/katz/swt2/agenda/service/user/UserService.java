package edu.hm.cs.katz.swt2.agenda.service.user;

import edu.hm.cs.katz.swt2.agenda.mvc.anwender.AnwenderDisplayDto;
import java.util.List;


public interface UserService {

  List<AnwenderDisplayDto> findeAlleAnwender();

  List<AnwenderDisplayDto> findeAdmins();

  AnwenderDisplayDto getAnwenderInfo(String login);

  void legeAn(String login, String password, boolean b);

}
