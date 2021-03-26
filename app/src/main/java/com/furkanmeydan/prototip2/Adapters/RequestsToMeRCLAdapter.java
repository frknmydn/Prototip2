package com.furkanmeydan.prototip2.Adapters;

import android.content.Intent;
import android.os.Bundle;
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
import com.furkanmeydan.prototip2.Models.Request;
import com.furkanmeydan.prototip2.R;
import com.furkanmeydan.prototip2.Views.MainActivity.FragmentRequestSenderProfile;
import com.furkanmeydan.prototip2.Views.MainActivity.MainActivity;
import com.furkanmeydan.prototip2.Views.PostSearchResultDetailActivity.PostSearchResultDetailActivity;

import java.util.ArrayList;

public class RequestsToMeRCLAdapter extends RecyclerView.Adapter<RequestsToMeRCLAdapter.PostHolder> {

    MainActivity activity;
    ArrayList<Request> requestsList;

    public RequestsToMeRCLAdapter(ArrayList<Request> requestsList, MainActivity activity) {
        this.requestsList = requestsList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public RequestsToMeRCLAdapter.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_requests_to_me_row,parent,false);

        return new RequestsToMeRCLAdapter.PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, final int position) {

        holder.txtHeader.setText(requestsList.get(position).getPostHeader());
        holder.txtNameSurname.setText(requestsList.get(position).getSenderName());
        holder.txtGender.setText(requestsList.get(position).getSenderGender());

        Glide.with(activity.getApplicationContext()).load(requestsList.get(position).getSenderImage()).apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Request request = requestsList.get(position);
                Bundle bundle = new Bundle();

                bundle.putSerializable("request",request);
                activity.changeFragmentArgs(new FragmentRequestSenderProfile(),bundle);

            }
        });


    }

    @Override
    public int getItemCount() {
        return requestsList.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView txtHeader, txtNameSurname, txtGender;
        Button btnAccept, btnDecline;
        public PostHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.requestsToMeRowRCLImg);
            txtHeader = itemView.findViewById(R.id.requestsToMeRowRCLHeader);
            txtNameSurname = itemView.findViewById(R.id.requestsToMeRowRCLNameSurname);
            txtGender = itemView.findViewById(R.id.requestsToMeRowRCLGender);
            btnAccept = itemView.findViewById(R.id.btnrequestsToMeRowRCLAccept);
            btnDecline = itemView.findViewById(R.id.btnrequestsToMeRowRCLDecline);

        }
    }
}
