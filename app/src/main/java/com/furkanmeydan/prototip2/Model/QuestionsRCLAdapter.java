package com.furkanmeydan.prototip2.Model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.furkanmeydan.prototip2.DataLayer.QuestionDAL;
import com.furkanmeydan.prototip2.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class QuestionsRCLAdapter extends RecyclerView.Adapter<QuestionsRCLAdapter.PostHolder> {

    QuestionDAL questionDAL = new QuestionDAL();
    private ArrayList<Question> questions;

    public QuestionsRCLAdapter(ArrayList<Question> questions) {
        this.questions = questions;
    }

    @NonNull
    @Override
    public QuestionsRCLAdapter.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.fragment_questions_row,parent,false);

        return new QuestionsRCLAdapter.PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        holder.txtQuestion.setText(questions.get(position).getQuestionText());
        holder.txtQuestionOwnerName.setText(questions.get(position).getSenderNameText());
        holder.txtAnswer.setText(questions.get(position).getAnswerText());
        holder.txtPostHeader.setText(questions.get(position).getPostHeaderText());
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder {
        TextView txtQuestion;
        TextView txtQuestionOwnerName;
        TextView txtAnswer;
        TextView txtPostHeader;

        public PostHolder(@NonNull View itemView) {
            super(itemView);
            txtQuestion = itemView.findViewById(R.id.questionsRCLRowQuestion);
            txtQuestionOwnerName = itemView.findViewById(R.id.questionsRCLRowQuestionOwner);
            txtAnswer = itemView.findViewById(R.id.questionsRCLRowAnswer);
            txtPostHeader = itemView.findViewById(R.id.questionsRCLRowPostHeader);
        }
    }
}
