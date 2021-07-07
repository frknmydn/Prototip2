package com.furkanmeydan.prototip2.Views.PostSearchResultDetailActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.furkanmeydan.prototip2.DataLayer.Callbacks.QuestionCallback;
import com.furkanmeydan.prototip2.DataLayer.QuestionDAL;
import com.furkanmeydan.prototip2.Models.Post;
import com.furkanmeydan.prototip2.Models.Question;
import com.furkanmeydan.prototip2.Adapters.QuestionsRCLAdapter;
import com.furkanmeydan.prototip2.R;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.List;


public class FragmentPostSearchResultQuestions extends Fragment {

    RecyclerView recyclerView;
    Button btnAskQuestion;
    PostSearchResultDetailActivity activity;
    QuestionsRCLAdapter adapter;
    ArrayList<Question> questionList;
    QuestionDAL questionDAL;
    Post post;
    Long currentTimestamp;
    boolean isPostOutdated;
    ConstraintLayout layoutQuestions;

    public FragmentPostSearchResultQuestions() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (PostSearchResultDetailActivity) getActivity();
        questionList = new ArrayList<>();
        questionDAL = new QuestionDAL();
        post = activity.post;
        currentTimestamp = Timestamp.now().getSeconds();
        isPostOutdated = post.getTimestamp() <= currentTimestamp - 180;

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

        layoutQuestions = view.findViewById(R.id.layoutQuestions);
        btnAskQuestion = view.findViewById(R.id.fragmentPostSearchResultQuestionsBtn);
        recyclerView = view.findViewById(R.id.fragmentPostSearchResultQuestionsRCL);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new QuestionsRCLAdapter(questionList);
        recyclerView.setAdapter(adapter);

        currentTimestamp = Timestamp.now().getSeconds();
        isPostOutdated = post.getTimestamp() <= currentTimestamp - 180;

        getQuestions();

        if(isPostOutdated){
            btnAskQuestion.setVisibility(View.GONE);
        }
        else{
            btnAskQuestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.changeFragment(new FragmentPostSearchResultAskQuestion());
                }
            });
        }

    }


    public void getQuestions(){
        String currentUserId = questionDAL.getUserId();
        String postID = activity.post.getPostID();
        questionDAL.getAnsweredQuestions(currentUserId, postID ,new QuestionCallback() {
            @Override
            public void onQuestionsRetrieved(List<Question> questions) {
                super.onQuestionsRetrieved(questions);

                questionList.addAll(questions);
                adapter.notifyDataSetChanged();

                if(questionList.size()>0){
                    layoutQuestions.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });

    }
}