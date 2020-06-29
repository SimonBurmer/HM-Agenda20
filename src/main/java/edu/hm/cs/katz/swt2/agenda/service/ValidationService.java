package edu.hm.cs.katz.swt2.agenda.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.regex.Pattern;
import javax.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;
import edu.hm.cs.katz.swt2.agenda.persistence.Task;


public class ValidationService {

  private static final Logger LOG = LoggerFactory.getLogger(ValidationService.class);
  private static String EINGABEHINWEIS = " Bitte passen Sie Ihre Eingabe an!";

  // Check Topic title, shortDescription, longDescription requirements
  public static void topicValidation(String title, String shortDescription,
      String longDescription) {

    if (title.length() < 10) {
      LOG.debug("Titel von Topics müssen mindestens 10 Zeichen lang sein!");
      throw new ValidationException("Titel müssen mindestens 10 Zeichen lang sein!" + EINGABEHINWEIS);
    }

    if (title.length() > 60) {
      LOG.debug("Titel von Topics dürfen nicht länger als 60 Zeichen sein!");
      throw new ValidationException("Maximale Länge von 60 Zeichen überschritten!" + EINGABEHINWEIS);
    }
    topicValidation(shortDescription, longDescription);

  }

  // Check Topic shortDescription and longDescription requirements
  public static void topicValidation(String shortDescription, String longDescription) {

    if (shortDescription.length() < 100) {
      LOG.debug("Kurzbeschreibungen von Topics müssen mindestens 100 Zeichen lang sein!");
      throw new ValidationException("Kurzbeschreibungen müssen mindestens 100 Zeichen lang sein!" + EINGABEHINWEIS);
    }

    if (shortDescription.length() > 200) {
      LOG.debug("Kurzbeschreibungen von Topics dürfen nicht länger als 200 Zeichen sein!");
      throw new ValidationException("Maximale Länge von 200 Zeichen überschritten!" + EINGABEHINWEIS);
    }

    if (longDescription.length() < 200) {
      LOG.debug("Langbeschreibungen von Topics müssen mindestens 200 Zeichen lang sein!");
      throw new ValidationException("Langbeschreibungen müssen mindestens 200 Zeichen lang sein!" + EINGABEHINWEIS);
    }
    if (longDescription.length() > 2000) {
      LOG.debug("Langbeschreibungen von Topics dürfen nicht länger als 2000 Zeichen sein!");
      throw new ValidationException("Maximale Länge von 2000 Zeichen überschritten!" + EINGABEHINWEIS);
    }
  }

  // Check task title, taskshortDescription, tasklongDescription requirements
  public static void taskValidation(String titel, String taskShortDescription,
      String taskLongDescription) {

    if (titel.length() < 8) {
      LOG.debug("Titel von Tasks müssen mindestens 8 Zeichen lang sein!");
      throw new ValidationException("Titel müssen mindestens 8 Zeichen lang sein!" + EINGABEHINWEIS);
    }
    if (titel.length() > 32) {
      LOG.debug("Titel von Tasks dürfen nicht länger als 32 Zeichen sein!");
      throw new ValidationException("Maximale Länge von 32 Zeichen überschritten!" + EINGABEHINWEIS);
    }
    taskValidation(taskShortDescription, taskLongDescription);

  }

  // Check Task taskshortDescription and tasklongDescription requirements
  public static void taskValidation(String taskShortDescription, String taskLongDescription) {

    if (taskShortDescription.length() < 100) {
      LOG.debug("Kurzbeschreibungen von Tasks müssen mindestens 100 Zeichen lang sein!");
      throw new ValidationException("Kurzbeschreibungen müssen mindestens 100 Zeichen lang sein!" + EINGABEHINWEIS);
    }
    if (taskShortDescription.length() > 200) {
      LOG.debug("Kurzbeschreibungen von Tasks dürfen nicht länger als 200 Zeichen sein!");
      throw new ValidationException("Maximale Länge von 200 Zeichen ueberschritten!" + EINGABEHINWEIS);
    }

    if (taskLongDescription.length() < 200) {
      LOG.debug("Langbeschreibungen von Tasks müssen mindestens 200 Zeichen lang sein!");
      throw new ValidationException("Langbeschreibungen müssen mindestens 200 Zeichen lang sein!" + EINGABEHINWEIS);
    }
    if (taskLongDescription.length() > 2000) {
      LOG.debug("Langbeschreibungen von Tasks dürfen nicht länger als 2000 Zeichen sein!");
      throw new ValidationException("Maximale Länge von 2000 Zeichen überschritten!" + EINGABEHINWEIS);
    }
  }
  
