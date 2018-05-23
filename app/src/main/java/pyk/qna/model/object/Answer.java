package pyk.qna.model.object;

import java.util.ArrayList;
import java.util.List;

import pyk.qna.model.firebase.FirebaseHandler;

public class Answer {
  private String       questionID;
  private String       username;
  private String       answerText;
  private String       postTime;
  private List<String> upvotes;
  
  public Answer() {}
  
  public Answer(String questionID, String answerText) {
    this.questionID = questionID;
    this.username = FirebaseHandler.getFb().getCurrentUsername();
    this.answerText = answerText;
    this.postTime = (String) android.text.format.DateFormat.format("yyyy-MM-dd HH:mm:ss", new java.util.Date());
    this.upvotes = new ArrayList<String>();
  }
  
  public String getQuestionID() {
    return questionID;
  }
  
  public String getUsername() {
    return username;
  }
  
  public String getAnswerText() {
    return answerText;
  }
  
  public String getPostTime() {
    return postTime;
  }
  
  public List<String> getUpvotes() {
    return upvotes;
  }
  
  public void initUpvotes() {
    upvotes = new ArrayList<>();
  }
}
