package pyk.qna;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class MainActivity extends AppCompatActivity {
  FrameLayout frameLayout;
  View purple;
  BlurView blurView;
  View red;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
    frameLayout = (FrameLayout) findViewById(R.id.root);
    purple = (View) findViewById(R.id.purple);
    blurView = (BlurView) findViewById(R.id.blurView);
    red = (View) findViewById(R.id.red);
  
    ItemAdapter itemAdapter;
    
    itemAdapter = new ItemAdapter();
    RecyclerView recyclerView = (RecyclerView) this.findViewById(R.id.rv_list);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.setAdapter(itemAdapter);
    
    setupBlurView();
  }
  
  private void setupBlurView() {
    final float radius = 25f;
    final float minBlurRadius = 10f;
    final float step = 4f;
    
    //set background, if your root layout doesn't have one
    final Drawable windowBackground = getWindow().getDecorView().getBackground();
    
    final BlurView.ControllerSettings topViewSettings = blurView.setupWith(frameLayout)
                                                                   .windowBackground(windowBackground)
                                                                   .blurAlgorithm(new RenderScriptBlur(this))
                                                                   .blurRadius(radius);
  }
}
