package com.furkanmeydan.prototip2.Adapters;

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
import com.furkanmeydan.prototip2.DataLayer.Callbacks.RequestCallback;
import com.furkanmeydan.prototip2.DataLayer.RequestDAL;
import com.furkanmeydan.prototip2.Models.Request;
import com.furkanmeydan.prototip2.R;
import com.furkanmeydan.prototip2.Views.MainActivity.FragmentRequestSenderProfile;
import com.furkanmeydan.prototip2.Views.MainActivity.MainActivity;

import java.util.ArrayList;

public class RequestsToMeRCLAdapter extends RecyclerView.Adapter<RequestsToMeRCLAdapter.PostHolder> {

    MainActivity activity;
    ArrayList<Request> requestsList;
    RequestDAL requestDAL;

    public RequestsToMeRCLAdapter(ArrayList<Request> requestsList, MainActivity activity) {
        this.requestsList = requestsList;
        this.activity = activity;
        requestDAL = new RequestDAL();
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
        holder.txtRequestText.setText(requestsList.get(position).getRequestText());

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

        holder.btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestDAL.rejectRequest(requestsList.get(position).getPostID(), requestsList.get(position).getPostOwnerID(), requestsList.get(position).getRequestID(), new RequestCallback() {
                    @Override
                    public void onRequestRejected() {
                        super.onRequestRejected();
                        requestsList.remove(position);
                        notifyDataSetChanged();
                    }
                });
            }
        });


    }

    @Override
    public int getItemCount() {
        return requestsList.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView txtHeader, txtNameSurname, txtGender, txtRequestText;
        Button btnDecline;
        public PostHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.requestsToMeRowRCLImg);
            txtHeader = itemView.findViewById(R.id.requestsToMeRowRCLHeader);
            txtNameSurname = itemView.findViewById(R.id.requestsToMeRowRCLNameSurname);
            txtGender = itemView.findViewById(R.id.requestsToMeRowRCLGender);
            btnDecline = itemView.findViewById(R.id.btnrequestsToMeRowRCLDecline);
            txtRequestText = itemView.findViewById(R.id.requestsToMeRowRCLRequestText);

        }
    }
}
