package pyk.qna.controller.activity;

import android.app.DialogFragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;
import pyk.qna.R;
import pyk.qna.controller.fragment.LoginDialog;
import pyk.qna.model.firebase.FirebaseHandler;
import pyk.qna.model.object.User;
import pyk.qna.view.adapter.ItemAdapter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
  FrameLayout frameLayout;
  BlurView    blurViewTop;
  BlurView    blurViewBottom;
  public static Drawable background;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
    frameLayout = (FrameLayout) findViewById(R.id.root);
    blurViewTop = (BlurView) findViewById(R.id.blurViewTop);
    blurViewBottom = (BlurView) findViewById(R.id.blurViewBottom);
  
    TextView bottomTV = (TextView) findViewById(R.id.lqa);
    bottomTV.setOnClickListener(this);
    
    
    
    ItemAdapter itemAdapter;
    
    itemAdapter = new ItemAdapter(this, frameLayout);
    RecyclerView recyclerView = (RecyclerView) this.findViewById(R.id.rv_list);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.setAdapter(itemAdapter);
    
    FirebaseHandler fb    = new FirebaseHandler();
    List<String>    empty = new ArrayList<String>();
    User user = new User("apple", "beetle", "1/1/1980 24:00", "2/2/2020 13:00", 2, 3,
                         empty, empty);
    fb.writeUser(true, user);
  
  
  
  
    FirebaseAuth auth = FirebaseAuth.getInstance();
  
    auth.createUserWithEmailAndPassword("pykennedy@gmail.com", "asdf1234")
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
          @Override public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful()) {
              Log.w("asdfasdfasdf", "createUserWithEmail:success", task.getException());
            } else {
              Log.w("asdfasdfasdf", "createUserWithEmail:failure", task.getException());
            }
          }
        });
    
    
    
    
    
    
    setupBlurView();
  }
  
  @Override public void onClick(View view) {
    DialogFragment dialogFragment = new LoginDialog();
    dialogFragment.show(getFragmentManager(), "LoginDialog");
    /*
    Dialog dialog = new Dialog(this);
    dialog.setContentView(R.layout.dialog_login);
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    dialog.show(); */
  }
  
  private void setupBlurView() {
    final float radius = 25f;
    
    //set background, if your root layout doesn't have one
    final Drawable windowBackground = getWindow().getDecorView().getBackground();
    background = windowBackground;
    
    blurViewTop.setupWith(frameLayout)
               .windowBackground(windowBackground)
               .blurAlgorithm(new RenderScriptBlur(this))
               .blurRadius(radius);
    blurViewBottom.setupWith(frameLayout)
                  .windowBackground(windowBackground)
                  .blurAlgorithm(new RenderScriptBlur(this))
                  .blurRadius(radius);
  }
  

}
