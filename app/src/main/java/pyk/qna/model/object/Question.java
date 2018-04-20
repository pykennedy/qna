package pyk.qna.model.object;

import java.util.ArrayList;
import java.util.List;

import pyk.qna.model.firebase.FirebaseHandler;

public class Question {
  private String username;
  private String questionText;
  private String postTime;
  private List<String> answers;
  
  public Question() {}
  
  public Question(String questionText) {
    this.username = FirebaseHandler.getFb().getCurrentUsername();
    this.questionText = questionText;
    this.postTime = (String) android.text.format.DateFormat.format("yyyy-MM-dd HH:mm:ss", new java.util.Date());
    answers = new ArrayList<String>();
  }
  
  public void setAnswer(String answerID) {
    answers.add(answerID);
  }
  
  public String getUsername() {
    return username;
  }
  
  public String getQuestionText() {
    return questionText;
  }
  
  public String getPostTime() {
    return postTime;
  }
  
  public List<String> getAnswers() {
    return answers;
  }
}
