package com.furkanmeydan.prototip2.View.PostSearchResultDetailActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.furkanmeydan.prototip2.R;


public class FragmentPostSearchResultQuestions extends Fragment {

    RecyclerView recyclerView;
    Button btnAskQuestion;
    PostSearchResultDetailActivity activity;


    public FragmentPostSearchResultQuestions() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (PostSearchResultDetailActivity) getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_search_result_questions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnAskQuestion = view.findViewById(R.id.fragmentPostSearchResultQuestionsBtn);
        recyclerView = view.findViewById(R.id.fragmentPostSearchResultQuestionsRCL);


        btnAskQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.changeFragment(new FragmentPostSearchResultAskQuestion());
            }
        });
    }
}