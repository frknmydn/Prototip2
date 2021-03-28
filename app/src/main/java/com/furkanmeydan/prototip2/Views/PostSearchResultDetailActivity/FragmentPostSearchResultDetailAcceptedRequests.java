package com.furkanmeydan.prototip2.Views.PostSearchResultDetailActivity;

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

import java.util.ArrayList;

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
        requestList = new ArrayList<>();
        activity = (PostSearchResultDetailActivity) getActivity();
        requestDAL = new RequestDAL();
        post = activity.post;


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


    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("Tag","AcceptedRequests onStart");
        getData();
    }

    public void getData(){
        requestDAL.getAcceptedRequests(post.getPostID(), post.getOwnerID(), new RequestCallback() {
            @Override
            public void onRequestsRetrievedNotNull() {
                super.onRequestsRetrievedNotNull();
                txtInfo.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onRequestsRetrievedNull() {
                super.onRequestsRetrievedNull();
            }
        });
    }
}