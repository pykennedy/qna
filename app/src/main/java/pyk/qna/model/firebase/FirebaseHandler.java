package pyk.qna.model.firebase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.ref.WeakReference;
import java.util.concurrent.Executor;

import pyk.qna.model.object.User;

public class FirebaseHandler {
  private static final FirebaseAuth      auth = FirebaseAuth.getInstance();
  private static final DatabaseReference db   = FirebaseDatabase.getInstance().getReference();
  private static final FirebaseHandler fb = new FirebaseHandler();
  
  public static FirebaseHandler getFb() {
    return fb;
  }
  
  public static interface Delegate {
    public void onLoginSuccess(String successType);
    
    public void onLoginFailed(String errorType);
  }
  
  private WeakReference<Delegate> delegate;
  
  public Delegate getDelegate() {
    if (delegate == null) {
      return null;
    }
    return delegate.get();
  }
  
  public void setDelegate(Delegate delegate) {
    this.delegate = new WeakReference<Delegate>(delegate);
  }
  
  public void createUser(final String email, String password, final String username) {
    if (auth.getCurrentUser() != null) {
      writeNewUser(email, username);
    } else {
      auth.createUserWithEmailAndPassword(email, password)
          .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
            @Override public void onComplete(@NonNull Task<AuthResult> task) {
              if (task.isSuccessful()) {
                Log.w("asdfasdfasdf", "createUserWithEmail:success", task.getException());
                writeNewUser(email, username);
              } else {
                Log.w("asdfasdfasdf", "createUserWithEmail:failure", task.getException());
                getDelegate().onLoginFailed("Account creation failed. Try again");
              }
            }
          });
    }
  }
  
  private void writeNewUser(String email, String username) {
    if (readUser(username) != null) {
      getDelegate().onLoginFailed("Username already exists. Try again.");
    } else {
      User user = new User(username);
      db.child("user").child(user.getUsername()).setValue(user);
      db.child("account").child(email).setValue(username);
    }
  }
  
  public void writeUser(User user) {}
  
  public void writeQuestion()      {}
  
  public void writeAnswer()        {}
  
  private String getUsernameFromEmail(String email) {
    // TODO: call onLoginSuccess
    return "";
  }
  
  public void loginUser(final String email, String password) {
    if (auth.getCurrentUser() != null) {
    } else {
      auth.signInWithEmailAndPassword(email, password)
          .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
            @Override public void onComplete(@NonNull Task<AuthResult> task) {
              if (task.isSuccessful()) {
                Log.w("asdfasdfasdf", "signInWithEmail:success", task.getException());
                getUsernameFromEmail(email);
              } else {
                Log.w("asdfasdfasdf", "signInWithEmail:failure", task.getException());
                getDelegate().onLoginFailed("Login failed. Try again");
              }
            }
          });
    }
  }
  
  public User readUser(String username) {return new User();}
  
  public void readQuestion()            {}
  
  public void readAnswer()              {}
  
}
