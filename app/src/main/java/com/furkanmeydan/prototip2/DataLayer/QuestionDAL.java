package com.furkanmeydan.prototip2.DataLayer;

import androidx.annotation.NonNull;

import com.furkanmeydan.prototip2.Model.CollectionHelper;
import com.furkanmeydan.prototip2.Model.Question;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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


    public void uploadQuestion(String question, String ownerID, String postID,String postHeader,String senderName, final QuestionCallback callback){

        String questionID = UUID.randomUUID().toString();
        Question questionObject = new Question(postID,postHeader,userId,senderName,question,ownerID,null,questionID,0);

        firestore.collection(CollectionHelper.USER_COLLECTION).document(ownerID)
                .collection(CollectionHelper.POST_COLLECTION).document(postID)
                .collection(CollectionHelper.QUESTION_COLLECTION).document(questionID).set(questionObject)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        callback.onQuestionAdded();
                    }
                });

    }



    public void getQuestions(String userId, final QuestionCallback callback){

        firestore.collectionGroup(CollectionHelper.QUESTION_COLLECTION).whereEqualTo(CollectionHelper.QUESTION_STATUS,1)
                .whereEqualTo(CollectionHelper.QUESTION_POSTOWNERID,userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful() && task.getResult() !=null){
                   List<Question> questions =  task.getResult().toObjects(Question.class);
                    callback.onQuestionsRetrieved(questions);
                }
            }
        });


    }

    public void deactivateQuestion(String userId, String postId, String questionId, final QuestionCallback callback){
        firestore.collection(CollectionHelper.USER_COLLECTION).document(userId)
                .collection(CollectionHelper.POST_COLLECTION).document(postId)
                .collection(CollectionHelper.QUESTION_COLLECTION).document(questionId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful() && task.getResult() !=null){
                    task.getResult().getReference().update(CollectionHelper.QUESTION_STATUS,-1);
                    callback.onQuestionDeactivated();
                }
            }
        });
    }

    public String getUserId() {
        return userId;
    }
}
