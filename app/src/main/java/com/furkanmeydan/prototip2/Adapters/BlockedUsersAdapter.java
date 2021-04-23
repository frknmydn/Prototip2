package com.furkanmeydan.prototip2.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.furkanmeydan.prototip2.Models.User;
import com.furkanmeydan.prototip2.R;
import com.furkanmeydan.prototip2.Views.MainActivity.MainActivity;

import java.util.ArrayList;

public class BlockedUsersAdapter extends RecyclerView.Adapter<BlockedUsersAdapter.PostHolder>{

    ArrayList<User> blockedUsers;
    MainActivity activity;

    public BlockedUsersAdapter(ArrayList<User> blockedUsers, MainActivity activity) {
        this.blockedUsers = blockedUsers;
        this.activity = activity;
    }

    @NonNull
    @Override
    public BlockedUsersAdapter.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.users_rcl_row,parent,false);

        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {

        holder.txtUserName.setText(blockedUsers.get(position).getNameSurname());
        holder.txtUserGender.setText(blockedUsers.get(position).getGender());
        Glide.with(activity.getApplicationContext()).load(blockedUsers.get(position).getProfilePicture()).apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).into(holder.imgUserPhoto);



    }



    @Override
    public int getItemCount() {
        return blockedUsers.size();
    }


    public static class PostHolder extends RecyclerView.ViewHolder {
        TextView txtUserName, txtUserGender;
        ImageView imgUserPhoto;
        Button btnUnblockUser, btnGoToProfile;
        public PostHolder(@NonNull View itemView) {
            super(itemView);
            txtUserGender = itemView.findViewById(R.id.txtUserRclGender);
            txtUserName = itemView.findViewById(R.id.txtUserRclName);
            imgUserPhoto = itemView.findViewById(R.id.imgUserRclRow);
            btnUnblockUser = itemView.findViewById(R.id.imgUserRclRow);
            btnGoToProfile = itemView.findViewById(R.id.btnRclGoToProfile);


        }
    }
}
