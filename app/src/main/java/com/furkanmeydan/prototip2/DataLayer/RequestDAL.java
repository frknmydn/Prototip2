package com.furkanmeydan.prototip2.DataLayer;

import android.util.Log;

import androidx.annotation.NonNull;

import com.furkanmeydan.prototip2.Models.CollectionHelper;
import com.furkanmeydan.prototip2.Models.Request;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.CollationElementIterator;
import java.util.List;
import java.util.UUID;

public class RequestDAL {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    PostDAL postDAL = new PostDAL();
    String currentUserID = firebaseAuth.getCurrentUser().getUid();

    public RequestDAL() {
    }

    public void sendRequest(String senderID, String senderName, String senderGender, String senderImage,
                            String senderBirthdate, String senderEmail, String postID, String postOwnerID,
                            Double lat1, Double lng1, Double lat2, Double lng2, String postHeader, final RequestCallback callback){

        String requestID = UUID.randomUUID().toString();
        Request request = new Request(requestID,senderID,senderName,senderGender,senderImage,senderBirthdate,senderEmail,postID,postOwnerID,lat1,lng1,lat2,lng2,0,postHeader);
        firestore.collection(CollectionHelper.USER_COLLECTION).document(postOwnerID)
                .collection(CollectionHelper.POST_COLLECTION).document(postID)
                .collection(CollectionHelper.REQUEST_COLLECTION).document(requestID).set(request).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                callback.onRequestSent();
            }
        });


    }

    public void getMyRequests(final RequestCallback callback){
        firestore.collectionGroup(CollectionHelper.REQUEST_COLLECTION).
                whereEqualTo(CollectionHelper.REQUEST_STATUS, 0).
                whereEqualTo(CollectionHelper.REQUEST_POSTOWNER, currentUserID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful() && task.getResult()!=null){
                    List<Request> list = task.getResult().toObjects(Request.class);

                    callback.getRequestsToMe(list);

                }
            }
        });

    }

    public void getAcceptedRequestsISent(final RequestCallback callback){
        String currentUserID = firebaseAuth.getCurrentUser().getUid();
        firestore.collectionGroup(CollectionHelper.REQUEST_COLLECTION)
                 .whereEqualTo(CollectionHelper.REQUEST_SENDERID,currentUserID)
                 .whereEqualTo(CollectionHelper.REQUEST_STATUS, 1).get()
                 .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful() && task.getResult() !=null){
                    if(task.getResult().toObjects(Request.class).size() > 0){
                        List<Request> list = task.getResult().toObjects(Request.class);

                        callback.onRequestsRetrievedNotNull(list);
                    }
                }
            }
        });
    }
    public void getAwatingRequestsISent(final RequestCallback callback){
        String currentUserID = firebaseAuth.getCurrentUser().getUid();
        firestore.collectionGroup(CollectionHelper.REQUEST_COLLECTION)
                .whereEqualTo(CollectionHelper.REQUEST_SENDERID,currentUserID)
                .whereEqualTo(CollectionHelper.REQUEST_STATUS, 0).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() && task.getResult() !=null){
                            if(task.getResult().toObjects(Request.class).size() > 0){
                                List<Request> list = task.getResult().toObjects(Request.class);
                                callback.onRequestsRetrievedNotNull(list);
                            }
                        }
                    }
                });
    }
    public void acceptRequest(final String postID, final String postOwnerID, String requestID, final RequestCallback callback){

        firestore.collection(CollectionHelper.USER_COLLECTION)
                .document(postOwnerID).collection(CollectionHelper.POST_COLLECTION)
                .document(postID).collection(CollectionHelper.REQUEST_COLLECTION)
                .document(requestID).update(CollectionHelper.REQUEST_STATUS, 1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                postDAL.decreasePassengerCount(postID,postOwnerID,callback);

            }
        });


    }
    public void rejectRequest(String postID, String postOwnerID, String requestID, final RequestCallback callback){
        firestore.collection(CollectionHelper.USER_COLLECTION)
                .document(postOwnerID).collection(CollectionHelper.POST_COLLECTION)
                .document(postID).collection(CollectionHelper.REQUEST_COLLECTION)
                .document(requestID).update(CollectionHelper.REQUEST_STATUS,-1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                callback.onRequestRejected();
            }
        });

    }

    public void getRequest(final String postID, final String postOwnerID, final RequestCallback callback){

         currentUserID = firebaseAuth.getCurrentUser().getUid();
         Log.d("RequestDalUserId",currentUserID);


        firestore.collection(CollectionHelper.USER_COLLECTION)
                .document(postOwnerID).collection(CollectionHelper.POST_COLLECTION)
                .document(postID).collection(CollectionHelper.REQUEST_COLLECTION)
                .whereEqualTo(CollectionHelper.REQUEST_SENDERID,currentUserID)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful() && task.getResult().size()>0){
                    Log.d("RequestDalOncomplete","Çalışıyor");
                    callback.onRequestRetrievedNotNull();
                }
            }
        });



         /*
         firestore.collection(CollectionHelper.USER_COLLECTION)
                 .document(postOwnerID).collection(CollectionHelper.POST_COLLECTION)
                 .document(postID).collection(CollectionHelper.REQUEST_COLLECTION)
                 .whereEqualTo(CollectionHelper.REQUEST_SENDERID,currentUserID)
                 .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
             @Override
             public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                 callback.onRequestRetrievedNotNull();

             }
         }).addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception e) {
                 callback.onRequestRetrievedNull();
             }
         });

          */

    }

    public void getAcceptedRequests(String postID, String postOwnerID, final RequestCallback callback){
        firestore.collection(CollectionHelper.USER_COLLECTION).document(postOwnerID)
                .collection(CollectionHelper.POST_COLLECTION).document(postID)
                .collection(CollectionHelper.REQUEST_COLLECTION).whereEqualTo(CollectionHelper.REQUEST_STATUS,1)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful() && task.getResult()!=null){
                    if(task.getResult().size() > 0){
                        List<Request> list = task.getResult().toObjects(Request.class);
                        Log.d("Tag","callbackonrequestretrievedNOTNULL");
                        callback.onRequestsRetrievedNotNull(list);
                    }
                    else{
                        Log.d("Tag","callbackonrequestretrievedNULL");
                        callback.onRequestsRetrievedNull();
                    }
                }
            }
        });
    }
}
