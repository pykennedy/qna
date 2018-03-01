package pyk.qna.controller.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import pyk.qna.R;
import pyk.qna.model.firebase.FirebaseHandler;

public class LoginDialog extends DialogFragment implements View.OnClickListener {
  EditText email;
  EditText password;
  TextView button;
  TextView change;
  boolean isCreate;
  
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    Dialog dialog;
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    LayoutInflater      inflater = getActivity().getLayoutInflater();
    View dialogView = inflater.inflate(R.layout.dialog_login, null);
    builder.setView(dialogView);
    dialog = builder.create();
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    
    email = (EditText) dialogView.findViewById(R.id.et_login_email);
    password = (EditText) dialogView.findViewById(R.id.et_login_pw);
    button = (TextView) dialogView.findViewById(R.id.tv_login_confirm);
    change = (TextView) dialogView.findViewById(R.id.tv_login_switchToCreate);
    isCreate = false;
    
    button.setOnClickListener(this);
    change.setOnClickListener(this);
    
    return dialog;
  }
  
  @Override public void onClick(View view) {
    switch (view.getId()) {
      case R.id.tv_login_confirm:
        FirebaseHandler.getFb().loginUser(email.getText().toString(), password.getText().toString());
        break;
      case R.id.tv_login_switchToCreate:
        change.setText((isCreate) ? "create an account" : "return to login");
        button.setText((isCreate) ? "login" : "create");
        isCreate = !isCreate;
        break;
      default:
        break;
    }
  }
}
