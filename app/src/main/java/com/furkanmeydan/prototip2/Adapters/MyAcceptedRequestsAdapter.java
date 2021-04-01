package com.furkanmeydan.prototip2.Adapters;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.furkanmeydan.prototip2.Models.Post;
import com.furkanmeydan.prototip2.R;
import com.furkanmeydan.prototip2.Views.PostSearchResultDetailActivity.PostSearchResultDetailActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MyAcceptedRequestsAdapter extends RecyclerView.Adapter<MyAcceptedRequestsAdapter.PostHolder> {
    ArrayList<Post> acceptedPosts;

    public MyAcceptedRequestsAdapter(ArrayList<Post> acceptedPosts) {
        this.acceptedPosts = acceptedPosts;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.post_search_result_rcl_row,parent,false);

        return new MyAcceptedRequestsAdapter.PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, final int position) {
        holder.txtPostHeader.setText(acceptedPosts.get(position).getDestination());
        holder.txtPostPassengerCount.setText((String.valueOf(acceptedPosts.get(position).getPassengerCount())));

        long timeStamp = acceptedPosts.get(position).getTimestamp();
        Log.d("adapter Tag timestamp", String.valueOf(timeStamp));
        SimpleDateFormat dateCombinedFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = new Date(TimeUnit.SECONDS.toMillis(timeStamp));
        String dateTime = dateCombinedFormat.format(date);
        holder.txtPostTime.setText(dateTime);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("post",acceptedPosts.get(position));

                Intent i = new Intent(view.getContext(), PostSearchResultDetailActivity.class);
                i.putExtra("bundle",bundle);
                view.getContext().startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return acceptedPosts.size();
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
