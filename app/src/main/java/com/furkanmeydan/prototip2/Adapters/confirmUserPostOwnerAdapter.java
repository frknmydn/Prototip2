package com.furkanmeydan.prototip2.Adapters;
//confirmUserPostOwnerAdapter

import android.graphics.drawable.Drawable;
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
import com.furkanmeydan.prototip2.DataLayer.Callbacks.RequestCallback;
import com.furkanmeydan.prototip2.DataLayer.RequestDAL;
import com.furkanmeydan.prototip2.Models.Request;
import com.furkanmeydan.prototip2.R;
import com.furkanmeydan.prototip2.Views.MainActivity.HomeFragment;
import com.furkanmeydan.prototip2.Views.MainActivity.MainActivity;


import java.util.ArrayList;

public class confirmUserPostOwnerAdapter extends RecyclerView.Adapter<confirmUserPostOwnerAdapter.PostHolder>{
    ArrayList<Request> acceptedRequest;
    RequestDAL requestDAL;
    MainActivity activity;
    HomeFragment homeFragment;

    public confirmUserPostOwnerAdapter(ArrayList<Request> acceptedRequest, RequestDAL requestDAL, MainActivity mainActivity, HomeFragment homeFragment) {
        this.acceptedRequest = acceptedRequest;
        this.requestDAL = requestDAL;
        this.activity = mainActivity;
        this.homeFragment = homeFragment;
    }

    @NonNull
    @Override
    public confirmUserPostOwnerAdapter.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.accepted_requests_row_new,parent,false);

        return new confirmUserPostOwnerAdapter.PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull confirmUserPostOwnerAdapter.PostHolder holder, int position) {


        holder.txtRequestSenderName.setText(acceptedRequest.get(position).getSenderName());
        holder.txtRequestSenderGender.setText(acceptedRequest.get(position).getSenderGender());
        holder.txtRequestSenderBirthdate.setText(acceptedRequest.get(position).getSenderBirthdate());

        Glide.with(activity.getApplicationContext()).load(acceptedRequest.get(position).getSenderImage()).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                holder.imagePB.setVisibility(View.INVISIBLE);
                return false;
            }
        }).apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(holder.imgRequestSenderPic);;


        holder.btnAcceptedRequestConfirmUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestDAL.confirmRequestAsPostOwner(acceptedRequest.get(position).getPostID(),
                        acceptedRequest.get(position).getPostOwnerID(),
                        acceptedRequest.get(position).getRequestID(), new RequestCallback() {
                            @Override
                            public void onRequestUpdated() {
                                super.onRequestUpdated();
                                acceptedRequest.get(position).setOwnerConfirmed(1);
                                homeFragment.setRequestListPostOwnerConfirm(acceptedRequest);
                                holder.btnAcceptedRequestDeclineUser.setVisibility(View.GONE);
                                holder.btnAcceptedRequestConfirmUser.setVisibility(View.GONE);
                                notifyDataSetChanged();
                            }
                        });
            }
        });
        holder.btnAcceptedRequestDeclineUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestDAL.rejectRequestAsPostOwner(acceptedRequest.get(position).getPostID(),
                        acceptedRequest.get(position).getPostOwnerID(),
                        acceptedRequest.get(position).getRequestID(), new RequestCallback() {
                            @Override
                            public void onRequestUpdated() {
                                super.onRequestUpdated();
                                acceptedRequest.get(position).setOwnerConfirmed(2);
                                homeFragment.setRequestListPostOwnerConfirm(acceptedRequest);
                                holder.btnAcceptedRequestDeclineUser.setVisibility(View.GONE);
                                holder.btnAcceptedRequestConfirmUser.setVisibility(View.GONE);
                                notifyDataSetChanged();
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return acceptedRequest.size();
    }

    public static class PostHolder extends RecyclerView.ViewHolder {

        ImageView imgRequestSenderPic;
        TextView txtRequestSenderName, txtRequestSenderBirthdate, txtRequestSenderGender;
        Button btnAcceptedRequestConfirmUser, btnAcceptedRequestDeclineUser;
        ProgressBar imagePB;

        public PostHolder(@NonNull View itemView) {
            super(itemView);

            imgRequestSenderPic = itemView.findViewById(R.id.imgAcceptedRequestPic);
            txtRequestSenderName = itemView.findViewById(R.id.txtAcceptedRequestName);
            txtRequestSenderBirthdate = itemView.findViewById(R.id.txtAcceptedRequestBirthdate);
            txtRequestSenderGender = itemView.findViewById(R.id.txtAcceptedRequestGender);
            btnAcceptedRequestConfirmUser = itemView.findViewById(R.id.btnAcceptedRequestConfirmUser);
            btnAcceptedRequestDeclineUser = itemView.findViewById(R.id.btnAcceptedRequestDeclineUser);
            imagePB = itemView.findViewById(R.id.acceptedRowPB);


            btnAcceptedRequestDeclineUser.setVisibility(View.VISIBLE);
            btnAcceptedRequestConfirmUser.setVisibility(View.VISIBLE);
        }
    }
}

