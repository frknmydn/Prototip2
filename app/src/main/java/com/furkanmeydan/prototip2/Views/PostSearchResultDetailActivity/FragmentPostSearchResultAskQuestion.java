package com.furkanmeydan.prototip2.Views.PostSearchResultDetailActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.furkanmeydan.prototip2.DataLayer.LocalDataManager;
import com.furkanmeydan.prototip2.DataLayer.QuestionCallback;
import com.furkanmeydan.prototip2.DataLayer.QuestionDAL;
import com.furkanmeydan.prototip2.R;


public class FragmentPostSearchResultAskQuestion extends Fragment {
    PostSearchResultDetailActivity activity;
    TextView edtTxt;
    Button btnSend;
    QuestionDAL questionDAL;
    String postOwnerID,postID;
    LocalDataManager localDataManager;

    public FragmentPostSearchResultAskQuestion() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (PostSearchResultDetailActivity) getActivity();
        questionDAL = new QuestionDAL();
        postOwnerID = activity.post.getOwnerID();
        postID = activity.post.getPostID();
        localDataManager = new LocalDataManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_search_result_ask_question, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edtTxt = view.findViewById(R.id.fragmentPostSearchResultAskQuestionEdtxt);
        btnSend = view.findViewById(R.id.fragmentPostSearchResultAskQuestionbtn);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String question = edtTxt.getText().toString();
                String postHeader = activity.post.getDescription();
                String senderName = localDataManager.getSharedPreference(activity,"sharedNameSurname",null);
                questionDAL.uploadQuestion(question, postOwnerID, postID,postHeader,senderName, new QuestionCallback() {
                    @Override
                    public void onQuestionAdded() {
                        super.onQuestionAdded();
                        activity.onBackPressed();
                    }
                });


            }
        });
    }
}