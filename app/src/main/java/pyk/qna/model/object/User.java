package pyk.qna.model.object;

import java.util.ArrayList;
import java.util.List;

public class User {
  private String       username;
  private String       photo;
  private String       description;
  private long         numberOfQ;
  private long         numberOfA;
  private List<String> questions;
  private List<String> answers;
  
  public User() {}
  
  public User(String username) {
    this.username = username;
    this.photo = null;
    this.description = null;
    this.numberOfQ = 0;
    this.numberOfA = 0;
    this.questions = new ArrayList<String>();
    this.answers = new ArrayList<String>();
  }
  
  public User(String username, String photo, String description, long numberOfQ,
              long numberOfA, List<String> questions, List<String> answers) {
    this.username = username;
    this.photo = photo;
    this.description = description;
    this.numberOfQ = numberOfQ;
    this.numberOfA = numberOfA;
    this.questions = questions;
    this.answers = answers;
  }
  
  public String getUsername()                      { return username; }
  
  public String getPhoto()                         { return photo; }
  
  public String getDescription()                   { return description; }
  
  public long getNumberOfQ()                       { return numberOfQ; }
  
  public long getNumberOfA()                       { return numberOfA; }
  
  public List<String> getQuestions()               { return questions; }
  
  public List<String> getAnswers()                 { return answers; }
  
  public void setPhoto(String photo)               { this.photo = photo; }
  
  public void setDescription(String description)   { this.description = description; }
  
  public void setNumberOfQ(long numberOfQ)         { this.numberOfQ = numberOfQ; }
  
  public void setNumberOfA(long numberOfA)         { this.numberOfA = numberOfA; }
  
  public void setQuestions(List<String> questions) { this.questions = questions; }
  
  public void setAnswers(List<String> answers)     { this.answers = answers; }
  
}
