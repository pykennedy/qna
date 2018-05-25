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

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;
import pyk.qna.R;
import pyk.qna.controller.Utility;
import pyk.qna.controller.fragment.HomeFragment;
import pyk.qna.controller.fragment.QuestionFragment;
import pyk.qna.controller.fragment.dialog.AnswerDialog;
import pyk.qna.controller.fragment.dialog.EditProfileDialog;
import pyk.qna.controller.fragment.dialog.LoginDialog;
import pyk.qna.controller.fragment.dialog.QuestionDialog;
import pyk.qna.model.firebase.FirebaseHandler;
import pyk.qna.model.object.Answer;
import pyk.qna.model.object.Question;
import pyk.qna.model.object.User;

public class MainActivity extends FragmentActivity
    implements View.OnClickListener, FirebaseHandler.Delegate {
  FrameLayout      frameLayout;
  BlurView         blurViewTop;
  BlurView         blurViewBottom;
  TextView         bottomTV;
  QuestionFragment questionFragment;
  Question         currentQuestion;
  CircleImageView image;
  TextView topTV;
  public static HashMap<String, String> userImageMap = new HashMap<>();
  
  public static Drawable          background;
  public static FrameLayout       sFrameLayout;
  private       LoginDialog       loginDialogFragment;
  private       EditProfileDialog editProfileDialogFragment;
  private       QuestionDialog    questionDialogFragment;
  private       AnswerDialog      answerDialogFragment;
  private       ViewPager         pager;
  private       PagerAdapter      pagerAdapter;
  private       String            currentQuestionID;
  private String currentQuestionUsername;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
    frameLayout = (FrameLayout) findViewById(R.id.root);
    sFrameLayout = frameLayout;
    blurViewTop = (BlurView) findViewById(R.id.blurViewTop);
    blurViewBottom = (BlurView) findViewById(R.id.blurViewBottom);
    
    image = (CircleImageView) findViewById(R.id.actionbar_image);
    image.setOnClickListener(this);
    
    topTV = (TextView) findViewById(R.id.title);
    bottomTV = (TextView) findViewById(R.id.lqa);
    bottomTV.setOnClickListener(this);
    
    FirebaseHandler.getFb().setDelegate(this);
    FirebaseHandler.getFb().readAllUsers();
    
    pager = (ViewPager) findViewById(R.id.vp_activity);
    pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
    pager.setAdapter(pagerAdapter);
    pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
      
      @Override public void onPageSelected(int position) {
        
        switch (position) {
          case 0:
            if (!bottomTV.getText().toString().equals("log in")) {
              bottomTV.setText("question");
            }
            topTV.setText("qna");
            break;
          case 1:
            if (!bottomTV.getText().toString().equals("log in")) {
              bottomTV.setText("answer");
            }
            topTV.setText(currentQuestionUsername + "'s q");
            break;
        }
      }
      
      @Override public void onPageScrollStateChanged(int state) {}
    });
    
    setupBlurView();
  }
  
  @Override public void onClick(View view) {
    switch (view.getId()) {
      case R.id.lqa:
        if (bottomTV.getText().toString().equals("log in")) {
          FirebaseHandler.getFb().setDelegate(this);
          loginDialogFragment = new LoginDialog();
          loginDialogFragment.show(getFragmentManager(), "LoginDialog");
        } else if (bottomTV.getText().toString().equals("question")) {
          questionDialogFragment = new QuestionDialog();
          questionDialogFragment.show(getFragmentManager(), "QuestionDialog");
        } else if (bottomTV.getText().toString().equals("answer")) {
          answerDialogFragment = new AnswerDialog();
          answerDialogFragment.setQuestion(currentQuestionID, currentQuestion);
          answerDialogFragment.show(getFragmentManager(), "AnswerDialog");
        }
        break;
      case R.id.actionbar_image:
        if (FirebaseHandler.getFb().getCurrentUsername() != null) {
          Utility.handlePermission(this);
          editProfileDialogFragment = new EditProfileDialog();
          editProfileDialogFragment.show(getFragmentManager(), "EditProfileDialog");
        } else {
          FirebaseHandler.getFb().setDelegate(this);
          loginDialogFragment = new LoginDialog();
          loginDialogFragment.show(getFragmentManager(), "LoginDialog");
        }
        break;
      default:
        break;
    }
  }
  
  public void switchToQuestion(String questionText, String questionID, Question question) {
    questionFragment.updateQuestion(questionText, questionID);
    currentQuestionID = questionID;
    currentQuestion = question;
    currentQuestionUsername = question.getUsername();
    
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
  @Override
  public void onBackPressed() {
    if(pager.getCurrentItem() == 1) {
      pager.setCurrentItem(0);
    } else {
      super.onBackPressed();
    }
  }
  
  @Override public void onLoginSuccess(String successType) {
    Toast.makeText(this, successType, Toast.LENGTH_SHORT).show();
    loginDialogFragment.dismiss();
    bottomTV.setText("question");
    FirebaseHandler fb = FirebaseHandler.getFb();
    fb.readAllQuestions();
    fb.readUser(fb.getCurrentUsername(), true);
  }
  
  @Override public void onLoginFailed(String errorType) {
    Toast.makeText(this, errorType, Toast.LENGTH_SHORT).show();
    loginDialogFragment.stopSpinning();
  }
  
  @Override public void onReadUserSuccess(User user)                             {
    if(user.getUsername().equals(FirebaseHandler.getFb().getCurrentUsername())) {
      image.setImageBitmap(
          (user.getPhoto() == null) ? Utility.getBitmapFromDrawable(R.drawable.emptyimage)
                                    : Utility.base64ToBitmap(user.getPhoto()));
    }
    userImageMap.put(user.getUsername(), user.getPhoto());
  }
  
  @Override public void onReadQuestionSuccess(Question question, boolean isList) {}
  
  @Override public void onReadAnswerSuccess(Answer result)                       {}

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
