package pyk.qna;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemAdapterViewHolder> {
  
  private List<String> poiItemList = null;
  Context context;
  FrameLayout frameLayout;
  
  public ItemAdapter(Context context, FrameLayout frameLayout) {
    this.context = context;
    this.frameLayout = frameLayout;
    List<String> s = new ArrayList<String>();
    for(int i = 0; i < 20; i++) {
      s.add("aasdfasfdsdfdsfasdfasdfasf");
    }
    poiItemList = s;
    notifyDataSetChanged();
  }
  
  @Override
  public ItemAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int index) {
    View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.poi_item, viewGroup,
                                                                       false);
    
    
    return new ItemAdapterViewHolder(inflate, frameLayout);
  }
  
  @Override
  public void onBindViewHolder(ItemAdapterViewHolder itemAdapterViewHolder, int index) {
    itemAdapterViewHolder.update("asdfasdfasdfasd");
  }
  
  @Override
  public int getItemCount() {
    int i = (poiItemList == null) ? 0 : poiItemList.size();
    
    return i;
  }
  
  public void setAll() {
    List<String> s = new ArrayList<String>();
    for(int i = 0; i < 20; i++) {
      s.add("aasdfasfdsdfdsfasdfasdfasf");
    }
    poiItemList = s;
    notifyDataSetChanged();
  }
  
  
  class ItemAdapterViewHolder extends RecyclerView.ViewHolder {
    TextView title;
    BlurView blurView;
    
    public ItemAdapterViewHolder(View itemView, FrameLayout frameLayout) {
      super(itemView);
      title = (TextView) itemView.findViewById(R.id.poi_item_name);
  
      blurView = itemView.findViewById(R.id.blurItemView);
      
      blurView.setupWith(frameLayout)
              .windowBackground(MainActivity.background)
              .blurAlgorithm(new RenderScriptBlur(context))
              .blurRadius(25f);
    }
    
    void update(String s) {
      title.setText(s);
    }
    
  }
  
}