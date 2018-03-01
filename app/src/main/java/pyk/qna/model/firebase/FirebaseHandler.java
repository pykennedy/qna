package pyk.qna.model.firebase;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.ref.WeakReference;

import pyk.qna.App;
import pyk.qna.controller.Utility;
import pyk.qna.model.object.User;

public class FirebaseHandler {
  private static final FirebaseAuth      auth        = FirebaseAuth.getInstance();
  private static final DatabaseReference db          =
      FirebaseDatabase.getInstance().getReference();
  private static final FirebaseHandler   fb          = new FirebaseHandler();
  private              boolean           chainChecks = false;
  
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
          .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override public void onComplete(@NonNull Task<AuthResult> task) {
              if (task.isSuccessful()) {
                Log.e("asdfasdfasdf", "createUserWithEmail:success", task.getException());
                writeNewUser(email, username);
              } else {
                Log.e("asdfasdfasdf", "createUserWithEmail:failure", task.getException());
                getDelegate().onLoginFailed("Account creation failed. Try again");
              }
            }
          });
    }
  }
  
  private void writeNewUser(String email, String username) {
    setPrevPassed(false);
    if (getUsernameFromEmail(Utility.cleanEmail(email)) == null) { // TODO: read username first, then attempt creation
      getDelegate().onLoginFailed("Username already exists. Try again.");
    } else {
      User user = new User(username);
      db.child("user").child(user.getUsername()).setValue(user,
                                                          new DatabaseReference.CompletionListener() {
                                                            @Override
                                                            public void onComplete(
                                                                DatabaseError databaseError,
                                                                DatabaseReference databaseReference) {
                                                              if (databaseError == null) {
                                                                setPrevPassed(true);
                                                              } else {
                                                                setPrevPassed(false);
                                                              }
                                                            }
                                                          });
      db.child("account").child(Utility.cleanEmail(email)).setValue(username,
                                                                    new DatabaseReference.CompletionListener() {
                                                                      @Override
                                                                      public void onComplete(
                                                                          DatabaseError databaseError,
                                                                          DatabaseReference databaseReference) {
                                                                        if (databaseError == null) {
                                                                          getDelegate()
                                                                              .onLoginSuccess(
                                                                                  "Account created");
                                                                        } else {
                                                                          getDelegate()
                                                                              .onLoginSuccess(
                                                                                  "Account creation failed. Try again.");
                                                                        }
                                                                      }
                                                                    });
    }
  }
  
  public void writeUser(User user) {}
  
  public void writeQuestion()      {}
  
  public void writeAnswer()        {}
  
  private String getUsernameFromEmail(String email) {
    // TODO: call onLoginSuccess
    db.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override public void onDataChange(DataSnapshot dataSnapshot) {
        for (DataSnapshot child : dataSnapshot.getChildren()) {
          Toast.makeText(App.get(), child.getValue().toString(), Toast.LENGTH_LONG).show();
        }
      }
      
      @Override public void onCancelled(DatabaseError databaseError) {
      
      }
    });
    return "";
  }
  
  public void loginUser(final String email, String password) {
    auth.getCurrentUser().delete();
    if (auth.getCurrentUser() != null) {
      Toast.makeText(App.get(), "loginUser exists", Toast.LENGTH_SHORT).show();
    } else {
      auth.signInWithEmailAndPassword(email, password)
          .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override public void onComplete(@NonNull Task<AuthResult> task) {
              if (task.isSuccessful()) {
                Log.e("asdfasdfasdf", "signInWithEmail:success", task.getException());
                getUsernameFromEmail(email);
              } else {
                Log.e("asdfasdfasdf", "signInWithEmail:failure", task.getException());
                getDelegate().onLoginFailed("Login failed. Try again");
              }
            }
          });
    }
  }
  
  public User readUser(String username) {return new User();}
  
  public void readQuestion()            {}
  
  public void readAnswer()              {}
  
  
  private boolean prevPassed() {
    return chainChecks;
  }
  
  private void setPrevPassed(boolean bool) {
    chainChecks = bool;
  }
}
