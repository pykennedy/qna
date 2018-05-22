package pyk.qna.view.adapter;

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
import pyk.qna.R;
import pyk.qna.controller.Utility;
import pyk.qna.controller.activity.MainActivity;
import pyk.qna.model.firebase.FirebaseHandler;
import pyk.qna.model.object.Answer;
import pyk.qna.model.object.Question;
import pyk.qna.model.object.User;

public class AListItemAdapter extends RecyclerView.Adapter<AListItemAdapter.ItemAdapterViewHolder>
    implements FirebaseHandler.Delegate {
  
  private List<Answer> answers = new ArrayList<Answer>();
  Context     context;
  FrameLayout frameLayout;
  
  public AListItemAdapter(Context context, FrameLayout frameLayout) {
    this.context = context;
    this.frameLayout = frameLayout;
  }
  
  @Override
  public ItemAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int index) {
    View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.answer_item, viewGroup,
                                                                       false);
    return new ItemAdapterViewHolder(inflate, frameLayout);
  }
  
  @Override
  public void onBindViewHolder(ItemAdapterViewHolder itemAdapterViewHolder, int index) {
    itemAdapterViewHolder.update(answers.get((getItemCount() - 1) - index));
  }
  
  @Override
  public int getItemCount() { return (answers == null) ? 0 : answers.size(); }
  
  private void insertItem(Answer answer) {
    for (int i = 0; i < answers.size() - 1; i++) {
      String q1 = answers.get(i).getUsername() + answers.get(i).getPostTime();
      String q2 = answer.getUsername() + answer.getPostTime();
      if (q1.equals(q2)) {
        answers.set(i, answer);
        return;
      }
    }
    answers.add(answer);
    notifyDataSetChanged();
  }
  
  public void updateList(String questionID) {
    FirebaseHandler fb = new FirebaseHandler();
    fb.setDelegate(this);
    fb.readAllAnswers(questionID);
  }
  
  @Override public void onLoginSuccess(String successType) {}
  
  @Override public void onLoginFailed(String errorType)    {}
  
  @Override public void onReadUserSuccess(User user)       {}
  
  @Override public void onReadQuestionSuccess(Question question, boolean isList) {}
  
  @Override public void onReadAnswerSuccess(Answer answer) {
    insertItem(answer);
  }
  
  class ItemAdapterViewHolder extends RecyclerView.ViewHolder {
    TextView title;
    TextView username;
    TextView postTime;
    BlurView blurView;
    
    public ItemAdapterViewHolder(View itemView, FrameLayout frameLayout) {
      super(itemView);
      title = (TextView) itemView.findViewById(R.id.answer_list);
      username = (TextView) itemView.findViewById(R.id.username_qlist);
      postTime = (TextView) itemView.findViewById(R.id.datetime_qlist);
      
      blurView = itemView.findViewById(R.id.blurItemView);
      
      blurView.setupWith(frameLayout)
              .windowBackground(MainActivity.background)
              .blurAlgorithm(new RenderScriptBlur(context))
              .blurRadius(25f);
      blurView.setClipToOutline(true);
      
      if(title.getText().toString().equals("")) {
        itemView.setVisibility(View.INVISIBLE);
      }
    }
    
    private Answer getAnswer(String answerID) {
      for (Answer answer : answers) {
        if (Utility.getIDFromObject(null, answer).equals(answerID)) {
          return answer;
        }
      }
      return null;
    }
    
    void update(Answer a) {
      title.setText(a.getAnswerText());
      username.setText(a.getUsername());
      postTime.setText(a.getPostTime());
    }
    
  }
  
}