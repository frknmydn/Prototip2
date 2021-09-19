package com.furkanmeydan.prototip2.DataLayer;

import android.util.Log;

import androidx.annotation.NonNull;

import com.furkanmeydan.prototip2.DataLayer.Callbacks.RequestCallback;
import com.furkanmeydan.prototip2.Models.CollectionHelper;
import com.furkanmeydan.prototip2.Models.Request;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class RequestDAL {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    PostDAL postDAL = new PostDAL();
    String currentUserID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();


    public RequestDAL() {
    }

    public void sendRequest(String senderID, String senderName, String senderGender, String senderImage,
                            String senderBirthdate, String senderEmail, String postID, String postOwnerID,
                            Double lat1, Double lng1, Double lat2, Double lng2, String postHeader,
                            String requestText,String oneSignalID,String ownerOneSignalID,long postTimestamp,
                            final RequestCallback callback){

        String requestID = UUID.randomUUID().toString();
        Request request = new Request(requestID,senderID,senderName,senderGender,senderImage,senderBirthdate,senderEmail,postID,postOwnerID,lat1,lng1,lat2,lng2,0,0,0,postHeader, requestText,postTimestamp);
        firestore.collection(CollectionHelper.USER_COLLECTION).document(postOwnerID)

                .collection(CollectionHelper.POST_COLLECTION).document(postID)
                .collection(CollectionHelper.REQUEST_COLLECTION).document(requestID).set(request).addOnCompleteListener(task -> callback.onRequestSent());


    }

    public void getMyRequests(final RequestCallback callback){
        Long timeStampNow = Timestamp.now().getSeconds();
        Log.d("TAG", "getMyRequests: now" + timeStampNow);

        firestore.collectionGroup(CollectionHelper.REQUEST_COLLECTION).
                whereEqualTo(CollectionHelper.REQUEST_STATUS, 0).
                whereEqualTo(CollectionHelper.REQUEST_POSTOWNER, currentUserID)
                .whereGreaterThan(CollectionHelper.REQUEST_POSTTIMESTAMP,timeStampNow)
                .get().addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult()!=null){

                        List<Request> list = task.getResult().toObjects(Request.class);

                        callback.getRequestsToMe(list);

                    }
                });

    }

    //Method iki defa çağırılacağı için DAL içindeki hazır currentuserid kullanmak yerine variable'a atanıyor.
    //İkinci çağırılışta id'ler yer değiştircek.
    public void getRequestStatusToBlock(String userid, String postOwnerID, final RequestCallback callback){

        Timestamp currentTime = Timestamp.now();
        long currentTimeSeconds = currentTime.getSeconds();
        firestore.collectionGroup(CollectionHelper.REQUEST_COLLECTION)
                .whereEqualTo(CollectionHelper.REQUEST_SENDERID,userid)
                .whereEqualTo(CollectionHelper.REQUEST_POSTOWNER,postOwnerID)
                .whereEqualTo(CollectionHelper.REQUEST_STATUS,1)
                .whereGreaterThan(CollectionHelper.REQUEST_POSTTIMESTAMP,currentTimeSeconds)
                .get().addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult() !=null){
                        boolean flag;
                        flag = task.getResult().size() > 0;
                        callback.onAcceptedRequestSearchResult(flag);

                    }
                });
    }

    public void deleteRequestsOnBlock(String userid, String postOwnerID , final RequestCallback callback){
        firestore.collectionGroup(CollectionHelper.REQUEST_COLLECTION).whereEqualTo(CollectionHelper.REQUEST_SENDERID,userid)
                .whereEqualTo(CollectionHelper.REQUEST_POSTOWNER,postOwnerID).get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult() !=null){
                        if(task.getResult().size() > 0) {
                            for (DocumentSnapshot ds : task.getResult().getDocuments()) {
                                ds.getReference().delete();
                            }
                        }
                        callback.onRequestsDeletedOnBlock();
                    }
                });

    }
    public void getAcceptedRequestsISent(final RequestCallback callback){
        String currentUserID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        firestore.collectionGroup(CollectionHelper.REQUEST_COLLECTION)
                 .whereEqualTo(CollectionHelper.REQUEST_SENDERID,currentUserID)
                 .whereEqualTo(CollectionHelper.REQUEST_STATUS, 1).get()
                 .addOnCompleteListener(task -> {
                     if(task.isSuccessful() && task.getResult() !=null){
                         if(task.getResult().toObjects(Request.class).size() > 0){
                             List<Request> list = task.getResult().toObjects(Request.class);

                             callback.onRequestsRetrievedNotNull(list);
                         }
                     }
                 });
    }
    public void getAwatingRequestsISent(final RequestCallback callback){
        String currentUserID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        firestore.collectionGroup(CollectionHelper.REQUEST_COLLECTION)
                .whereEqualTo(CollectionHelper.REQUEST_SENDERID,currentUserID)
                .whereEqualTo(CollectionHelper.REQUEST_STATUS, 0).get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult() !=null){
                        if(task.getResult().toObjects(Request.class).size() > 0){
                            List<Request> list = task.getResult().toObjects(Request.class);
                            callback.onRequestsRetrievedNotNull(list);
                        }
                    }
                });
    }

    public void confirmRequestAsRequestSender(String postID, String postOwnerID, String requestID, RequestCallback callback){
        firestore.collection(CollectionHelper.USER_COLLECTION).document(postOwnerID)
                .collection(CollectionHelper.POST_COLLECTION)
                .document(postID).collection(CollectionHelper.REQUEST_COLLECTION)
                .document(requestID).update(CollectionHelper.REQUEST_SELFCONFIRMED,1)
                .addOnCompleteListener(task -> callback.onRequestUpdated());
    }

    public void confirmRequestAsPostOwner(String postID, String postOwnerID, String requestID, RequestCallback callback){
        firestore.collection(CollectionHelper.USER_COLLECTION).document(postOwnerID)
                .collection(CollectionHelper.POST_COLLECTION)
                .document(postID).collection(CollectionHelper.REQUEST_COLLECTION)
                .document(requestID)
                .update(CollectionHelper.REQUEST_POSTOWNERCONFIRMED,1)
                .addOnCompleteListener(task -> callback.onRequestUpdated());
    }
    public void rejectRequestAsPostOwner(String postID, String postOwnerID, String requestID, RequestCallback callback){
        firestore.collection(CollectionHelper.USER_COLLECTION).document(postOwnerID)
                .collection(CollectionHelper.POST_COLLECTION)
                .document(postID).collection(CollectionHelper.REQUEST_COLLECTION)
                .document(requestID)
                .update(CollectionHelper.REQUEST_POSTOWNERCONFIRMED,2)
                .addOnCompleteListener(task -> callback.onRequestUpdated());
    }

    public void rejectRequestAsRequestSender(String postID, String postOwnerID, String requestID, RequestCallback callback){
        firestore.collection(CollectionHelper.USER_COLLECTION).document(postOwnerID)
                .collection(CollectionHelper.POST_COLLECTION)
                .document(postID).collection(CollectionHelper.REQUEST_COLLECTION)
                .document(requestID)
                .update(CollectionHelper.REQUEST_SELFCONFIRMED,2)
                .addOnCompleteListener(task -> callback.onRequestUpdated());
    }


    public void acceptRequest(final String postID, final String postOwnerID, String requestID, final RequestCallback callback){

        firestore.collection(CollectionHelper.USER_COLLECTION)
                .document(postOwnerID).collection(CollectionHelper.POST_COLLECTION)
                .document(postID).collection(CollectionHelper.REQUEST_COLLECTION)
                .document(requestID).update(CollectionHelper.REQUEST_STATUS, 1)
                .addOnCompleteListener(task -> postDAL.decreasePassengerCount(postID,postOwnerID,callback));


    }
    public void rejectRequest(String postID, String postOwnerID, String requestID, final RequestCallback callback){
        firestore.collection(CollectionHelper.USER_COLLECTION)
                .document(postOwnerID).collection(CollectionHelper.POST_COLLECTION)
                .document(postID).collection(CollectionHelper.REQUEST_COLLECTION)
                .document(requestID).delete().addOnCompleteListener(task -> callback.onRequestRejected());
    }

    public void getRequest(final String postID, final String postOwnerID, final RequestCallback callback){

         currentUserID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
         Log.d("RequestDalUserId",currentUserID);


        firestore.collection(CollectionHelper.USER_COLLECTION)
                .document(postOwnerID).collection(CollectionHelper.POST_COLLECTION)
                .document(postID).collection(CollectionHelper.REQUEST_COLLECTION)
                .whereEqualTo(CollectionHelper.REQUEST_SENDERID,currentUserID)
                .get().addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult()!=null && task.getResult().size()>0){
                        Log.d("RequestDalOncomplete","Çalışıyor");
                        callback.onRequestRetrievedNotNull();
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


    // Kullanıcı postsearchresultdetail fragmentında isteği sil butonuna bastığı anda ilk bu metod çalışıyor
    // Onaylı isteği var mı yok mu kontrolü yapılıyor
    public void checkRequestDeletionStatus(String postID, String postOwnerID, RequestCallback callback){
        currentUserID = firebaseAuth.getCurrentUser().getUid();
        firestore.collection(CollectionHelper.USER_COLLECTION)
                .document(postOwnerID).collection(CollectionHelper.POST_COLLECTION)
                .document(postID).collection(CollectionHelper.REQUEST_COLLECTION)
                .whereEqualTo(CollectionHelper.REQUEST_SENDERID, currentUserID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful() && task.getResult() != null){
                    if(task.getResult().size() > 0){
                        List<Request> list = task.getResult().toObjects(Request.class);
                        if(list.get(0).getStatus() == 1){
                            boolean flag = true;
                            callback.onRequestChecked(flag);
                        }else if (list.get(0).getStatus() == 0){
                            boolean flag = false;
                            callback.onRequestChecked(flag);
                        }

                    }
                }
            }
        });

    }

    //Eğer checkRequestDeletionStatus metodu true döndürürse bu metod çalışacak, bu metod çalışırken popup/dialog ekranına gerek var
    public void deleteRequestIfConfirmed(String postID, String postOwnerID,String reason, RequestCallback callback){
        currentUserID = firebaseAuth.getCurrentUser().getUid();

        firestore.collection(CollectionHelper.USER_COLLECTION)
                .document(postOwnerID).collection(CollectionHelper.POST_COLLECTION)
                .document(postID).collection(CollectionHelper.REQUEST_COLLECTION)
                .whereEqualTo(CollectionHelper.REQUEST_SENDERID, currentUserID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful() && task.getResult() !=null){
                    if(task.getResult().size() > 0){
                        List<Request> list = task.getResult().toObjects(Request.class);
                            for(DocumentSnapshot ds : task.getResult().getDocuments()){
                                DocumentReference dr = ds.getReference();
                                dr.update(CollectionHelper.REQUEST_DELETIONRESON,reason);
                            list.get(0).setPendingDeletion(reason);

                        }
                        callback.onRequestUpdated();
                    }

                }
            }
        });
    }
        //Eğer checkRequestDeletionStatus metodu false döndürürse bu metod çalışacak, bu metod çalışırken popup/dialog ekranına gerek yok
    public void deleteRequestIfNotConfirmed(String postID, String postOwnerID,String reason, RequestCallback callback){
        currentUserID = firebaseAuth.getCurrentUser().getUid();

        firestore.collection(CollectionHelper.USER_COLLECTION)
                .document(postOwnerID).collection(CollectionHelper.POST_COLLECTION)
                .document(postID).collection(CollectionHelper.REQUEST_COLLECTION)
                .whereEqualTo(CollectionHelper.REQUEST_SENDERID, currentUserID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful() && task.getResult() !=null){
                    if(task.getResult().size() > 0){
                            for (DocumentSnapshot ds : task.getResult().getDocuments()) {
                                ds.getReference().delete();
                            }
                            callback.onRequestDeleted();


                    }

                }
            }
        });
    }
    public void getAcceptedRequests(String postID, String postOwnerID, final RequestCallback callback){
        firestore.collection(CollectionHelper.USER_COLLECTION).document(postOwnerID)
                .collection(CollectionHelper.POST_COLLECTION).document(postID)
                .collection(CollectionHelper.REQUEST_COLLECTION).whereEqualTo(CollectionHelper.REQUEST_STATUS,1)
                .get().addOnCompleteListener(task -> {
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
                });
    }



    public void getConfirmUserForPostOwner(final RequestCallback callback){
        Long timestampNow = Timestamp.now().getSeconds();
        firestore.collectionGroup(CollectionHelper.REQUEST_COLLECTION)
                .whereEqualTo(CollectionHelper.REQUEST_POSTOWNER,currentUserID)
                .whereEqualTo(CollectionHelper.REQUEST_POSTOWNERCONFIRMED,0)
                .whereLessThan(CollectionHelper.REQUEST_POSTTIMESTAMP,timestampNow).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if(task.isSuccessful() && task.getResult()!=null){
                    if(task.getResult().size()>0){
                        List<Request> list = task.getResult().toObjects(Request.class);
                        Log.d("Tag","callbackonrequestretrievedNOTNULL for confirmUserForPostOwner");
                        callback.onRequestsRetrievedNotNull(list);
                    }
                    else {
                        Log.d("Tag","callbackonrequestretrievedNULL for confirmUserForPostOwner");
                        callback.onRequestsRetrievedNull();

                    }
                }
            }
        });
    }


    public void getConfirmUserForRequestSender(final RequestCallback callback){
        Long timestampNow = Timestamp.now().getSeconds();
        firestore.collectionGroup(CollectionHelper.REQUEST_COLLECTION)
                .whereEqualTo(CollectionHelper.REQUEST_SENDERID,currentUserID)
                .whereEqualTo(CollectionHelper.REQUEST_SELFCONFIRMED,0)
                .whereLessThan(CollectionHelper.REQUEST_POSTTIMESTAMP,timestampNow).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if(task.isSuccessful() && task.getResult()!=null){
                    if(task.getResult().size()>0){
                        List<Request> list = task.getResult().toObjects(Request.class);
                        Log.d("Tag","callbackonrequestretrievedNOTNULL for confirmUserRequestSender");
                        callback.onRequestsRetrievedNotNull(list);
                    }
                    else {
                        Log.d("Tag","callbackonrequestretrievedNULL for confirmUserRequestSender");
                        callback.onRequestsRetrievedNull();

                    }
                }
            }
        });
    }

    public String getCurrentUserID() {
        return currentUserID;
    }
}
