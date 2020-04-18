package edu.hm.cs.katz.swt2.agenda.mvc.anwender;

public class AnwenderDisplayDto {

  private String login = "";

  public AnwenderDisplayDto(String login) {
    this.login = login;
  }

  public AnwenderDisplayDto() {}

  public String getDisplayName() {
    return login;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }
}
