package com.furkanmeydan.prototip2.Views.MainActivity;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.furkanmeydan.prototip2.Adapters.MyAcceptedRequestsAdapter;
import com.furkanmeydan.prototip2.Adapters.MyWaitingRequestsAdapter;
import com.furkanmeydan.prototip2.DataLayer.Callbacks.PostCallback;
import com.furkanmeydan.prototip2.DataLayer.Callbacks.RequestCallback;
import com.furkanmeydan.prototip2.DataLayer.PostDAL;
import com.furkanmeydan.prototip2.DataLayer.RequestDAL;
import com.furkanmeydan.prototip2.Models.Post;
import com.furkanmeydan.prototip2.Models.Request;
import com.furkanmeydan.prototip2.R;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.List;


public class FragmentMyRequests2 extends Fragment {

    BottomNavigationView bottomRequestView;
    MyWaitingRequestsAdapter adapterWaiting;
    MyAcceptedRequestsAdapter adapterAccepted;
    MyAcceptedRequestsAdapter adapterAcceptedHistory;


    ArrayList<Request> acceptedRequestList;
    ArrayList<Request> waitingRequestList;
    ArrayList<Request> requestListHistory;

    ArrayList<Post> acceptedPostList;
    ArrayList<Post> waitingPostList;
    ArrayList<Post> postListHistory;

    RequestDAL requestDAL;
    PostDAL postDAL;
    MainActivity mainActivity;


    ConstraintLayout layoutAccepted, layoutHistory, layoutAwaiting;
    RecyclerView RCLAccepted, RCLHistory, RCLAwaiting;

    Long currentTimestamp;


    public FragmentMyRequests2() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        requestDAL = new RequestDAL();
        postDAL = new PostDAL();

        acceptedRequestList = new ArrayList<>();
        waitingRequestList = new ArrayList<>();
        requestListHistory = new ArrayList<>();
        acceptedPostList = new ArrayList<>();
        waitingPostList = new ArrayList<>();
        postListHistory = new ArrayList<>();

        mainActivity = (MainActivity) getActivity();
        currentTimestamp = Timestamp.now().getSeconds();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_requests2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        getAcceptedRequests();
        getAwaitingRequests();

        bottomRequestView.setOnNavigationItemSelectedListener(navListener);
    }

    private void init(View view){
        bottomRequestView = view.findViewById(R.id.RequestsBottomNavigation);

        bottomRequestView.bringToFront();

        layoutAccepted = view.findViewById(R.id.consLayoutAccepted);
        layoutAwaiting = view.findViewById(R.id.consLayoutAwaiting);
        layoutHistory = view.findViewById(R.id.consLayoutHistory);

        RCLAccepted = view.findViewById(R.id.RCLAcceptedRequest1);
        RCLAwaiting = view.findViewById(R.id.RCLaWaitingRequests);
        RCLHistory = view.findViewById(R.id.RCLRequstHistory);

        RCLAccepted.setLayoutManager(new LinearLayoutManager(getContext()));
        RCLAwaiting.setLayoutManager(new LinearLayoutManager(getContext()));
        RCLHistory.setLayoutManager(new LinearLayoutManager(getContext()));

        adapterAccepted = new MyAcceptedRequestsAdapter(acceptedPostList);
        adapterAcceptedHistory = new MyAcceptedRequestsAdapter(postListHistory);
        adapterWaiting = new MyWaitingRequestsAdapter(waitingPostList);

        RCLAccepted.setAdapter(adapterAccepted);
        RCLAwaiting.setAdapter(adapterWaiting);
        RCLHistory.setAdapter(adapterAcceptedHistory);



    }

    public void getAcceptedRequests(){
        requestDAL.getAcceptedRequestsISent(new RequestCallback() {
            @Override
            public void onRequestsRetrievedNotNull(List<Request> list) {
                super.onRequestsRetrievedNotNull(list);
                if(list !=null && list.size() > 0) {
                    for (Request request : list) {
                        if (request.getPostTimestamp() >= currentTimestamp) {
                            acceptedRequestList.add(request);
                             // 0 - bekleyen kabul edilmiş, 1 - geçmiş kabul edilmiş, 2 - bekleyen onaylanmamış
                        }
                        else{
                            requestListHistory.add(request);

                        }
                    }
                    Log.d("Tag","AcceptedRequstList size :" + acceptedRequestList.size());
                    Log.d("Tag","historyRequestList size :" + requestListHistory.size());
                    getPosts(acceptedRequestList,0);
                    getPosts(requestListHistory,1);
                }
            }
        });

    }
    public void getAwaitingRequests(){
            requestDAL.getAwatingRequestsISent(new RequestCallback() {
                @Override
                public void onRequestsRetrievedNotNull(List<Request> list) {
                    super.onRequestsRetrievedNotNull(list);
                    if(list !=null && list.size() > 0){
                        for (Request request : list){
                            if(request.getPostTimestamp() >= currentTimestamp){
                                waitingRequestList.add(request);
                            }
                        }
                        getPosts(waitingRequestList,2);
                    }
                }
            });
    }


    public void getPosts(ArrayList<Request> requestList, final int flag){
            for(Request request : requestList){
                String postID = request.getPostID();
                String postOwnerID = request.getPostOwnerID();

                postDAL.getSinglePost(postOwnerID, postID, new PostCallback() {
                    @Override
                    public void getPost(Post post) {
                        super.getPost(post);
                        if(flag == 0){
                            acceptedPostList.add(post);
                            Log.d("Tag","AcceptedPostList size :" + acceptedPostList.size());
                            adapterAccepted.notifyDataSetChanged();

                        }else if(flag == 1){
                            postListHistory.add(post);
                            Log.d("Tag","HistoryList size :" + postListHistory.size());
                            adapterAcceptedHistory.notifyDataSetChanged();

                        }else if(flag == 2){
                            waitingPostList.add(post);
                            Log.d("Tag","waitingpostList size :" + waitingPostList.size());
                            adapterWaiting.notifyDataSetChanged();
                        }
                    }
                });
            }

    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
        if(item.getItemId()==R.id.accepted_requests){
            layoutHistory.setVisibility(View.INVISIBLE);
            layoutAwaiting.setVisibility(View.INVISIBLE);
            layoutAccepted.setVisibility(View.VISIBLE);
            //layoutAccepted.bringToFront();

        }
        else if(item.getItemId() == R.id.accepted_requests_history){
            layoutAwaiting.setVisibility(View.INVISIBLE);
            layoutAccepted.setVisibility(View.INVISIBLE);
            layoutHistory.setVisibility(View.VISIBLE);
        }
        else if (item.getItemId() == R.id.awaiting_requests){
            layoutAccepted.setVisibility(View.INVISIBLE);
            layoutHistory.setVisibility(View.INVISIBLE);
            layoutAwaiting.setVisibility(View.VISIBLE);
        }

        return true;
    };
}