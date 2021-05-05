package com.furkanmeydan.prototip2.Adapters;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.furkanmeydan.prototip2.DataLayer.Callbacks.PostCallback;
import com.furkanmeydan.prototip2.DataLayer.PostDAL;
import com.furkanmeydan.prototip2.Models.Post;
import com.furkanmeydan.prototip2.R;
import com.furkanmeydan.prototip2.Views.MainActivity.MainActivity;
import com.furkanmeydan.prototip2.Views.PostSearchResultDetailActivity.PostSearchResultDetailActivity;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class myPostRecyclerAdapter extends RecyclerView.Adapter<myPostRecyclerAdapter.PostHolder> {

    private ArrayList<Post> posts;
    MainActivity activity;
    Dialog dialog;
    PostDAL postDAL;

    public myPostRecyclerAdapter(MainActivity activity,ArrayList<Post> posts) {
        this.posts = posts;
        this.activity = activity;
        postDAL = new PostDAL();
    }

    @NonNull
    @Override
    public myPostRecyclerAdapter.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_post_row,parent,false);

        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, final int position) {

        holder.txtDestination.setText(posts.get(position).getDestination());

        long timeStamp = posts.get(position).getTimestamp();
        Log.d("adapter Tag timestamp", String.valueOf(timeStamp));
        SimpleDateFormat dateCombinedFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        Date date = new Date(TimeUnit.SECONDS.toMillis(timeStamp));
       // date.setTime(timeStamp);
        //dateCombinedFormat.format(date);

        String dateTime = dateCombinedFormat.format(date);
        holder.txtDateTime.setText(dateTime);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Tag","Adapter onclick");
                Bundle bundle = new Bundle();
                bundle.putSerializable("post",posts.get(position));
                Intent i = new Intent(activity, PostSearchResultDetailActivity.class);
                i.putExtra("bundle",bundle);
                activity.startActivity(i);


            }
        });
        long ts = Timestamp.now().getSeconds();

        if(posts.get(position).getTimestamp() - 180 <= ts ){
            Log.d("Time", "Bir kere çalışması lazım");
            holder.timeInfo.setImageResource(R.drawable.timer);
        }

        /*
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(activity);
                dialog.setContentView(R.layout.popup_delete_post_row);
                dialog.show();
                Button btnYes = dialog.findViewById(R.id.btnPopupYes);
                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        postDAL.deletePost(posts.get(position), new PostCallback() {
                            @Override
                            public void onPostDeleted() {
                                super.onPostDeleted();
                                posts.remove(position);
                                notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        });
                    }
                });
                Button btnNo = dialog.findViewById(R.id.btnPopupNo);
                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

         */
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }


    public class PostHolder extends RecyclerView.ViewHolder {

        TextView txtDestination, txtDateTime;
        ImageView timeInfo;

        public PostHolder(@NonNull View itemView) {
            super(itemView);
            txtDestination = itemView.findViewById(R.id.postRowDestination);
            txtDateTime = itemView.findViewById(R.id.postRowDate);
            timeInfo = itemView.findViewById(R.id.postRowBtnDelete);

        }
    }
}
