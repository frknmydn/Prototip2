package com.furkanmeydan.prototip2.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.furkanmeydan.prototip2.DataLayer.RequestDAL;
import com.furkanmeydan.prototip2.Models.Request;
import com.furkanmeydan.prototip2.R;
import com.furkanmeydan.prototip2.Views.PostActivity.PostActivity;

import java.util.ArrayList;

public class AcceptedRequestAdapter extends RecyclerView.Adapter<AcceptedRequestAdapter.PostHolder> {

    ArrayList<Request> acceptedRequest;
    PostActivity postActivity;
    RequestDAL requestDAL;

    public AcceptedRequestAdapter(ArrayList<Request> acceptedRequest, PostActivity postActivity) {
        this.acceptedRequest = acceptedRequest;
        this.postActivity = postActivity;
        requestDAL = new RequestDAL();
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.accepted_requests_row,parent,false);

        return new AcceptedRequestAdapter.PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        holder.txtRequestSenderName.setText(acceptedRequest.get(position).getSenderName());
        holder.txtRequestSenderGender.setText(acceptedRequest.get(position).getSenderGender());
        holder.txtRequestSenderBirthdate.setText(acceptedRequest.get(position).getSenderBirthdate());

        Glide.with(postActivity.getApplicationContext()).load(acceptedRequest.get(position).getSenderImage()).apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).into(holder.imgRequestSenderPic);
    }

    @Override
    public int getItemCount() {
        return acceptedRequest.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder {

        ImageView imgRequestSenderPic;
        TextView txtRequestSenderName, txtRequestSenderBirthdate, txtRequestSenderGender;
        public PostHolder(@NonNull View itemView) {
            super(itemView);

            imgRequestSenderPic = itemView.findViewById(R.id.imgAcceptedRequestPic);
            txtRequestSenderName = itemView.findViewById(R.id.txtAcceptedRequestName);
            txtRequestSenderBirthdate = itemView.findViewById(R.id.txtAcceptedRequestBirthdate);
            txtRequestSenderGender = itemView.findViewById(R.id.txtAcceptedRequestGender);
        }
    }
}
