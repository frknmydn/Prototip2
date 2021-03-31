package com.furkanmeydan.prototip2.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.furkanmeydan.prototip2.Models.Post;
import com.furkanmeydan.prototip2.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MyWaitingRequestsAdapter extends RecyclerView.Adapter<MyWaitingRequestsAdapter.PostHolder>{

    ArrayList<Post> waitingPosts;

    public MyWaitingRequestsAdapter(ArrayList<Post> waitingPosts) {
        this.waitingPosts = waitingPosts;
    }

    @NonNull
    @Override
    public MyWaitingRequestsAdapter.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.post_search_result_rcl_row,parent,false);

        return new MyWaitingRequestsAdapter.PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        holder.txtPostHeader.setText(waitingPosts.get(position).getDestination());
        holder.txtPostPassengerCount.setText(waitingPosts.get(position).getPassengerCount());

        long timeStamp = waitingPosts.get(position).getTimestamp();
        Log.d("adapter Tag timestamp", String.valueOf(timeStamp));
        SimpleDateFormat dateCombinedFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = new Date(TimeUnit.SECONDS.toMillis(timeStamp));
        String dateTime = dateCombinedFormat.format(date);
        holder.txtPostTime.setText(dateTime);
    }


    @Override
    public int getItemCount() {
        return waitingPosts.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder {
        TextView txtPostHeader, txtPostTime, txtPostPassengerCount;
        public PostHolder(@NonNull View itemView) {
            super(itemView);
            txtPostHeader = itemView.findViewById(R.id.txtRCLPostHeaderRow);
            txtPostTime = itemView.findViewById(R.id.txtRCLPostTimeRow);
            txtPostPassengerCount = itemView.findViewById(R.id.txtRCLPostPCROW);
        }
    }
}
