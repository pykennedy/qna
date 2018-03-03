package pyk.qna.model.firebase;

import android.support.annotation.NonNull;
import android.util.Log;

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

import pyk.qna.controller.Utility;
import pyk.qna.model.object.User;

public class FirebaseHandler {
  private static final FirebaseAuth      auth            = FirebaseAuth.getInstance();
  private static final DatabaseReference db              =
      FirebaseDatabase.getInstance().getReference();
  private static final FirebaseHandler   fb              = new FirebaseHandler();
  private              boolean           chainChecks     = false;
  private              String            currentUsername = null;
  
  public static FirebaseHandler getFb() {
    return fb;
  }
  
  public interface Delegate {
    void onLoginSuccess(String successType);
    
    void onLoginFailed(String errorType);
  }
  
  private WeakReference<Delegate> delegate;
  
  private Delegate getDelegate() {
    if (delegate == null) {
      return null;
    }
    return delegate.get();
  }
  
  public void setDelegate(Delegate delegate) {
    this.delegate = new WeakReference<Delegate>(delegate);
  }
  
  public void createUser(final String email, String password, final String username) {
    // if user is authenticated, but failed to add username to db
    if (auth.getCurrentUser() != null) {
      writeNewUser(email, username);
      // else if user needs to create account
    } else {
      auth.createUserWithEmailAndPassword(email, password)
          .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override public void onComplete(@NonNull Task<AuthResult> task) {
              // if user created account, then add username to db
              if (task.isSuccessful()) {
                Log.e("asdfasdfasdf", "createUserWithEmail:success", task.getException());
                writeNewUser(email, username);
                // if user failed to add account, tell user to retry
              } else {
                Log.e("asdfasdfasdf", "createUserWithEmail:failure", task.getException());
                getDelegate().onLoginFailed("Account creation failed. Try again");
              }
            }
          });
    }
  }
  
  private void writeNewUser(final String email, final String username) {
    setPrevPassed(false);
    db.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
      @Override public void onDataChange(DataSnapshot dataSnapshot) {
        for (DataSnapshot child : dataSnapshot.getChildren()) {
          // if username already exists, tell user to make a different one
          if (child.getValue() != null && child.getValue().toString().equals(username)) {
            getDelegate().onLoginFailed("Username already exists. Try again.");
            return;
          }
        }
        User user = new User(username);
        db.child("user").child(user.getUsername()).setValue(user,
                                                            new DatabaseReference.CompletionListener() {
                                                              @Override
                                                              public void onComplete(
                                                                  DatabaseError databaseError,
                                                                  DatabaseReference databaseReference) {
                                                                // if add user record passed, then then track progress
                                                                if (databaseError == null) {
                                                                  setPrevPassed(true);
                                                                  // if add user failed, then fail next check even if successful
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
                                                                          // if add email to username mapping passed and previous passed, then inform user of successful creation
                                                                          if (databaseError ==
                                                                              null &&
                                                                              prevPassed()) {
                                                                            getUsernameFromEmail(email);
                                                                          } else {
                                                                            // if previous or mapping failed, tell user to try again
                                                                            getDelegate()
                                                                                .onLoginFailed(
                                                                                    "Account creation failed. Try again.");
                                                                            // delete previous progress for consistency
                                                                            if (prevPassed()) {
                                                                              db.child("user/" + username).removeValue();
                                                                            }
                                                                          }
                                                                        }
                                                                      });
      }
      
      @Override public void onCancelled(DatabaseError databaseError) {}
    });
  }
  
  public void writeUser(User user) {}
  
  public void writeQuestion()      {}
  
  public void writeAnswer()        {}
  
  private void getUsernameFromEmail(final String email) {
    db.child("account").addListenerForSingleValueEvent(new ValueEventListener() {
      @Override public void onDataChange(DataSnapshot dataSnapshot) {
        for (DataSnapshot child : dataSnapshot.getChildren()) {
          if (child.getKey().equals(Utility.cleanEmail(email))) {
            try {
              setLoggedInUser(child.getValue().toString());
            } catch (NullPointerException e) {
              setLoggedInUser(null);
            }
          } else {
          }
        }
      }
      
      @Override public void onCancelled(DatabaseError databaseError) {}
    });
  }
  
  public void loginUser(final String email, String password) {
    // if user is already logged in, then let user know what they're logged in as
    if (auth.getCurrentUser() != null && auth.getCurrentUser().getEmail().equals(email)) {
      getUsernameFromEmail(email);
      // else login user and let them know what they're logged in as
    } else {
      auth.signOut();
      auth.signInWithEmailAndPassword(email, password)
          .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override public void onComplete(@NonNull Task<AuthResult> task) {
              // if login successful, pull username and show user
              if (task.isSuccessful()) {
                getUsernameFromEmail(email);
                // else have user try again
              } else {
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
  
  public String getCurrentUsername() {
    return currentUsername;
  }
  
  private void setLoggedInUser(String username) {
    currentUsername = username;
    if (currentUsername != null) {
      getDelegate().onLoginSuccess("Logged in as " + currentUsername);
    }
  }
}
