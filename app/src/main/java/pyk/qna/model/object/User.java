package pyk.qna.model.object;

import java.util.ArrayList;
import java.util.List;

public class User {
  private String       username;
  private String       photo;
  private String       description;
  private List<String> questions;
  private List<String> answers;
  
  public User() {}
  
  public User(String username) {
    this.username = username;
    this.photo = null;
    this.description = null;
    this.questions = new ArrayList<String>();
    this.answers = new ArrayList<String>();
  }
  
  public User(String username, String photo, String description, List<String> questions,
              List<String> answers) {
    this.username = username;
    this.photo = photo;
    this.description = description;
    this.questions = questions;
    this.answers = answers;
  }
  
  public String getUsername()                    { return username; }
  
  public String getPhoto()                       { return photo; }
  
  public String getDescription()                 { return description; }
  
  public List<String> getQuestions()             { return questions; }
  
  public List<String> getAnswers()               { return answers; }
  
  public void setPhoto(String photo)             { this.photo = photo; }
  
  public void setDescription(String description) { this.description = description; }
  
  public void addQuestion(String questionID)     {
    if(questions == null) {
      questions = new ArrayList<String>();
    }
    questions.add(questionID);
  }
  
  public void addAnswer(String answerID)         {
    if(answers == null) {
      answers = new ArrayList<String>();
    }
    answers.add(answerID);
  }
  
}
