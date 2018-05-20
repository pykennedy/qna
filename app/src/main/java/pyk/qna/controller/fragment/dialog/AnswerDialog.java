package pyk.qna.controller.fragment.dialog;

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
import pyk.qna.model.object.Question;

public class AnswerDialog extends DialogFragment implements View.OnClickListener{
  EditText answer;
  TextView button;
  String   questionID;
  Question question;
  
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    Dialog              dialog;
    AlertDialog.Builder builder    = new AlertDialog.Builder(getActivity());
    LayoutInflater      inflater   = getActivity().getLayoutInflater();
    View                dialogView = inflater.inflate(R.layout.dialog_answer, null);
    builder.setView(dialogView);
    dialog = builder.create();
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
  
    answer = (EditText) dialogView.findViewById(R.id.et_answer);
    answer.setImeOptions(EditorInfo.IME_ACTION_DONE);
    answer.setRawInputType(InputType.TYPE_CLASS_TEXT);
    
    button = (TextView) dialogView.findViewById(R.id.tv_answer);
    button.setOnClickListener(this);
    
    return dialog;
  }
  
  @Override public void onClick(View view) {
    switch (view.getId()) {
      case R.id.tv_answer:
        FirebaseHandler.getFb().writeAnswer(questionID, answer.getText().toString(), question);
        this.dismiss();
        break;
      default:
        break;
    }
  }
  
  public void setQuestion(String questionID, Question question) {
    this.questionID = questionID;
    this.question = question;
  }
}
