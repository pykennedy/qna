package pyk.qna.controller.fragment.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import pyk.qna.App;
import pyk.qna.R;
import pyk.qna.controller.Utility;
import pyk.qna.model.firebase.FirebaseHandler;
import pyk.qna.model.object.Question;
import pyk.qna.model.object.User;

import static android.app.Activity.RESULT_OK;

// TODO: clicking on the username signs out the user and opens the login dialog
public class EditProfileDialog extends DialogFragment
    implements View.OnClickListener, FirebaseHandler.Delegate {
  User user = null;
  private Bitmap imageBM;
  TextView        username;
  CircleImageView circleImageView;
  EditText        description;
  ProgressBar     progressBar;
  
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    Dialog              dialog;
    AlertDialog.Builder builder    = new AlertDialog.Builder(getActivity());
    LayoutInflater      inflater   = getActivity().getLayoutInflater();
    View                dialogView = inflater.inflate(R.layout.dialog_edit_profile, null);
    builder.setView(dialogView);
    dialog = builder.create();
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    
    progressBar = dialogView.findViewById(R.id.login_pb);
    username = dialogView.findViewById(R.id.tv_edit_username);
    circleImageView = dialogView.findViewById(R.id.civ_edit_image);
    description = dialogView.findViewById(R.id.et_edit_description);
    description.setImeOptions(EditorInfo.IME_ACTION_DONE);
    description.setRawInputType(InputType.TYPE_CLASS_TEXT);
    
    circleImageView.setOnClickListener(this);
    
    FirebaseHandler fb = FirebaseHandler.getFb();
    fb.setDelegate(this);
    fb.readUser(fb.getCurrentUsername());
    
    return dialog;
  }
  
  @Override public void onClick(View view) {
    switch (view.getId()) {
      case R.id.civ_edit_image:
        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                                       android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        startActivityForResult(pickIntent, 1);
        break;
      default:
        break;
    }
  }
  
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode == RESULT_OK) {
      Uri imageUri = data.getData();
      if (imageUri.toString().contains("image")) {
        try {
          imageBM = MediaStore.Images.Media.getBitmap(App.get().getContentResolver(),
                                                      imageUri);
          imageBM = Utility.scaleDownBitmap(imageBM);
          
          imageBM = Utility.rotateBitmapIfNeeded(imageUri, imageBM);
          circleImageView.setImageBitmap(imageBM);
        } catch (IOException e) {
          Toast.makeText(App.get(), "Load image failed, try again.", Toast.LENGTH_SHORT).show();
        }
        
      } else if (imageUri.toString().contains("video")) {
        Toast.makeText(App.get(), "You must select an image", Toast.LENGTH_SHORT).show();
      }
    }
  }
  
  @Override
  public void onDismiss(final DialogInterface dialogInterface) {
    if (user != null) {
      user.setPhoto(Utility.bitmapToBase64(((BitmapDrawable)circleImageView.getDrawable()).getBitmap()));
      user.setDescription(description.getText().toString());
      FirebaseHandler fb = FirebaseHandler.getFb();
      fb.writeUser(user);
    }
    super.onDismiss(dialogInterface);
  }
  
  @Override public void onLoginSuccess(String successType) {}
  
  @Override public void onLoginFailed(String errorType)    {}
  
  @Override public void onReadUserSuccess(User user) {
    this.user = user;
    progressBar.setVisibility(View.GONE);
    username.setVisibility(View.VISIBLE);
    circleImageView.setVisibility(View.VISIBLE);
    description.setVisibility(View.VISIBLE);
    
    username.setText(user.getUsername());
    circleImageView.setImageBitmap(
        (user.getPhoto() == null) ? Utility.getBitmapFromDrawable(R.drawable.emptyimage)
                                  : Utility.base64ToBitmap(user.getPhoto()));
    description.setText(user.getDescription());
  }
  
  @Override public void onReadQuestionSuccess(Question question, boolean isList) {}
  
  @Override public void onReadAnswerSuccess(List<String> result)                 {}
}
