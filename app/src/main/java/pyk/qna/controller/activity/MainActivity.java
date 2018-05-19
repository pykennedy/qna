package pyk.qna.controller.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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
import pyk.qna.controller.fragment.HomeFragment;
import pyk.qna.controller.fragment.QuestionFragment;
import pyk.qna.controller.fragment.dialog.EditProfileDialog;
import pyk.qna.controller.fragment.dialog.LoginDialog;
import pyk.qna.controller.fragment.dialog.QuestionDialog;
import pyk.qna.model.firebase.FirebaseHandler;
import pyk.qna.model.object.Question;
import pyk.qna.model.object.User;

public class MainActivity extends FragmentActivity
    implements View.OnClickListener, FirebaseHandler.Delegate {
  FrameLayout frameLayout;
  BlurView    blurViewTop;
  BlurView    blurViewBottom;
  TextView bottomTV;
  QuestionFragment questionFragment;
  
  public static Drawable          background;
  public static FrameLayout       sFrameLayout;
  private       LoginDialog       loginDialogFragment;
  private       EditProfileDialog editProfileDialogFragment;
  private       QuestionDialog    questionDialogFragment;
  private       ViewPager         pager;
  private       PagerAdapter      pagerAdapter;
  
  private Bundle bundle;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
    frameLayout = (FrameLayout) findViewById(R.id.root);
    sFrameLayout = frameLayout;
    blurViewTop = (BlurView) findViewById(R.id.blurViewTop);
    blurViewBottom = (BlurView) findViewById(R.id.blurViewBottom);
    
    CircleImageView image = (CircleImageView) findViewById(R.id.actionbar_image);
    image.setOnClickListener(this);
    
    bottomTV = (TextView) findViewById(R.id.lqa);
    bottomTV.setOnClickListener(this);
    
    FirebaseHandler.getFb().setDelegate(this);
    
    pager = (ViewPager) findViewById(R.id.vp_activity);
    pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
    pager.setAdapter(pagerAdapter);
    
    setupBlurView();
  }
  
  @Override public void onClick(View view) {
    switch (view.getId()) {
      case R.id.lqa:
        if (bottomTV.getText().toString().equals("log in")) {
          loginDialogFragment = new LoginDialog();
          loginDialogFragment.show(getFragmentManager(), "LoginDialog");
        } else if (bottomTV.getText().toString().equals("question")) {
          questionDialogFragment = new QuestionDialog();
          questionDialogFragment.show(getFragmentManager(), "QuestionDialog");
        } else if (bottomTV.getText().toString().equals("answer")) {
        
        }
        break;
      case R.id.actionbar_image:
        if (FirebaseHandler.getFb().getCurrentUsername() != null) {
          Utility.handlePermission(this);
          editProfileDialogFragment = new EditProfileDialog();
          editProfileDialogFragment.show(getFragmentManager(), "EditProfileDialog");
        } else {
          loginDialogFragment = new LoginDialog();
          loginDialogFragment.show(getFragmentManager(), "LoginDialog");
        }
        break;
      default:
        break;
    }
  }
  
  public void switchToQuestion(String questionText, String questionID) {
    questionFragment.updateQuestion(questionText, questionID);
    pager.setCurrentItem(1);
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
    bottomTV.setText("question");
    FirebaseHandler.getFb().readAllQuestions();
  }
  
  @Override public void onLoginFailed(String errorType) {
    Toast.makeText(this, errorType, Toast.LENGTH_SHORT).show();
    loginDialogFragment.stopSpinning();
  }
  
  @Override public void onReadUserSuccess(User user)                             {}
  
  @Override public void onReadQuestionSuccess(Question question, boolean isList) {}
  
  @Override public void onReadAnswerSuccess(List<String> result)                 {}
  
  private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
    public ScreenSlidePagerAdapter(FragmentManager fm) {
      super(fm);
    }
    
    @Override
    public android.support.v4.app.Fragment getItem(int position) {
      switch (position) {
        case 0:
          return new HomeFragment();
        case 1:
          questionFragment = new QuestionFragment();
          questionFragment.setArguments(bundle);
          return questionFragment;
        default:
          return new HomeFragment();
      }
    }
    
    @Override
    public int getCount() {
      return 2;
    }
  }
  
}
