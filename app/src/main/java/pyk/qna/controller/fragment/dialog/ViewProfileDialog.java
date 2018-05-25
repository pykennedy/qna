package pyk.qna.controller.fragment.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import pyk.qna.R;
import pyk.qna.controller.Utility;
import pyk.qna.model.firebase.FirebaseHandler;
import pyk.qna.model.object.Answer;
import pyk.qna.model.object.Question;
import pyk.qna.model.object.User;

// TODO: clicking on the username signs out the user and opens the login dialog
public class ViewProfileDialog extends DialogFragment
    implements FirebaseHandler.Delegate {
  User user = null;
  private Bitmap imageBM;
  TextView        username;
  CircleImageView circleImageView;
  TextView        description;
  TextView        qCount;
  TextView        aCount;
  ProgressBar     progressBar;
  
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    Dialog              dialog;
    AlertDialog.Builder builder    = new AlertDialog.Builder(getActivity());
    LayoutInflater      inflater   = getActivity().getLayoutInflater();
    View                dialogView = inflater.inflate(R.layout.dialog_view_profile, null);
    builder.setView(dialogView);
    dialog = builder.create();
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    
    progressBar = dialogView.findViewById(R.id.view_pb);
    username = dialogView.findViewById(R.id.tv_view_username);
    circleImageView = dialogView.findViewById(R.id.civ_view_image);
    description = dialogView.findViewById(R.id.et_view_description);
    qCount = dialogView.findViewById(R.id.tv_view_questions);
    aCount = dialogView.findViewById(R.id.tv_view_answers);
    
    return dialog;
  }
  
  public void populateUserDetails(String username) {
    FirebaseHandler fb = FirebaseHandler.getFb();
    fb.setDelegate(this);
    fb.readUser(username, false);
  }
  
  @Override public void onLoginSuccess(String successType) {}
  
  @Override public void onLoginFailed(String errorType)    {}
  
  @Override public void onReadUserSuccess(User user) {
    this.user = user;
    progressBar.setVisibility(View.GONE);
    username.setVisibility(View.VISIBLE);
    circleImageView.setVisibility(View.VISIBLE);
    description.setVisibility(View.VISIBLE);
    qCount.setVisibility(View.VISIBLE);
    aCount.setVisibility(View.VISIBLE);
    
    username.setText(user.getUsername());
    circleImageView.setImageBitmap(
        (user.getPhoto() == null) ? Utility.getBitmapFromDrawable(R.drawable.emptyimage)
                                  : Utility.base64ToBitmap(user.getPhoto()));
    description.setText(user.getDescription());
    qCount.setText((user.getQuestions() != null) ? user.getQuestions().size() + " q" : "0 q");
    aCount.setText((user.getAnswers() != null) ? user.getAnswers().size() + " a" : "0 a");
  }
  
  @Override public void onReadQuestionSuccess(Question question, boolean isList) {}
  
  @Override public void onReadAnswerSuccess(Answer result)                       {}
}
