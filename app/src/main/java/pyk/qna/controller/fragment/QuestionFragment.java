package pyk.qna.controller.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import pyk.qna.R;
import pyk.qna.view.adapter.AListItemAdapter;
import pyk.qna.view.custom.HeaderDecoration;

public class QuestionFragment extends Fragment {
  RecyclerView recyclerView;
  AListItemAdapter itemAdapter;
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    ViewGroup    rootView     = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);
    recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_home_list);
    
    FrameLayout      frameLayout = (FrameLayout) getActivity().findViewById(R.id.root);
    itemAdapter = new AListItemAdapter(getActivity(), frameLayout);
    recyclerView.setAdapter(itemAdapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.addItemDecoration(HeaderDecoration.with(recyclerView, "")
                                  .inflate(R.layout.question_text)
                                  .parallax(0.35f)
                                  .build());
    
    return rootView;
  }
  
  public void updateQuestion(String questionText, String questionID) {
    recyclerView.removeItemDecorationAt(0);
    recyclerView.addItemDecoration(HeaderDecoration.with(recyclerView, questionText)
                                                   .inflate(R.layout.question_text)
                                                   .parallax(0.35f)
                                                   .build());
    itemAdapter.updateList(questionID);
  }
  
}
