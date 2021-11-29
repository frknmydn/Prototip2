package com.furkanmeydan.prototip2.DataLayer;

import android.util.Log;

import androidx.annotation.NonNull;

import com.furkanmeydan.prototip2.DataLayer.Callbacks.MessageCallback;
import com.furkanmeydan.prototip2.Models.CollectionHelper;
import com.furkanmeydan.prototip2.Models.Message;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class MessageDAL {
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://prototip2-69173-default-rtdb.europe-west1.firebasedatabase.app/");
    DatabaseReference myRef = firebaseDatabase.getReference("message");


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



}
