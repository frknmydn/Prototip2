package com.furkanmeydan.prototip2.DataLayer;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.furkanmeydan.prototip2.DataLayer.Callbacks.BlockCallback;
import com.furkanmeydan.prototip2.Models.Block;
import com.furkanmeydan.prototip2.Models.CollectionHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Objects;

public class BlockDAL {

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final String userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

    public void blockUser(String userBlocker, String userBlocked, String blockReason, final BlockCallback callback){
        Block block = new Block(userBlocker,userBlocked,blockReason);
        firestore.collection(CollectionHelper.BLOCK_COLLECTION).add(block).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                callback.onUserBlocked();
            }
        });

    }

    public void unblockUser(String userBlocker, String userBlocked, final BlockCallback callback){

        firestore.collection(CollectionHelper.BLOCK_COLLECTION).whereEqualTo(CollectionHelper.BLOCK_BLOCKERID,userBlocker).whereEqualTo(CollectionHelper.BLOCK_BLOCKEDID,userBlocked).get().addOnCompleteListener(task -> {
            if(task.isSuccessful() && task.getResult() !=null){
                if(task.getResult().size() > 0){
                    for(DocumentSnapshot ds : task.getResult().getDocuments()){
                        ds.getReference().delete();

                    }
                    callback.onUserUnblocked();
                }
            }
        });

    }

    public void getBlockedList(final BlockCallback callback){
        firestore.collection(CollectionHelper.BLOCK_COLLECTION).whereEqualTo(CollectionHelper.BLOCK_BLOCKERID,userId).whereNotEqualTo(CollectionHelper.BLOCK_BLOCKREASON,null).get().addOnCompleteListener(task -> {
            if(task.isSuccessful() && task.getResult() !=null){
                if(task.getResult().size() > 0){
                    List<Block> blocks = task.getResult().toObjects(Block.class);
                    callback.onListRetrieved(blocks);
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

    public String getUserId() {
        return userId;
    }
}
