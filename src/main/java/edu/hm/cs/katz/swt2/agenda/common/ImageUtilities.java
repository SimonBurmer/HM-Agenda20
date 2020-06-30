package edu.hm.cs.katz.swt2.agenda.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;


public class ImageUtilities {

  private static final Logger LOG = LoggerFactory.getLogger(ImageUtilities.class);


  /**
   * Lädt ein Image aus dem Resources/static/assets Verzeichnis.
   */
  public static byte[] imageLoader(String fileName) {
    try {
      File file = new ClassPathResource("static/assets/" + fileName).getFile();
      byte[] fileContent = Files.readAllBytes(file.toPath());
      return fileContent;
    } catch (IOException e) {
      LOG.error("Image {} konnten nicht geladen werden!", fileName);
      throw new ImageException("Fehler beim laden des Image!");
    }
  }
}
