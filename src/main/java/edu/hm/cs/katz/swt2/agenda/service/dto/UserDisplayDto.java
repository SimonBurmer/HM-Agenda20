package edu.hm.cs.katz.swt2.agenda.service.dto;

public class UserDisplayDto {

  private String login = "";

  public UserDisplayDto(String login) {
    this.login = login;
  }

  public UserDisplayDto() {
  }

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
