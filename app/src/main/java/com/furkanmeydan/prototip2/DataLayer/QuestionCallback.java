package com.furkanmeydan.prototip2.DataLayer;

import com.furkanmeydan.prototip2.Model.Question;

import java.util.List;

public abstract class QuestionCallback {

    public void onQuestionAdded(){

    }

    public void onQuestionsRetrieved(List<Question> questions){

    }

    public void onQuestionDeactivated(){

    }

    public void onQuestionAnswered(){

    }


}
