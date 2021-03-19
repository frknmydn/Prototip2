package com.furkanmeydan.prototip2.Model;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.furkanmeydan.prototip2.R;
import com.furkanmeydan.prototip2.View.PostSearchResultDetailActivity.PostSearchResultDetailActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class SearchResultRecyclerAdapter extends RecyclerView.Adapter<SearchResultRecyclerAdapter.PostHolder> {
    private ArrayList<Post> posts;

    public SearchResultRecyclerAdapter(ArrayList<Post> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public SearchResultRecyclerAdapter.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.post_search_result_rcl_row,parent,false);

        return new SearchResultRecyclerAdapter.PostHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, final int position) {
        holder.txtHeader.setText(posts.get(position).getDestination());

        long timeStamp = posts.get(position).getTimestamp();
        Log.d("adapter Tag timestamp", String.valueOf(timeStamp));
        SimpleDateFormat dateCombinedFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        Date date = new Date(TimeUnit.SECONDS.toMillis(timeStamp));
        // date.setTime(timeStamp);
        //dateCombinedFormat.format(date);

        String dateTime = dateCombinedFormat.format(date);
        holder.txtTime.setText(dateTime);

        holder.txtPassengerCount.setText(String.valueOf(posts.get(position).getPassengerCount()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Post post = posts.get(position);
                Bundle bundle = new Bundle();

                bundle.putSerializable("post",post);
                Intent i = new Intent(view.getContext(), PostSearchResultDetailActivity.class);
                i.putExtra("bundle",bundle);
                view.getContext().startActivity(i);

            }
        });

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder {
        TextView txtHeader, txtTime, txtPassengerCount;
        public PostHolder(@NonNull View itemView) {
            super(itemView);

            txtHeader = itemView.findViewById(R.id.txtRCLPostHeaderRow);
            txtTime = itemView.findViewById(R.id.txtRCLPostTimeRow);
            txtPassengerCount = itemView.findViewById(R.id.txtRCLPostPCROW);


        }
    }
}
