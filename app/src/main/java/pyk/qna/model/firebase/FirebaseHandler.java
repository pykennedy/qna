package pyk.qna.model.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import pyk.qna.model.object.User;

public class FirebaseHandler {
  private DatabaseReference db;
  
  public FirebaseHandler() {
    db = FirebaseDatabase.getInstance().getReference();
  }
  
  public boolean writeUser(boolean isNewUser, User user) {
    db.child("user").child(user.getUsername()).setValue(user);
    return true;
  }
  
  public void writeQuestion() {}
  
  public void writeAnswer() {}
  
  
  
  public void readUser() {}
  
  public void readQuestion() {}
  
  public void readAnswer() {}
  
}
