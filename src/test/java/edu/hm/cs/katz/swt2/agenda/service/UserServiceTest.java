package edu.hm.cs.katz.swt2.agenda.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.hm.cs.katz.swt2.agenda.persistence.User;
import edu.hm.cs.katz.swt2.agenda.persistence.UserRepository;
import javax.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @Mock
  UserRepository userRepository;

  @InjectMocks
  UserService userService = new UserServiceImpl();


  @Test
  public void createUserSuccess() {
    String user_Login = "tiffy";
    String user_Name = "Tiffy";
    String user_Pw = "Da3rG23*";
    Boolean isAdmin = false;

    // Given
    Mockito.when(userRepository.existsById(user_Login)).thenReturn(false);

    // When
    userService.legeAn(user_Login, user_Name, user_Pw, isAdmin);

    // Then
    ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
    Mockito.verify(userRepository).save(userCaptor.capture());
    Mockito.verifyNoMoreInteractions(userRepository);
    assertEquals(user_Login, userCaptor.getValue().getLogin());
    assertEquals(user_Name, userCaptor.getValue().getName());
    assertEquals("{noop}" + user_Pw, userCaptor.getValue().getPassword());
    assertEquals(isAdmin, userCaptor.getValue().isAdministrator());
  }

  @Test
  public void shouldFailIfUserExists() {
    String user_Login = "tiffy";
    String user_Name = "Tiffy";
    String user_Pw = "Da3rG23*";
    Boolean isAdmin = false;


    Mockito.when(userRepository.existsById(user_Login)).thenReturn(true);

    assertThrows(ValidationException.class, () -> {
      userService.legeAn(user_Login, user_Name, user_Pw, isAdmin);
    });
  }

  @Test
  public void shouldFailIfUserLoginHasUppercase() {
    String user_Login = "Tiffy";
    String user_Name = "Tiffy";
    String user_Pw = "Da3rG23*";
    Boolean isAdmin = false;


    assertThrows(ValidationException.class, () -> {
      userService.legeAn(user_Login, user_Name, user_Pw, isAdmin);
    });
  }

  @Test
  public void shouldFailIfUserPwContainsSpace() {
    String user_Login = "tiffy";
    String user_Name = "Tiffy";
    String user_Pw = "Da3r G23*";
    Boolean isAdmin = false;


    assertThrows(ValidationException.class, () -> {
      userService.legeAn(user_Login, user_Name, user_Pw, isAdmin);
    });
  }


}


