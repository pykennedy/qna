package pyk.qna.controller.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;
import pyk.qna.R;
import pyk.qna.controller.Utility;
import pyk.qna.controller.fragment.EditProfileDialog;
import pyk.qna.controller.fragment.LoginDialog;
import pyk.qna.model.firebase.FirebaseHandler;
import pyk.qna.model.object.User;
import pyk.qna.view.adapter.ItemAdapter;

public class MainActivity extends AppCompatActivity
    implements View.OnClickListener, FirebaseHandler.Delegate {
  FrameLayout frameLayout;
  BlurView    blurViewTop;
  BlurView    blurViewBottom;
  
  public static Drawable          background;
  private       LoginDialog       loginDialogFragment;
  private       EditProfileDialog editProfileDialogFragment;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
    frameLayout = (FrameLayout) findViewById(R.id.root);
    blurViewTop = (BlurView) findViewById(R.id.blurViewTop);
    blurViewBottom = (BlurView) findViewById(R.id.blurViewBottom);
    
    CircleImageView image = (CircleImageView) findViewById(R.id.actionbar_image);
    image.setOnClickListener(this);
    
    TextView bottomTV = (TextView) findViewById(R.id.lqa);
    bottomTV.setOnClickListener(this);
    
    FirebaseHandler.getFb().setDelegate(this);
    
    ItemAdapter itemAdapter;
    
    itemAdapter = new ItemAdapter(this, frameLayout);
    RecyclerView recyclerView = (RecyclerView) this.findViewById(R.id.rv_list);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.setAdapter(itemAdapter);
    
    setupBlurView();
  }
  
  @Override public void onClick(View view) {
    switch (view.getId()) {
      case R.id.lqa:
        loginDialogFragment = new LoginDialog();
        loginDialogFragment.show(getFragmentManager(), "LoginDialog");
        break;
      case R.id.actionbar_image:
        Utility.handlePermission(this);
        editProfileDialogFragment = new EditProfileDialog();
        editProfileDialogFragment.show(getFragmentManager(), "EditProfileDialog");
        break;
      default:
        break;
    }
    
  }
  
  private void setupBlurView() {
    final float radius = 25f;
    
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
  
  
  @Override public void onLoginSuccess(String successType) {
    Toast.makeText(this, successType, Toast.LENGTH_SHORT).show();
    loginDialogFragment.dismiss();
  }
  
  @Override public void onLoginFailed(String errorType) {
    Toast.makeText(this, errorType, Toast.LENGTH_SHORT).show();
    loginDialogFragment.stopSpinning();
  }
  
  @Override public void onReadUserSuccess(User user)               {}
  
  @Override public void onReadQuestionSuccess(List<String> result) {}
  
  @Override public void onReadAnswerSuccess(List<String> result)   {}
}
