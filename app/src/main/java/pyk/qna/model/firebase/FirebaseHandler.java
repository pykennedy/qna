package pyk.qna.model.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import pyk.qna.model.object.User;

public class FirebaseHandler {
  private FirebaseAuth      auth;
  private DatabaseReference db;
  
  public FirebaseHandler() {
    auth = FirebaseAuth.getInstance();
    db = FirebaseDatabase.getInstance().getReference();
  }
  
  public void createUser(String email, String password) {
    auth.createUserWithEmailAndPassword(email, password);
  }
  
  public void loginUser() {}
  
  public boolean writeUser(boolean isNewUser, User user) {
    db.child("user").child(user.getUsername()).setValue(user);
    return true;
  }
  
  public void writeQuestion() {}
  
  public void writeAnswer()   {}
  
  
  public void readUser()      {}
  
  public void readQuestion()  {}
  
  public void readAnswer()    {}
  
}
