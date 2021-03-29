package com.furkanmeydan.prototip2.Views.PostSearchResultDetailActivity;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.furkanmeydan.prototip2.Adapters.AcceptedRequestAdapter;
import com.furkanmeydan.prototip2.DataLayer.RequestCallback;
import com.furkanmeydan.prototip2.DataLayer.RequestDAL;
import com.furkanmeydan.prototip2.Models.Post;
import com.furkanmeydan.prototip2.Models.Request;
import com.furkanmeydan.prototip2.R;
import com.furkanmeydan.prototip2.Views.PostActivity.PostActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FragmentPostSearchResultDetailAcceptedRequests extends Fragment {
    RecyclerView recyclerView;
    ArrayList<Request> requestList;
    AcceptedRequestAdapter adapter;
    PostSearchResultDetailActivity activity;
    TextView txtInfo;
    Post post;

    RequestDAL requestDAL;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("Tag","AcceptedRequests onCreate");
        requestDAL = new RequestDAL();
        activity = (PostSearchResultDetailActivity) getActivity();
        post = activity.post;
        requestList = activity.requestList;


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_search_accepted_requests, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        txtInfo= view.findViewById(R.id.txtFragmentAcceptedRequestInformation);

        recyclerView = view.findViewById(R.id.fragmentAcceptedRequestsRCL);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AcceptedRequestAdapter(requestList,activity);
        recyclerView.setAdapter(adapter);
        Log.d("Tag","AcceptedRequests onViewCreated");

        if(requestList.size()>0){
            adapter.notifyDataSetChanged();
            txtInfo.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        else{
            txtInfo.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        //getData();
        Log.d("Tag","AcceptedRequests onStart");
    }

    public void getData(){
        requestDAL.getAcceptedRequests(post.getPostID(), post.getOwnerID(), new RequestCallback() {
            @Override
            public void onRequestsRetrievedNotNull(List<Request> list) {
                super.onRequestsRetrievedNotNull(list);
                requestList.addAll(list);
                adapter.notifyDataSetChanged();
                txtInfo.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onRequestsRetrievedNull() {
                super.onRequestsRetrievedNull();
            }
        });
    }





    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("Tag","onSaveInstanceState");
        outState.putSerializable("requestList",(Serializable) requestList);
    }
}