package pyk.qna.model.firebase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.Executor;

import pyk.qna.model.object.User;

public class FirebaseHandler {
  private FirebaseAuth      auth;
  private DatabaseReference db;
  
  public FirebaseHandler() {
    auth = FirebaseAuth.getInstance();
    db = FirebaseDatabase.getInstance().getReference();
  }
  
  public void createUser(String email, String password) {
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
      @Override public void onComplete(@NonNull Task<AuthResult> task) {
        if(task.isSuccessful()) {
          Log.w("asdfasdfasdf", "createUserWithEmail:success", task.getException());
        } else {
          Log.w("asdfasdfasdf", "createUserWithEmail:failure", task.getException());
        }
      }
    });
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
