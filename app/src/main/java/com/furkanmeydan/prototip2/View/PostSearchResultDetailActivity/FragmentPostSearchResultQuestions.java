package com.furkanmeydan.prototip2.View.PostSearchResultDetailActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.furkanmeydan.prototip2.DataLayer.QuestionCallback;
import com.furkanmeydan.prototip2.DataLayer.QuestionDAL;
import com.furkanmeydan.prototip2.Model.Question;
import com.furkanmeydan.prototip2.Model.QuestionsRCLAdapter;
import com.furkanmeydan.prototip2.R;

import java.util.ArrayList;
import java.util.List;


public class FragmentPostSearchResultQuestions extends Fragment {

    RecyclerView recyclerView;
    Button btnAskQuestion;
    PostSearchResultDetailActivity activity;
    QuestionsRCLAdapter adapter;
    ArrayList<Question> questionList;
    QuestionDAL questionDAL;

    public FragmentPostSearchResultQuestions() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (PostSearchResultDetailActivity) getActivity();
        questionList = new ArrayList<>();
        questionDAL = new QuestionDAL();

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
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new QuestionsRCLAdapter(questionList);
        recyclerView.setAdapter(adapter);

        getQuestions();


        btnAskQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.changeFragment(new FragmentPostSearchResultAskQuestion());
            }
        });
    }

    public void getQuestions(){
        String currentUserId = questionDAL.getUserId();
        questionDAL.getAnsweredQuestions(currentUserId, new QuestionCallback() {
            @Override
            public void onQuestionsRetrieved(List<Question> questions) {
                super.onQuestionsRetrieved(questions);
                questionList.addAll(questions);
                adapter.notifyDataSetChanged();
            }
        });

    }
}