package pyk.qna.model.object;

import java.util.ArrayList;
import java.util.List;

public class User {
  String       username;
  String       join;
  String       lastActive;
  long         numberOfQ;
  long         numberOfA;
  List<String> questions;
  List<String> answers;
  
  public User() {}
  
  public User(String username) {
    this.username = username;
    this.join = null;
    this.lastActive = null;
    this.numberOfQ = 0;
    this.numberOfA = 0;
    this.questions = new ArrayList<String>();
    this.answers = new ArrayList<String>();
  }
  
  public User(String username, String join, String lastActive, long numberOfQ,
              long numberOfA, List<String> questions, List<String> answers) {
    this.username = username;
    this.join = join;
    this.lastActive = lastActive;
    this.numberOfQ = numberOfQ;
    this.numberOfA = numberOfA;
    this.questions = questions;
    this.answers = answers;
  }
  
  public String getUsername() {
    return username;
  }
  
  public String getJoin() {
    return join;
  }
  
  public String getLastActive() {
    return lastActive;
  }
  
  public long getNumberOfQ() {
    return numberOfQ;
  }
  
  public long getNumberOfA() {
    return numberOfA;
  }
  
  public List<String> getQuestions() {
    return questions;
  }
  
  public List<String> getAnswers() {
    return answers;
  }
}
