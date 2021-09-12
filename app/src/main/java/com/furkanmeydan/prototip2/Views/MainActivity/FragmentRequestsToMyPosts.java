package com.furkanmeydan.prototip2.Views.MainActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.furkanmeydan.prototip2.DataLayer.Callbacks.RequestCallback;
import com.furkanmeydan.prototip2.DataLayer.RequestDAL;
import com.furkanmeydan.prototip2.Models.Request;
import com.furkanmeydan.prototip2.Adapters.RequestsToMeRCLAdapter;
import com.furkanmeydan.prototip2.R;

import java.util.ArrayList;
import java.util.List;


public class FragmentRequestsToMyPosts extends Fragment {
        RecyclerView recyclerView;
        RequestsToMeRCLAdapter adapter;
        MainActivity activity;
        RequestDAL requestDAL;
        ArrayList<Request> requests;
        ConstraintLayout layoutInfo,layoutProgress, layoutContent;
        TextView txtMessageInfo;




    public FragmentRequestsToMyPosts() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        requestDAL = new RequestDAL();
        requests = new ArrayList<>();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_requests_to_my_posts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.fragmentRequestsToMyPostsRCL);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RequestsToMeRCLAdapter(requests,activity);
        recyclerView.setAdapter(adapter);
        layoutInfo = view.findViewById(R.id.consLayoutMyPostsRequestsLayout);
        layoutContent = view.findViewById(R.id.layoutRequestToMyPostContent);
        layoutProgress = view.findViewById(R.id.layoutRequestToMeProgress);
        txtMessageInfo =view.findViewById(R.id.txtfragmentRequestToMyPosts);





    }

    @Override
    public void onStart() {
        super.onStart();
        getRequests();
    }

    private void getRequests() {

        requestDAL.getMyRequests(new RequestCallback() {
            @Override
            public void getRequestsToMe(List<Request> list) {

                super.getRequestsToMe(list);
                requests.clear();
                requests.addAll(list);
                adapter.notifyDataSetChanged();
                Log.d("TAG", "getRequestsToMe: "+requests.size());

                if(requests.size()>0){

                    layoutProgress.setVisibility(View.GONE);
                    layoutContent.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();

                }
                else{
                    layoutProgress.setVisibility(View.GONE);
                    layoutInfo.setVisibility(View.VISIBLE);
                }
            }
        });

    }
}