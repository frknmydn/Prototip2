package com.furkanmeydan.prototip2.DataLayer;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.TaskExecutor;

import com.furkanmeydan.prototip2.DataLayer.Callbacks.MessageCallback;
import com.furkanmeydan.prototip2.Models.CollectionHelper;
import com.furkanmeydan.prototip2.Models.Message;
import com.furkanmeydan.prototip2.Models.Post;
import com.furkanmeydan.prototip2.Models.Request;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MessageDAL {
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://prototip2-69173-default-rtdb.europe-west1.firebasedatabase.app/");
    DatabaseReference myRef = firebaseDatabase.getReference("message");
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String currentUserID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();


    public void sendMessage(String senderID, String receiverID, String message, MessageCallback newCallback){
        Long timestamp = Timestamp.now().getSeconds();
        Message MessageToSend = new Message(senderID,receiverID,timestamp,message);
        myRef.child(CollectionHelper.MESSAGE_COLLECTION).setValue(MessageToSend).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                     newCallback.onMessageSent();
            }
        });
    }

    public void receiveMessage(String senderID, String receiverID, MessageCallback messageCallback){
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Message newMessage = snapshot.getValue(Message.class);
                messageCallback.onMessageReceived(newMessage);

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.d("TAG","mesj yarra yemiş");
            }
        };

    }


    public void getProfilesForPostOwner(MessageCallback callback){
        ArrayList<Integer> statusArr = new ArrayList<>(Arrays.asList(1,3));

        Long currentTimestamp = Timestamp.now().getSeconds();

        /*
        firestore.collection(CollectionHelper.USER_COLLECTION)
                .document(currentUserID)
                .collection(CollectionHelper.POST_COLLECTION)
                .whereIn(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_STATUS,statusArr)
                .whereGreaterThan(CollectionHelper.POST_TIMESTAMP,currentTimestamp).get().addOnCompleteListener(task -> {
                  if(task.isSuccessful() && task.getResult() !=null){
                      QuerySnapshot postsList = task.getResult();
                      for(QueryDocumentSnapshot qds : postsList){
                          DocumentReference docref = qds.getReference();
                          docref.collection(CollectionHelper.REQUEST_COLLECTION).whereEqualTo(CollectionHelper.REQUEST_STATUS,1)
                                  .get().addOnCompleteListener(task1 -> {
                                      if(task1.isSuccessful()&& task1.getResult() !=null){
                                          List<Request> requestsList = task1.getResult().toObjects(Request.class);
                                          callback.onProfilesLoaded(requestsList);
                                      }
                                  });
                      }

                  }
                });

         */

        firestore.collectionGroup(CollectionHelper.REQUEST_COLLECTION)
                .whereEqualTo(CollectionHelper.REQUEST_POSTOWNER,currentUserID)
                .whereEqualTo(CollectionHelper.REQUEST_STATUS,1)
                .whereGreaterThan(CollectionHelper.REQUEST_POSTTIMESTAMP,currentTimestamp).get().addOnCompleteListener(task ->{
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<Request> requestsList = task.getResult().toObjects(Request.class);
                        callback.onProfilesLoaded(requestsList);
                        Log.d("TAG", "getProfilesForPostOwner: PATLAMADI");

            }
                    else{
                        Log.d("TAG", "getProfilesForPostOwner: PATLADI");
                    }
        });

    }
    public void getProfilesForRequestSender(MessageCallback callback){
        Long currentTimestamp = Timestamp.now().getSeconds();
        firestore.collectionGroup(CollectionHelper.REQUEST_COLLECTION)
                .whereEqualTo(CollectionHelper.REQUEST_SENDERID,currentUserID)
                .whereEqualTo(CollectionHelper.REQUEST_STATUS,1)
                .whereGreaterThan(CollectionHelper.REQUEST_POSTTIMESTAMP,currentTimestamp)
                .get().addOnCompleteListener(task ->{
                    if(task.isSuccessful() && task.getResult() !=null){
                        List<Request> requestsList = task.getResult().toObjects(Request.class);
                        callback.onProfilesLoaded(requestsList);
                        Log.d("TAG", "getProfilesForRequestSender: PATLAMADI");
                    }
                    else{
                        Log.d("TAG", "getProfilesForRequestSender: PATLADI");
                    }
        });
    }



}
