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

    private ConstraintLayout layoutInfo, layoutProgress, layoutContent;

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


        recyclerView = view.findViewById(R.id.questionToMeRCL);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new QuestionsToMeRCLAdapter(questionsArrayList,activity);
        recyclerView.setAdapter(adapter);

        layoutInfo = view.findViewById(R.id.layoutMyRequestAcceptedInfo);
        layoutInfo.setVisibility(View.GONE);
        layoutContent = view.findViewById(R.id.layoutQuestionToMeRCL);
        layoutContent.setVisibility(View.GONE);
        layoutProgress = view.findViewById(R.id.layoutQuestionToMeProgress);
        layoutProgress.setVisibility(View.VISIBLE);
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
                questionsArrayList.clear();
                questionsArrayList.addAll(questions);
                Log.d("TAGGO", "onQuestionsRetrieved: " + questionsArrayList.size());
                adapter.notifyDataSetChanged();
                Log.d("TAGGO", "onQuestionsRetrieved: asd" +questionsArrayList.size());
                if(questionsArrayList.size()>0){
                    layoutProgress.setVisibility(View.GONE);
                    Log.d("TAGGO", "onQuestionsRetrieved: asd" +questionsArrayList.size());
                    layoutContent.setVisibility(View.VISIBLE);
                }
                else{
                    layoutProgress.setVisibility(View.GONE);
                    layoutInfo.setVisibility(View.VISIBLE);

                }

            }
        });

    }
}