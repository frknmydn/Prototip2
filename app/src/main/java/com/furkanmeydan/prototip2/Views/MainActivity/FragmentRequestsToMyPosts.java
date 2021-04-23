package com.furkanmeydan.prototip2.Views.MainActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
            }
        });

    }
}