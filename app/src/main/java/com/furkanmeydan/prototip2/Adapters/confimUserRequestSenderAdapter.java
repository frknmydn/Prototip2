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
import com.furkanmeydan.prototip2.DataLayer.Callbacks.RequestCallback;
import com.furkanmeydan.prototip2.DataLayer.RequestDAL;
import com.furkanmeydan.prototip2.Models.Request;
import com.furkanmeydan.prototip2.R;
import com.furkanmeydan.prototip2.Views.MainActivity.HomeFragment;
import com.furkanmeydan.prototip2.Views.MainActivity.MainActivity;


import java.util.ArrayList;

public class confimUserRequestSenderAdapter extends RecyclerView.Adapter<confimUserRequestSenderAdapter.PostHolder>{
    ArrayList<Request> acceptedRequest;
    RequestDAL requestDAL;
    MainActivity activity;
    HomeFragment homeFragment;

    public confimUserRequestSenderAdapter(ArrayList<Request> acceptedRequest, RequestDAL requestDAL, MainActivity mainActivity, HomeFragment homeFragment) {
        this.acceptedRequest = acceptedRequest;
        this.requestDAL = requestDAL;
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


        holder.txtRequestSenderName.setText(acceptedRequest.get(position).getSenderName());
        holder.txtRequestSenderGender.setText(acceptedRequest.get(position).getSenderGender());
        holder.txtRequestSenderBirthdate.setText(acceptedRequest.get(position).getSenderBirthdate());

        Glide.with(activity.getApplicationContext()).load(acceptedRequest.get(position)
                .getSenderImage()).apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(holder.imgRequestSenderPic);

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

                                holder.btnAcceptedRequestDeclineUser.setVisibility(View.GONE);
                                holder.btnAcceptedRequestConfirmUser.setVisibility(View.GONE);
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

                                holder.btnAcceptedRequestDeclineUser.setVisibility(View.GONE);
                                holder.btnAcceptedRequestConfirmUser.setVisibility(View.GONE);
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

        public PostHolder(@NonNull View itemView) {
            super(itemView);

            imgRequestSenderPic = itemView.findViewById(R.id.imgAcceptedRequestPic);
            txtRequestSenderName = itemView.findViewById(R.id.txtAcceptedRequestName);
            txtRequestSenderBirthdate = itemView.findViewById(R.id.txtAcceptedRequestBirthdate);
            txtRequestSenderGender = itemView.findViewById(R.id.txtAcceptedRequestGender);
            btnAcceptedRequestConfirmUser = itemView.findViewById(R.id.btnAcceptedRequestConfirmUser);
            btnAcceptedRequestDeclineUser = itemView.findViewById(R.id.btnAcceptedRequestDeclineUser);

            btnAcceptedRequestDeclineUser.setVisibility(View.VISIBLE);
            btnAcceptedRequestConfirmUser.setVisibility(View.VISIBLE);

        }
    }
}