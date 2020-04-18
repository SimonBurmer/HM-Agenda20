package edu.hm.cs.katz.swt2.agenda.mvc.anwender;

public class AnwenderManagementDto extends AnwenderDisplayDto {

  private String password = "";

  public AnwenderManagementDto(String login, String password) {
    super(login);
    this.password = password;
  }

  public AnwenderManagementDto() {
    super();
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
