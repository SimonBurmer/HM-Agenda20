package edu.hm.cs.katz.swt2.agenda.service;

import java.util.regex.Pattern;
import javax.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidationService {

  private static final Logger LOG = LoggerFactory.getLogger(ValidationService.class);


  // Check Topic title, shortDescription, longDescription requirements
  public static void topicValidation(String title, String shortDescription,
      String longDescription) {

    if (title.length() < 10) {
      LOG.debug("Titel von Topics müssen mindestens 10 Zeichen lang sein!");
      throw new ValidationException("Titel müssen mindestens 10 Zeichen lang sein!");
    }

    if (title.length() > 60) {
      LOG.debug("Titel von Topics dürfen nicht länger als 60 Zeichen sein!");
      throw new ValidationException("Maximale Länge von 60 Zeichen überschritten!");
    }
    topicValidation(shortDescription, longDescription);

  }

  // Check Topic shortDescription and longDescription requirements
  public static void topicValidation(String shortDescription, String longDescription) {

    if (shortDescription.length() < 100) {
      LOG.debug("Kurzbeschreibungen von Topics müssen mindestens 100 Zeichen lang sein!");
      throw new ValidationException("Kurzbeschreibungen müssen mindestens 100 Zeichen lang sein!");
    }

    if (shortDescription.length() > 200) {
      LOG.debug("Kurzbeschreibungen von Topics dürfen nicht länger als 200 Zeichen sein!");
      throw new ValidationException("Maximale Länge von 200 Zeichen überschritten!");
    }

    if (longDescription.length() < 200) {
      LOG.debug("Langbeschreibungen von Topics müssen mindestens 200 Zeichen lang sein!");
      throw new ValidationException("Langbeschreibungen müssen mindestens 200 Zeichen lang sein!");
    }
    if (longDescription.length() > 2000) {
      LOG.debug("Langbeschreibungen von Topics dürfen nicht länger als 2000 Zeichen sein!");
      throw new ValidationException("Maximale Länge von 2000 Zeichen überschritten!");
    }
  }

  // Check task title, taskshortDescription, tasklongDescription requirements
  public static void taskValidation(String titel, String taskShortDescription,
      String taskLongDescription) {

    if (titel.length() < 8) {
      LOG.debug("Titel von Tasks müssen mindestens 8 Zeichen lang sein!");
      throw new ValidationException("Titel müssen mindestens 8 Zeichen lang sein!");
    }
    if (titel.length() > 32) {
      LOG.debug("Titel von Tasks dürfen nicht länger als 32 Zeichen sein!");
      throw new ValidationException("Maximale Länge von 32 Zeichen überschritten!");
    }
    taskValidation(taskShortDescription, taskLongDescription);

  }

  // Check Task taskshortDescription and tasklongDescription requirements
  public static void taskValidation(String taskShortDescription, String taskLongDescription) {

    if (taskShortDescription.length() < 100) {
      LOG.debug("Kurzbeschreibungen von Tasks müssen mindestens 100 Zeichen lang sein!");
      throw new ValidationException("Kurzbeschreibungen müssen mindestens 100 Zeichen lang sein!");
    }
    if (taskShortDescription.length() > 200) {
      LOG.debug("Kurzbeschreibungen von Tasks dürfen nicht länger als 200 Zeichen sein!");
      throw new ValidationException("Maximale Länge von 200 Zeichen ueberschritten!");
    }

    if (taskLongDescription.length() < 200) {
      LOG.debug("Langbeschreibungen von Tasks müssen mindestens 200 Zeichen lang sein!");
      throw new ValidationException("Langbeschreibungen müssen mindestens 200 Zeichen lang sein!");
    }
    if (taskLongDescription.length() > 2000) {
      LOG.debug("Langbeschreibungen von Tasks dürfen nicht länger als 2000 Zeichen sein!");
      throw new ValidationException("Maximale Länge von 2000 Zeichen überschritten!");
    }
  }
  
  // Check User login, name and password requirements
  public static void userValidation(String login, String name, String password) {

    if (!login.equals(login.toLowerCase())) {
      LOG.debug("Name eines Users darf nur aus kleingeschriebenen Buchstaben bestehen. "
              + "Bitte passen Sie Ihre Eingabe an!");
      throw new ValidationException(
          "Name des Anwenders darf nur aus kleingeschriebenen Buchstaben bestehen. "
              + "Bitte passen Sie Ihre Eingabe an!");
    }

    if (login.length() < 4 || login.length() > 20) {
      LOG.debug("Name eines Users muss zwischen 4 und 20 Zeichen lang sein. "
              + "Bitte passen Sie Ihre Eingabe an!");
      throw new ValidationException(
          "Der Name muss zwischen 4 und 20 Zeichen lang sein. Bitte passen Sie Ihre Eingabe an!");
    }

    if (name.length() < 1 || name.length() > 32) {
      LOG.debug("Name eines Users muss zwischen 1 und 32 Zeichen lang sein."
          + "Bitte passen Sie Ihre Eingabe an!");
      throw new ValidationException(
          "Der Name muss zwischen 1 und 32 Zeichen lang sein. Bitte passen Sie Ihre Eingabe an!");
    }

    if (password.length() < 8 || password.length() > 20) {
      LOG.debug("Das Passwort eines Users muss zwischen 8 und 20 Zeichen lang sein. "
              + "Bitte passen Sie Ihre Eingabe an!");
      throw new ValidationException("Das Passwort muss zwischen 8 und 20 Zeichen lang sein. "
          + "Bitte passen Sie Ihre Eingabe an!");
    }

    String leerzeichen = " ";

    if (password.contains(leerzeichen)) {
      LOG.debug("Das Passwort eines Users darf keine Leerzeichen beinhlaten."
              + "Bitte passen Sie Ihre Eingabe an!");
      throw new ValidationException(
          "Das Passwort darf keine Leerzeichen beinhlaten. Bitte passen Sie Ihre Eingabe an!");
    }

    Pattern pruefenAufGrossbuchstaben = Pattern.compile("[A-Z]");
    Pattern pruefenAufKleinbuchstaben = Pattern.compile("[a-z]");
    Pattern pruefenAufZahl = Pattern.compile("[0-9]");


    if (!pruefenAufGrossbuchstaben.matcher(password).find()) {
      LOG.debug("Das Passwort eines Users muss mindestens einen Großbuchstaben beinhalten. "
              + "Bitte passen Sie Ihre Eingabe an!");
      throw new ValidationException(
          "Das Passwort muss mindestens einen Großbuchstaben beinhalten. "
              + "Bitte passen Sie Ihre Eingabe an!");
    }

    if (!pruefenAufKleinbuchstaben.matcher(password).find()) {
      LOG.debug("Das Passwort eines Users muss mindestens einen Kleinbuchstaben beinhalten. "
              + "Bitte passen Sie Ihre Eingabe an!");
      throw new ValidationException(
          "Das Passwort muss mindestens einen Kleinbuchstaben beinhalten. "
              + "Bitte passen Sie Ihre Eingabe an!");
    }

    if (!pruefenAufZahl.matcher(password).find()) {
      LOG.debug("Das Passwort eines Users muss mindestens eine Zahl beinhalten."
              + "Bitte passen Sie Ihre Eingabe an!");
      throw new ValidationException(
          "Das Passwort muss mindestens eine Zahl beinhalten. Bitte passen Sie Ihre Eingabe an!");

    }

    Pattern pruefenAufSonderzeichen = Pattern.compile("[*?!$%&]");

    if (!pruefenAufSonderzeichen.matcher(password).find()) {
      LOG.debug("Das Passwort eines Users muss mindestens eines dieser Sonderzeichen beinhalten: *?!$%&. "
              + "Bitte passen Sie Ihre Eingabe an!");
      throw new ValidationException(
          "Das Passwort muss mindestens eines dieser Sonderzeichen beinhalten: *?!$%&. "
              + "Bitte passen Sie Ihre Eingabe an!");
    }
  }
  
  
}
