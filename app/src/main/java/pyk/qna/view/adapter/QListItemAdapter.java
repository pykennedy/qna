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
import pyk.qna.controller.activity.MainActivity;
import pyk.qna.model.firebase.FirebaseHandler;
import pyk.qna.model.object.Question;
import pyk.qna.model.object.User;

public class QListItemAdapter extends RecyclerView.Adapter<QListItemAdapter.ItemAdapterViewHolder>
    implements FirebaseHandler.Delegate {
  
  private List<Question> questions = new ArrayList<Question>();
  Context     context;
  FrameLayout frameLayout;
  final MainActivity mainActivity;
  
  public QListItemAdapter(Context context, FrameLayout frameLayout, MainActivity mainActivity) {
    this.context = context;
    this.frameLayout = frameLayout;
    this.mainActivity = mainActivity;
    
    FirebaseHandler fb = new FirebaseHandler();
    fb.setDelegate(this);
    fb.readAllQuestions();
  }
  
  @Override
  public ItemAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int index) {
    View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.poi_item, viewGroup,
                                                                       false);
    return new ItemAdapterViewHolder(inflate, frameLayout);
  }
  
  @Override
  public void onBindViewHolder(ItemAdapterViewHolder itemAdapterViewHolder, int index) {
    itemAdapterViewHolder.update(questions.get((getItemCount() - 1) - index));
  }
  
  @Override
  public int getItemCount() { return (questions == null) ? 0 : questions.size(); }
  
  private void insertItem(Question question) {
    for (int i = 0; i < questions.size() - 1; i++) {
      String q1 = questions.get(i).getUsername() + questions.get(i).getPostTime();
      String q2 = question.getUsername() + question.getPostTime();
      if (q1.equals(q2)) {
        questions.set(i, question);
        return;
      }
    }
    questions.add(question);
    notifyDataSetChanged();
  }
  
  @Override public void onLoginSuccess(String successType) {}
  
  @Override public void onLoginFailed(String errorType)    {}
  
  @Override public void onReadUserSuccess(User user)       {}
  
  @Override public void onReadQuestionSuccess(Question question, boolean isList) {
    insertItem(question);
  }
  
  @Override public void onReadAnswerSuccess(List<String> result) {}
  
  
  class ItemAdapterViewHolder extends RecyclerView.ViewHolder {
    TextView title;
    TextView username;
    TextView postTime;
    BlurView blurView;
    
    public ItemAdapterViewHolder(View itemView, FrameLayout frameLayout) {
      super(itemView);
      title = (TextView) itemView.findViewById(R.id.question_list);
      username = (TextView) itemView.findViewById(R.id.username_list);
      postTime = (TextView) itemView.findViewById(R.id.datetime_list);
      
      blurView = itemView.findViewById(R.id.blurItemView);
      
      blurView.setupWith(frameLayout)
              .windowBackground(MainActivity.background)
              .blurAlgorithm(new RenderScriptBlur(context))
              .blurRadius(25f);
      blurView.setClipToOutline(true);
      
      itemView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View view) {
          mainActivity.switchToQuestion(title.getText().toString(), username.getText().toString() +
                                                                    postTime.getText().toString(),
                                        getQuestion(username.getText().toString() +
                                                    postTime.getText().toString()));
          
        }
      });
    }
    
    private Question getQuestion(String questionID) {
      for (Question question : questions) {
        if ((question.getUsername() + question.getPostTime()).equals(questionID)) {
          return question;
        }
      }
      return null;
    }
    
    void update(Question q) {
      title.setText(q.getQuestionText());
      username.setText(q.getUsername());
      postTime.setText(q.getPostTime());
    }
    
  }
  
}