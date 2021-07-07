package com.furkanmeydan.prototip2.DataLayer;

import android.util.Log;

import androidx.annotation.NonNull;

import com.furkanmeydan.prototip2.DataLayer.Callbacks.QuestionCallback;
import com.furkanmeydan.prototip2.Models.CollectionHelper;
import com.furkanmeydan.prototip2.Models.Question;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class QuestionDAL {
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private String userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

    public QuestionDAL() {
    }

    public void uploadQuestion(String question, String ownerID, String postID, String postHeader, String senderName,long postTimestamp, final QuestionCallback callback){

        String questionID = UUID.randomUUID().toString();
        Question questionObject = new Question(postID,postHeader,userId,senderName,question,ownerID,null,questionID,0,postTimestamp);

        firestore.collection(CollectionHelper.USER_COLLECTION).document(ownerID)
                .collection(CollectionHelper.POST_COLLECTION).document(postID)
                .collection(CollectionHelper.QUESTION_COLLECTION).document(questionID).set(questionObject)
                .addOnCompleteListener(task -> callback.onQuestionAdded());

    }

    public void getAnsweredQuestions(String userId,String postID,final QuestionCallback callback){
        firestore.collectionGroup(CollectionHelper.QUESTION_COLLECTION).whereEqualTo(CollectionHelper.QUESTION_STATUS,1)
                .whereEqualTo(CollectionHelper.QUESTION_POSTOWNERID,userId).
                whereEqualTo(CollectionHelper.QUESTION_POSTID,postID).get().addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult()!=null){
                        List<Question> answeredQuestions = task.getResult().toObjects(Question.class);
                        callback.onQuestionsRetrieved(answeredQuestions);
                    }
                });
    }


    public void getQuestions(String userId, final QuestionCallback callback){

        Log.d("Tag","DAL getQuestions içi");
        Long timestampNow = Timestamp.now().getSeconds();

        firestore.collectionGroup(CollectionHelper.QUESTION_COLLECTION).whereEqualTo(CollectionHelper.QUESTION_STATUS,0)
                .whereEqualTo(CollectionHelper.QUESTION_POSTOWNERID,userId)
                // yani burada zaman kıyasalaması yaptığımda kod patlıyor
                //.whereGreaterThan(CollectionHelper.QUESTION_POST_TIMESTAMP,timestampNow)
                .get()
                .addOnCompleteListener(task -> {
                    Log.d("Tag","getQuestions task onComplete içi");
                    if(task.isSuccessful() && task.getResult() !=null){
                        Log.d("Tag","getQuestions task is successful && null değil");
                        List<Question> questions =  task.getResult().toObjects(Question.class);
                        callback.onQuestionsRetrieved(questions);
                    }
                });


    }

    public void deactivateQuestion(String userId, String postId, String questionId, final QuestionCallback callback){
        firestore.collection(CollectionHelper.USER_COLLECTION).document(userId)
                .collection(CollectionHelper.POST_COLLECTION).document(postId)
                .collection(CollectionHelper.QUESTION_COLLECTION).document(questionId)
                .get().addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult() !=null){
                        task.getResult().getReference().update(CollectionHelper.QUESTION_STATUS,-1);
                        callback.onQuestionDeactivated();
                    }
                });
    }

    public void answerQuestion(String userId, String postId, String questionId, final String answer, final QuestionCallback callback){
        firestore.collection(CollectionHelper.USER_COLLECTION).document(userId)
                .collection(CollectionHelper.POST_COLLECTION).document(postId)
                .collection(CollectionHelper.QUESTION_COLLECTION).document(questionId)
                .get().addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult() !=null){
                        DocumentReference docref = task.getResult().getReference();
                        docref.update(CollectionHelper.QUESTION_STATUS,1);
                        docref.update(CollectionHelper.QUESTION_ANSWER,answer);
                        callback.onQuestionAnswered();
                    }
                });
    }

    public String getUserId() {
        return userId;
    }
}
