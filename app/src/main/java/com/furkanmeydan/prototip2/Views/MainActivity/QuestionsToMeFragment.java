package com.furkanmeydan.prototip2.Views.MainActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.furkanmeydan.prototip2.DataLayer.Callbacks.QuestionCallback;
import com.furkanmeydan.prototip2.DataLayer.QuestionDAL;
import com.furkanmeydan.prototip2.Models.Question;
import com.furkanmeydan.prototip2.Adapters.QuestionsToMeRCLAdapter;
import com.furkanmeydan.prototip2.R;

import java.util.ArrayList;
import java.util.List;


public class QuestionsToMeFragment extends Fragment {
    RecyclerView recyclerView;
    QuestionsToMeRCLAdapter adapter;
    QuestionDAL questionDAL;

    MainActivity activity;
    ArrayList<Question> questionsArrayList;

    private ConstraintLayout layoutInfo;

    public QuestionsToMeFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        questionsArrayList = new ArrayList<>();
        questionDAL = new QuestionDAL();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_questions_to_me, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layoutInfo = view.findViewById(R.id.consLayoutMyQuestions);
        recyclerView = view.findViewById(R.id.questionToMeRCL);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new QuestionsToMeRCLAdapter(questionsArrayList,activity);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        getQuestions();
    }

    public void getQuestions(){
        Log.d("Tag","getQuestions içi");
        String currentUserID = questionDAL.getUserId();
        questionDAL.getQuestions(currentUserID, new QuestionCallback() {
            @Override
            public void onQuestionsRetrieved(List<Question> questions) {
                super.onQuestionsRetrieved(questions);
                Log.d("Tag","callback İçi");
                questionsArrayList.addAll(questions);
                adapter.notifyDataSetChanged();

                if(questionsArrayList.size()>0){
                    layoutInfo.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }

            }
        });

    }
}