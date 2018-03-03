package pyk.qna.controller;

public class Utility {
  public static String cleanEmail (String email) {
    return email.replace('@','a').replace('.','d');
  }
}