  // Check User login, name and password requirements
  public static void userValidation(String login, String name, String password) {

    if (!login.equals(login.toLowerCase())) {
      LOG.debug("Der Name eines Users darf nur aus kleingeschriebenen Buchstaben bestehen!");
      throw new ValidationException("Der Name darf nur aus kleingeschriebenen Buchstaben bestehen!" + EINGABEHINWEIS);
    }

    if (login.length() < 4 || login.length() > 20) {
      LOG.debug("Der Name eines Users muss zwischen 4 und 20 Zeichen lang sein!");
      throw new ValidationException("Der Name muss zwischen 4 und 20 Zeichen lang sein!" + EINGABEHINWEIS);
    }

    if (name.length() < 1 || name.length() > 32) {
      LOG.debug("Der Name eines Users muss zwischen 1 und 32 Zeichen lang sein!");
      throw new ValidationException("Der Name muss zwischen 1 und 32 Zeichen lang sein!" + EINGABEHINWEIS);
    }

    if (password.length() < 8 || password.length() > 20) {
      LOG.debug("Das Passwort eines Users muss zwischen 8 und 20 Zeichen lang sein!");
      throw new ValidationException("Das Passwort muss zwischen 8 und 20 Zeichen lang sein!" + EINGABEHINWEIS);
    }

    String leerzeichen = " ";

    if (password.contains(leerzeichen)) {
      LOG.debug("Das Passwort eines Users darf keine Leerzeichen beinhlaten!");
      throw new ValidationException("Das Passwort darf keine Leerzeichen beinhlaten!" + EINGABEHINWEIS);
    }

    Pattern pruefenAufGrossbuchstaben = Pattern.compile("[A-Z]");
    Pattern pruefenAufKleinbuchstaben = Pattern.compile("[a-z]");
    Pattern pruefenAufZahl = Pattern.compile("[0-9]");


    if (!pruefenAufGrossbuchstaben.matcher(password).find()) {
      LOG.debug("Das Passwort eines Users muss mindestens einen Großbuchstaben beinhalten!");
      throw new ValidationException("Das Passwort muss mindestens einen Großbuchstaben beinhalten!" + EINGABEHINWEIS);
    }

    if (!pruefenAufKleinbuchstaben.matcher(password).find()) {
      LOG.debug("Das Passwort eines Users muss mindestens einen Kleinbuchstaben beinhalten!");
      throw new ValidationException("Das Passwort muss mindestens einen Kleinbuchstaben beinhalten!" + EINGABEHINWEIS);
    }

    if (!pruefenAufZahl.matcher(password).find()) {
      LOG.debug("Das Passwort eines Users muss mindestens eine Zahl beinhalten!");
      throw new ValidationException("Das Passwort muss mindestens eine Zahl beinhalten!" + EINGABEHINWEIS);

    }

    Pattern pruefenAufSonderzeichen = Pattern.compile("[*?!$%&]");

    if (!pruefenAufSonderzeichen.matcher(password).find()) {
      LOG.debug("Das Passwort eines Users muss mindestens eines dieser Sonderzeichen beinhalten: *?$%&.!");
      throw new ValidationException("Das Passwort muss mindestens eines dieser Sonderzeichen beinhalten: *?$%&.!" + EINGABEHINWEIS);
    }
  } 
  
  // Check image requirements
  public static void ImageValidation(MultipartFile imageFile){
        
    if(imageFile.isEmpty()) {
      LOG.debug("Die übergebene Datei \"{}\" ist leer!", imageFile.getOriginalFilename());
      throw new ValidationException("Es wurde keine Datei ausgewählt!");
    }
    
    String mimetype = imageFile.getContentType();
    String type = mimetype.split("/")[0];
    if(!type.equals("image")) {
      LOG.debug("Die übergebene Datei \"{}\" ist nicht in einem Bildformat!", imageFile.getOriginalFilename());
      throw new ValidationException("Es dürfen nur Bilder der hochgeladen werden!");
    }

    long size = imageFile.getSize();
    if(size > 10000000) {
      LOG.debug("Die übergebene Datei \"{}\" ist größer als 10MB!", imageFile.getOriginalFilename());
      throw new ValidationException("Dein Bild " + imageFile.getOriginalFilename() + "konnte nicht Hochgeladen werden, da es größer als 10MB ist!");
    }  
  }
}
