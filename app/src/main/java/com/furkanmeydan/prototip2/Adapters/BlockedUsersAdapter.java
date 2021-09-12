package com.furkanmeydan.prototip2.Adapters;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.furkanmeydan.prototip2.DataLayer.BlockDAL;
import com.furkanmeydan.prototip2.DataLayer.Callbacks.BlockCallback;
import com.furkanmeydan.prototip2.Models.User;
import com.furkanmeydan.prototip2.R;
import com.furkanmeydan.prototip2.Views.MainActivity.MainActivity;
import com.furkanmeydan.prototip2.Views.MainActivity.ProfileFragment;

import java.util.ArrayList;

public class BlockedUsersAdapter extends RecyclerView.Adapter<BlockedUsersAdapter.PostHolder>{

    ArrayList<User> blockedUsers;
    MainActivity activity;
    BlockDAL blockDAL;

    public BlockedUsersAdapter(ArrayList<User> blockedUsers, MainActivity activity) {
        this.blockedUsers = blockedUsers;
        this.activity = activity;
        blockDAL = new BlockDAL();
    }

    @NonNull
    @Override
    public BlockedUsersAdapter.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.blocked_users_rcl_row,parent,false);

        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, final int position) {

        Log.d("TAG", "onBindViewHolder: "+blockedUsers.size());

        holder.txtUserName.setText(blockedUsers.get(position).getNameSurname());
        holder.txtUserGender.setText(blockedUsers.get(position).getGender());
        Glide.with(activity.getApplicationContext()).load(blockedUsers.get(position).getProfilePicture()).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                holder.blockedUserPB.setVisibility(View.INVISIBLE);
                return false;
            }
        }).apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).into(holder.imgUserPhoto);


        holder.btnGoToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = blockedUsers.get(position);
                Bundle bundle = new Bundle();

                bundle.putSerializable("user", user);
                activity.changeFragmentArgs(new ProfileFragment(),bundle);
            }
        });

        holder.btnUnblockUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blockDAL.unblockUser(blockDAL.getUserId(), blockedUsers.get(position).getUserID(), new BlockCallback() {
                    @Override
                    public void onUserUnblocked() {
                        super.onUserUnblocked();
                        blockDAL.unblockUser(blockedUsers.get(position).getUserID(), blockDAL.getUserId(), new BlockCallback() {
                            @Override
                            public void onUserUnblocked() {
                                super.onUserUnblocked();
                                blockedUsers.remove(position);
                                notifyDataSetChanged();
                            }
                        });
                    }
                });
            }
        });
    }



    @Override
    public int getItemCount() {
        return blockedUsers.size();
    }


    public static class PostHolder extends RecyclerView.ViewHolder {
        TextView txtUserName, txtUserGender;
        ImageView imgUserPhoto;
        Button btnUnblockUser, btnGoToProfile;
        ProgressBar blockedUserPB;
        public PostHolder(@NonNull View itemView) {
            super(itemView);
            txtUserGender = itemView.findViewById(R.id.txtUserRclGender);
            txtUserName = itemView.findViewById(R.id.txtUserRclName);
            imgUserPhoto = itemView.findViewById(R.id.imgUserRclRow);
            btnUnblockUser = itemView.findViewById(R.id.btnUnBlockUser);
            btnGoToProfile = itemView.findViewById(R.id.btnRclGoToProfile);
            blockedUserPB = itemView.findViewById(R.id.progressBarBlock);
        }
    }
}
