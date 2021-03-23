package com.furkanmeydan.prototip2.Model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.furkanmeydan.prototip2.DataLayer.QuestionCallback;
import com.furkanmeydan.prototip2.DataLayer.QuestionDAL;
import com.furkanmeydan.prototip2.R;

import java.util.ArrayList;

public class QuestionsToMeRCLAdapter extends RecyclerView.Adapter<QuestionsToMeRCLAdapter.PostHolder> {

    QuestionDAL questionDAL = new QuestionDAL();
    private ArrayList<Question> questions;
    @NonNull
    @Override
    public QuestionsToMeRCLAdapter.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.fragment_questions_to_me_row,parent,false);

        return new QuestionsToMeRCLAdapter.PostHolder(view);
    }

    public QuestionsToMeRCLAdapter(ArrayList<Question> questions) {
        this.questions = questions;
    }

    @Override
    public void onBindViewHolder(@NonNull final PostHolder holder, final int position) {

        holder.txtHeader.setText(questions.get(position).getPostHeaderText());
        holder.txtSenderName.setText(questions.get(position).getSenderNameText());
        holder.txtQuestionText.setText(questions.get(position).getQuestionText());
        holder.btnDeleteQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questionDAL.deactivateQuestion(questions.get(position).getPostOwnerID()
                        , questions.get(position).getPostID(), questions.get(position).getQuestionID(),
                        new QuestionCallback() {
                            @Override
                            public void onQuestionDeactivated() {
                                super.onQuestionDeactivated();

                                notifyDataSetChanged();
                            }
                        });
            }
        });

    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder {
        TextView txtHeader, txtQuestionText, txtSenderName;
        Button btnDeleteQuestion;
        public PostHolder(@NonNull View itemView) {
            super(itemView);

            txtHeader = itemView.findViewById(R.id.fragmentQuestionsToMeRowPostHeader);
            txtQuestionText = itemView.findViewById(R.id.fragmentQuestionsToMeRowQuestionText);
            txtSenderName = itemView.findViewById(R.id.fragmentQuestionsToMeRowSenderUserName);
            btnDeleteQuestion = itemView.findViewById(R.id.fragmentQuestionsToMeRowDeleteButton);
        }
    }
}
