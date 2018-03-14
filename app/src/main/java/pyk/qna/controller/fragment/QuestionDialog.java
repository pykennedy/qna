package pyk.qna.controller.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import pyk.qna.R;
import pyk.qna.model.firebase.FirebaseHandler;

public class QuestionDialog extends DialogFragment implements View.OnClickListener{
  EditText question;
  TextView button;
  
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    Dialog              dialog;
    AlertDialog.Builder builder    = new AlertDialog.Builder(getActivity());
    LayoutInflater      inflater   = getActivity().getLayoutInflater();
    View                dialogView = inflater.inflate(R.layout.dialog_question, null);
    builder.setView(dialogView);
    dialog = builder.create();
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    
    question = (EditText) dialogView.findViewById(R.id.et_question);
    question.setImeOptions(EditorInfo.IME_ACTION_DONE);
    question.setRawInputType(InputType.TYPE_CLASS_TEXT);
    
    button = (TextView) dialogView.findViewById(R.id.tv_ask);
    button.setOnClickListener(this);
    
    return dialog;
  }
  
  @Override public void onClick(View view) {
    switch (view.getId()) {
      case R.id.tv_ask:
        FirebaseHandler.getFb().writeQuestion(question.getText().toString(), true);
        this.dismiss();
        break;
      default:
        break;
    }
  }
}
