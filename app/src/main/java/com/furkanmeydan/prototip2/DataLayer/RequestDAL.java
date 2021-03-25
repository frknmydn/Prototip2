package com.furkanmeydan.prototip2.DataLayer;

import androidx.annotation.NonNull;

import com.furkanmeydan.prototip2.Model.CollectionHelper;
import com.furkanmeydan.prototip2.Model.Request;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.UUID;

public class RequestDAL {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public RequestDAL() {
    }

    public void sendRequest(String senderID,String senderName,String senderGender,String senderImage,
                            String senderBirthdate,String senderEmail,String postID,String postOwnerID,
                            Double lat1,Double lng1, Double lat2, Double lng2,String postHeader,RequestCallback callback){

        String requestID = UUID.randomUUID().toString();
        Request request = new Request(requestID,senderID,senderName,senderGender,senderImage,senderBirthdate,senderEmail,postID,postOwnerID,lat1,lng1,lat2,lng2,0,postHeader);
        firestore.collection(CollectionHelper.USER_COLLECTION).document(postOwnerID)
                .collection(CollectionHelper.POST_COLLECTION).document(postID)
                .collection(CollectionHelper.REQUEST_COLLECTION).document(requestID).set(request);
        callback.onRequestSent();

    }

    public void getMyRequests(final RequestCallback callback){
        String currentUserID = firebaseAuth.getCurrentUser().getUid();
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
}
