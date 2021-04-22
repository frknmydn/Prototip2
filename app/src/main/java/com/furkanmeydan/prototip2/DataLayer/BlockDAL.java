package com.furkanmeydan.prototip2.DataLayer;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.furkanmeydan.prototip2.Models.Block;
import com.furkanmeydan.prototip2.Models.CollectionHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class BlockDAL {

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private String userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

    public void blockUser(String userBlocker, String userBlocked, String blockReason, final BlockCallback callback){
        Block block = new Block(userBlocker,userBlocked,blockReason);
        firestore.collection(CollectionHelper.BLOCK_COLLECTION).add(block).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()){
                    callback.onUserBlocked();
                }
            }
        });

    }

    public boolean checkReason(String blockReason, Context context){
        String error = "";

        if(blockReason == null || blockReason.equals("") ){
            Toast.makeText(context,"Lütfen bir sebep belirtiniz",Toast.LENGTH_LONG).show();
            error += "HATA";
        }

        return error.equals("");

    }

}
