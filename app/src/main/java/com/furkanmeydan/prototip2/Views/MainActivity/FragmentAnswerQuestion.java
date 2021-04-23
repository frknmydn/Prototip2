package com.furkanmeydan.prototip2.Views.MainActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.furkanmeydan.prototip2.DataLayer.Callbacks.QuestionCallback;
import com.furkanmeydan.prototip2.DataLayer.QuestionDAL;
import com.furkanmeydan.prototip2.Models.Question;
import com.furkanmeydan.prototip2.R;


public class FragmentAnswerQuestion extends Fragment {

    QuestionDAL questionDAL;
    MainActivity activity;
    TextView txtQuestion, txtPostHeader;
    EditText edtAnswer;
    Button btnAnswer;
    String answer;
    Question currentQuestion;

    public FragmentAnswerQuestion() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        questionDAL = new QuestionDAL();
        activity = (MainActivity) getActivity();
        if(getArguments() !=null){
            currentQuestion = (Question) getArguments().getSerializable("question");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_answer_question, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtQuestion = view.findViewById(R.id.tctFragmentAnswerQuestionQuestion);
        edtAnswer = view.findViewById(R.id.edtFragmentAnswerQuestionAnswer);
        btnAnswer = view.findViewById(R.id.btnFragmentAnswerQuestion);
        txtPostHeader = view.findViewById(R.id.txtPostHeader);
        txtQuestion.setText(currentQuestion.getQuestionText());
        txtPostHeader.setText(currentQuestion.getPostHeaderText());

        btnAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answerQuestion();
            }
        });

    }

    public void answerQuestion(){

        answer = edtAnswer.getText().toString();
        questionDAL.answerQuestion(currentQuestion.getPostOwnerID(), currentQuestion.getPostID(),
                currentQuestion.getQuestionID(), answer, new QuestionCallback() {
            @Override
            public void onQuestionAnswered() {
                super.onQuestionAnswered();
                activity.onBackPressed();
            }
        });

    }

}