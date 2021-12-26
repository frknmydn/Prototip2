package com.furkanmeydan.prototip2.Adapters;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
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
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MessageAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    ArrayList<Message> messageArray;
    String currentuserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    Context context;

    public MessageAdapter(ArrayList<Message> messageArray, Context context) {
        this.messageArray = messageArray;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view;

        if(viewType == VIEW_TYPE_MESSAGE_SENT){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_message_row_me,parent, false);
            return new SentMessageHolder(view);
        }else if(viewType == VIEW_TYPE_MESSAGE_RECEIVED){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_message_row_other,parent,false);
            return new ReceivedMessageHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        Message message = messageArray.get(position);
        if(holder.getItemViewType() == VIEW_TYPE_MESSAGE_SENT){
            ((SentMessageHolder) holder).bind(message);
        }else if(holder.getItemViewType() == VIEW_TYPE_MESSAGE_RECEIVED){
            ((ReceivedMessageHolder) holder).bind(message);
        }

    }

    @Override
    public int getItemViewType(int position) {

        Message message = messageArray.get(position);
        if(message!=null){
            if(message.getSender().equals(currentuserID)){
                return VIEW_TYPE_MESSAGE_SENT;
            }else{
                return VIEW_TYPE_MESSAGE_RECEIVED;
            }
        }
        return 0;

    }

    @Override
    public int getItemCount() {
        return messageArray.size();
    }

    public static class SentMessageHolder extends  RecyclerView.ViewHolder{
        TextView messageText, timeText;
        public SentMessageHolder(@NonNull View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_gchat_message_me);
            timeText = (TextView) itemView.findViewById(R.id.text_gchat_timestamp_me);
        }
        void bind (Message message){

            SimpleDateFormat dateCombinedFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date date = new Date(TimeUnit.SECONDS.toMillis(message.getTimestamp()));
            String dateTime = dateCombinedFormat.format(date);
            String[] dateStringArray = dateTime.split(" ");
            String dateDate = dateStringArray[0];
            String dateTimeTime = dateStringArray[1];

            messageText.setText(message.getMessage());
            timeText.setText(dateTimeTime);
        }
    }

    public static class ReceivedMessageHolder extends RecyclerView.ViewHolder {
       TextView messageText,timeText;

        public ReceivedMessageHolder(@NonNull View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.text_gchat_message_other);
            timeText = (TextView) itemView.findViewById(R.id.text_gchat_timestamp_other);
        }
        void bind (Message message){

            SimpleDateFormat dateCombinedFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date date = new Date(TimeUnit.SECONDS.toMillis(message.getTimestamp()));
            String dateTime = dateCombinedFormat.format(date);
            String[] dateStringArray = dateTime.split(" ");
            String dateDate = dateStringArray[0];
            String dateTimeTime = dateStringArray[1];

            messageText.setText(message.getMessage());
            timeText.setText(dateTimeTime);
        }
    }




}



