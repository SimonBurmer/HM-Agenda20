package edu.hm.cs.katz.swt2.agenda.mvc;

public class Search {

  private String search;
  private boolean onlyNewTasks;

  public Search() {
    this.search = "";
    this.onlyNewTasks = false;
  }

  /**
   * Parameter der Suche.
   */
  public Search(String searchParameter, String onlyNewTasksParameter) {
    this.search = searchParameter;
    if (onlyNewTasksParameter.equals("yes")) {
      this.onlyNewTasks = true;
    } else {
      this.onlyNewTasks = false;
    }
  }

  public String getSearch() {
    return search;
  }

  public void setSearch(String search) {
    this.search = search;
  }

  public boolean isOnlyNewTasks() {
    return onlyNewTasks;
  }

  public void setOnlyNewTasks(boolean onlyNewTasks) {
    this.onlyNewTasks = onlyNewTasks;
  }

  public boolean hasParameters() {
    return !search.isEmpty() || onlyNewTasks;
  }
}
