package com.furkanmeydan.prototip2.Adapters;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.furkanmeydan.prototip2.DataLayer.MessageDAL;
import com.furkanmeydan.prototip2.Models.Request;
import com.furkanmeydan.prototip2.R;
import com.furkanmeydan.prototip2.Views.MainActivity.FragmentChat;
import com.furkanmeydan.prototip2.Views.MainActivity.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.PostHolder> {
    ArrayList<Request> userList;
    MainActivity mainActivity;
    MessageDAL messageDAL;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    public ChatListAdapter(ArrayList<Request> userList, MainActivity mainActivity, MessageDAL messageDAL) {
        this.userList = userList;
        this.mainActivity = mainActivity;
        this.messageDAL = messageDAL;
    }

    @NonNull
    @NotNull
    @Override
    public ChatListAdapter.PostHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.message_list_rcl_row,parent,false);

        return new ChatListAdapter.PostHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PostHolder holder, int position) {


        if(userList.get(position).getSenderID().equals(auth.getCurrentUser().getUid())){

            Glide.with(mainActivity.getApplicationContext()).load(userList.get(position).getPostOwnerProfilePicture()).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    holder.pbImage.setVisibility(View.INVISIBLE);
                    return false;
                }
            }).apply(RequestOptions.skipMemoryCacheOf(true))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).into(holder.userImage);

            holder.userName.setText(userList.get(position).getPostOwnerName());
            holder.postHeader.setText(userList.get(position).getPostHeader());

        }
        else {
            Glide.with(mainActivity.getApplicationContext()).load(userList.get(position).getSenderImage()).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    holder.pbImage.setVisibility(View.INVISIBLE);
                    return false;
                }
            }).apply(RequestOptions.skipMemoryCacheOf(true))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).into(holder.userImage);
            holder.userName.setText(userList.get(position).getSenderName());

        }

        holder.postHeader.setText(userList.get(position).getPostHeader());

        SimpleDateFormat dateCombinedFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        Date date = new Date(TimeUnit.SECONDS.toMillis(userList.get(position).getPostTimestamp()));

        String dateTime = dateCombinedFormat.format(date);
        String[] dateStringArray = dateTime.split(" ");
        String dateDate = dateStringArray[0];
        String dateTimeTime = dateStringArray[1];
        holder.postTime.setText(dateTimeTime);
        holder.postDate.setText(dateDate);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("request",userList.get(position));
                //Burada bundle oluşturup konuşulacak sayfada DB'ye gitmesi gereken bilgileri göndermek gerekiyor.
                mainActivity.changeFragmentArgs(new FragmentChat(),bundle);
            }
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class PostHolder extends RecyclerView.ViewHolder {
        ImageView userImage;
        TextView userName, postHeader, postDate, postTime;
        ProgressBar pbImage;
        public PostHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.imgUserRclRowMessage);
            userName = itemView.findViewById(R.id.txtUserChatRclName);
            postHeader = itemView.findViewById(R.id.txtMessageRowPostHeader);
            postDate = itemView.findViewById(R.id.txtMessageRowPostDate);
            postTime = itemView.findViewById(R.id.txtMessageRowPostTime);
            pbImage = itemView.findViewById(R.id.progressBarBlockMessage);


        }
    }
}
