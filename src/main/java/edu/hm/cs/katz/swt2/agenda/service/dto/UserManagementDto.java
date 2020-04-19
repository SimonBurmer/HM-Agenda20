package edu.hm.cs.katz.swt2.agenda.service.dto;

public class UserManagementDto extends UserDisplayDto {

  private String password = "";

  public UserManagementDto(String login, String password) {
    super(login);
    this.password = password;
  }

  public UserManagementDto() {
    super();
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
