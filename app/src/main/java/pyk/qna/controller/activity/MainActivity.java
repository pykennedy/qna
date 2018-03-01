package pyk.qna.controller.activity;

import android.app.DialogFragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;
import pyk.qna.R;
import pyk.qna.controller.fragment.LoginDialog;
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
