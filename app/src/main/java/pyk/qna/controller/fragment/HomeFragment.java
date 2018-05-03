package pyk.qna.controller.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import pyk.qna.R;
import pyk.qna.controller.activity.MainActivity;
import pyk.qna.view.adapter.ItemAdapter;

public class HomeFragment extends Fragment {
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);
    RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_home_list);
  
    FrameLayout frameLayout = (FrameLayout) getActivity().findViewById(R.id.root);
    ItemAdapter itemAdapter = new ItemAdapter(getActivity(), frameLayout, ((MainActivity)getActivity()));
    recyclerView.setAdapter(itemAdapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    
    return rootView;
  }
  
}