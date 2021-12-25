package com.furkanmeydan.prototip2.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.furkanmeydan.prototip2.Models.Message;
import com.furkanmeydan.prototip2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.installations.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.PostHolder> {
    ArrayList<Message> messageArray;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    Context context;

    public MessageAdapter(ArrayList<Message> messageArray, FirebaseAuth auth, Context context) {
        this.messageArray = messageArray;
        this.auth = auth;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PostHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class PostHolder extends RecyclerView.ViewHolder {
        public PostHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }
    }


}



