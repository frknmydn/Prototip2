package com.furkanmeydan.prototip2.Adapters;

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
import com.furkanmeydan.prototip2.DataLayer.Callbacks.ProfileCallback;
import com.furkanmeydan.prototip2.DataLayer.Callbacks.RequestCallback;
import com.furkanmeydan.prototip2.DataLayer.ProfileDAL;
import com.furkanmeydan.prototip2.DataLayer.RequestDAL;
import com.furkanmeydan.prototip2.Models.Request;
import com.furkanmeydan.prototip2.Models.User;
import com.furkanmeydan.prototip2.R;
import com.furkanmeydan.prototip2.Views.MainActivity.HomeFragment;
import com.furkanmeydan.prototip2.Views.MainActivity.MainActivity;


import java.util.ArrayList;

public class confimUserRequestSenderAdapter extends RecyclerView.Adapter<confimUserRequestSenderAdapter.PostHolder>{
    ArrayList<Request> acceptedRequest;
    RequestDAL requestDAL;
    ProfileDAL profileDAL;
    MainActivity activity;
    HomeFragment homeFragment;

    public confimUserRequestSenderAdapter(ArrayList<Request> acceptedRequest, RequestDAL requestDAL,ProfileDAL profileDAL, MainActivity mainActivity, HomeFragment homeFragment) {
        this.acceptedRequest = acceptedRequest;
        this.requestDAL = requestDAL;
        this.profileDAL = profileDAL;
        this.activity = mainActivity;
        this.homeFragment = homeFragment;
    }

    @NonNull
    @Override
    public confimUserRequestSenderAdapter.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.accepted_requests_row_new,parent,false);

        return new confimUserRequestSenderAdapter.PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull confimUserRequestSenderAdapter.PostHolder holder, int position) {

        for(Request request : acceptedRequest){
            profileDAL.getProfile(request.getPostOwnerID(), new ProfileCallback() {
                @Override
                public void getUser(User user) {
                    super.getUser(user);
                    holder.txtRequestSenderName.setText(user.getNameSurname());
                    holder.txtRequestSenderGender.setText(user.getGender());
                    holder.txtRequestSenderBirthdate.setText(user.getBirthDate());

                    Glide.with(activity.getApplicationContext()).load(user.getProfilePicture()).listener(new RequestListener<Drawable>() {
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
                            .into(holder.imgRequestSenderPic);
                }
            });

    }
        holder.btnAcceptedRequestConfirmUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestDAL.confirmRequestAsRequestSender(acceptedRequest.get(position).getPostID(),
                        acceptedRequest.get(position).getPostOwnerID(),
                        acceptedRequest.get(position).getRequestID(), new RequestCallback() {
                            @Override
                            public void onRequestUpdated() {
                                super.onRequestUpdated();

                                acceptedRequest.get(position).setSelfConfirmed(1);
                                homeFragment.setRequestListRequestSenderConfirm(acceptedRequest);
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
                requestDAL.rejectRequestAsRequestSender(acceptedRequest.get(position).getPostID(),
                        acceptedRequest.get(position).getPostOwnerID(),
                        acceptedRequest.get(position).getRequestID(), new RequestCallback() {
                            @Override
                            public void onRequestUpdated() {
                                super.onRequestUpdated();

                                acceptedRequest.get(position).setSelfConfirmed(2);
                                homeFragment.setRequestListRequestSenderConfirm(acceptedRequest);
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
