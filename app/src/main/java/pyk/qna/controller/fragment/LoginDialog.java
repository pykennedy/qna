package pyk.qna.controller.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import pyk.qna.R;
import pyk.qna.model.firebase.FirebaseHandler;

public class LoginDialog extends DialogFragment implements View.OnClickListener {
  View     loginBoxSmall;
  View     loginBoxBig;
  ProgressBar progressBar;
  EditText email;
  EditText password;
  EditText username;
  TextView button;
  TextView change;
  boolean  isCreate;
  
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    Dialog              dialog;
    AlertDialog.Builder builder    = new AlertDialog.Builder(getActivity());
    LayoutInflater      inflater   = getActivity().getLayoutInflater();
    View                dialogView = inflater.inflate(R.layout.dialog_login, null);
    builder.setView(dialogView);
    dialog = builder.create();
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    
    loginBoxSmall = dialogView.findViewById(R.id.login_box_small);
    loginBoxBig = dialogView.findViewById(R.id.login_box_big);
    progressBar = dialogView.findViewById(R.id.login_pb);
    email = (EditText) dialogView.findViewById(R.id.et_login_email);
    password = (EditText) dialogView.findViewById(R.id.et_login_pw);
    username = (EditText) dialogView.findViewById(R.id.et_login_username);
    button = (TextView) dialogView.findViewById(R.id.tv_login_confirm);
    change = (TextView) dialogView.findViewById(R.id.tv_login_switchToCreate);
    isCreate = false;
    
    button.setOnClickListener(this);
    change.setOnClickListener(this);
    
    return dialog;
  }
  
  @Override
  public void onDismiss(final DialogInterface dialogInterface) {
    super.onDismiss(dialogInterface);
    stopSpinning();
  }
  
  @Override public void onClick(View view) {
    switch (view.getId()) {
      case R.id.tv_login_confirm:
        if (isCreate) {
          FirebaseHandler.getFb().createUser(email.getText().toString(),
                                             password.getText().toString(),
                                             username.getText().toString());
          startSpinning();
        } else {
          FirebaseHandler.getFb().loginUser(email.getText().toString(),
                                             password.getText().toString());
          startSpinning();
        }
        break;
      case R.id.tv_login_switchToCreate:
        change.setText((isCreate) ? "create an account" : "return to login");
        button.setText((isCreate) ? "login" : "create");
        username.setVisibility((isCreate) ? View.GONE : View.VISIBLE);
        loginBoxSmall.setVisibility((isCreate) ? View.VISIBLE : View.GONE);
        loginBoxBig.setVisibility((isCreate) ? View.GONE : View.VISIBLE);
        isCreate = !isCreate;
        break;
      default:
        break;
    }
  }
  
  private void startSpinning() {
    email.setVisibility(View.INVISIBLE);
    password.setVisibility(View.INVISIBLE);
    username.setVisibility((isCreate) ? View.INVISIBLE : View.GONE);
    button.setVisibility(View.INVISIBLE);
    change.setVisibility(View.INVISIBLE);
    progressBar.setVisibility(View.VISIBLE);
  }
  
  public void stopSpinning() {
    email.setVisibility(View.VISIBLE);
    password.setVisibility(View.VISIBLE);
    username.setVisibility((isCreate) ? View.VISIBLE : View.GONE);
    button.setVisibility(View.VISIBLE);
    change.setVisibility(View.VISIBLE);
    progressBar.setVisibility(View.INVISIBLE);
  }
}
