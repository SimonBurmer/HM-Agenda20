package edu.hm.cs.katz.swt2.agenda.service;

import java.util.Comparator;
import edu.hm.cs.katz.swt2.agenda.persistence.*;

public class TopicComparator implements Comparator<Topic> {
  /**
   * Comparatorklasse um Topics ihrem Titel nach alphabetisch zu sortieren.
   * 
   * @author Simon Burmer
   */

  @Override
  public int compare(Topic a, Topic b) {
    if (a.getTitle().compareTo(b.getTitle()) > 0) {
      return 1;
    }
    if (a.getTitle().compareTo(b.getTitle()) < 0) {
      return -1;
    }
    return 0;
  }
}