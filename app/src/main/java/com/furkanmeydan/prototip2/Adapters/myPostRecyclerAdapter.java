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

public class myPostRecyclerAdapter extends RecyclerView.Adapter<myPostRecyclerAdapter.PostHolder> {

    private ArrayList<Post> posts;

    public myPostRecyclerAdapter(ArrayList<Post> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public myPostRecyclerAdapter.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_post_row,parent,false);

        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {

        holder.txtDestination.setText(posts.get(position).getDestination());

        long timeStamp = posts.get(position).getTimestamp();
        Log.d("adapter Tag timestamp", String.valueOf(timeStamp));
        SimpleDateFormat dateCombinedFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        Date date = new Date(TimeUnit.SECONDS.toMillis(timeStamp));
       // date.setTime(timeStamp);
        //dateCombinedFormat.format(date);

        String dateTime = dateCombinedFormat.format(date);
        holder.txtDateTime.setText(dateTime);


    }

    @Override
    public int getItemCount() {
        return posts.size();
    }


    public class PostHolder extends RecyclerView.ViewHolder {

        TextView txtDestination, txtDateTime;

        public PostHolder(@NonNull View itemView) {
            super(itemView);
            txtDestination = itemView.findViewById(R.id.postRowDestination);
            txtDateTime = itemView.findViewById(R.id.postRowDate);

        }
    }
}
