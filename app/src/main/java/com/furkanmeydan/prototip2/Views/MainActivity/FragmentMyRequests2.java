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
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.List;


public class FragmentMyRequests2 extends Fragment {


    MyWaitingRequestsAdapter adapterWaiting;
    MyAcceptedRequestsAdapter adapterAccepted;
    MyAcceptedRequestsAdapter adapterAcceptedHistory;
    MyAcceptedRequestsAdapter adapterOncoming;


    ArrayList<Request> acceptedRequestList;
    ArrayList<Request> waitingRequestList;
    ArrayList<Request> requestListHistory;
    ArrayList<Request> oncomingRequestList;

    ArrayList<Post> acceptedPostList;
    ArrayList<Post> waitingPostList;
    ArrayList<Post> postListHistory;
    ArrayList<Post> oncomingPostList;

    RequestDAL requestDAL;
    PostDAL postDAL;
    MainActivity mainActivity;


    ConstraintLayout layoutAccepted, layoutHistory, layoutAwaiting, layoutOncoming, layoutOnComingProgress
            ,layoutOnComingInfo, layoutOncomingContent, layoutAwaitingProgress, layoutAwaitingInfo, layoutAwaitingContent
            ,layoutAcceptedInfo, layoutAcceptedContent, layoutHistoryContent, layoutHistoryInfo;
    RecyclerView RCLAccepted, RCLHistory, RCLAwaiting, RCLOncoming;



    Long currentTimestamp;
    Long timestampMargin = 86400L;
    Long timestampPostWithMargin;

    private TabLayout tabLayout;


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
        oncomingPostList = new ArrayList<>();
        oncomingRequestList = new ArrayList<>();

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

    }

    private void init(View view){


        layoutAccepted = view.findViewById(R.id.consLayoutAccepted);
        layoutAwaiting = view.findViewById(R.id.consLayoutAwaiting);
        layoutHistory = view.findViewById(R.id.consLayoutHistory);
        layoutOncoming = view.findViewById(R.id.consLayoutYaklas);

        layoutOnComingProgress = view.findViewById(R.id.myRequestYaklasProgress);
        layoutOnComingInfo = view.findViewById(R.id.layoutYaklasInfo);
        layoutOncomingContent = view.findViewById(R.id.myRequestYaklasContent);

        layoutAwaitingProgress = view.findViewById(R.id.layoutAwaitingProgress);
        layoutAwaitingInfo  = view.findViewById(R.id.layoutAwaitingInfo);
        layoutAwaitingContent  = view.findViewById(R.id.layoutAwaitingContent);

        layoutAcceptedInfo = view.findViewById(R.id.layoutMyRequestAcceptedInfo);
        layoutAcceptedContent = view.findViewById(R.id.layoutMyRequestAcceptedContent);

        layoutHistoryContent = view.findViewById(R.id.conslayoutMyRequestsHistoryContent);
        layoutHistoryInfo = view.findViewById(R.id.conslayoutMyRequestsHistoryInfo);


        RCLAccepted = view.findViewById(R.id.RCLAcceptedRequest1);
        RCLAwaiting = view.findViewById(R.id.RCLaWaitingRequests);
        RCLHistory = view.findViewById(R.id.RCLRequstHistory);
        RCLOncoming = view.findViewById(R.id.RCLYaklasRequests);


        RCLAccepted.setLayoutManager(new LinearLayoutManager(getContext()));
        RCLAwaiting.setLayoutManager(new LinearLayoutManager(getContext()));
        RCLHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        RCLOncoming.setLayoutManager(new LinearLayoutManager(getContext()));

        adapterAccepted = new MyAcceptedRequestsAdapter(acceptedPostList);
        adapterAcceptedHistory = new MyAcceptedRequestsAdapter(postListHistory);
        adapterWaiting = new MyWaitingRequestsAdapter(waitingPostList);
        adapterOncoming = new MyAcceptedRequestsAdapter(oncomingPostList);

        RCLAccepted.setAdapter(adapterAccepted);
        RCLAwaiting.setAdapter(adapterWaiting);
        RCLHistory.setAdapter(adapterAcceptedHistory);
        RCLOncoming.setAdapter(adapterOncoming);

        tabLayout = view.findViewById(R.id.tabLayoutRequestISent);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        layoutOncoming.setVisibility(View.VISIBLE);
                        layoutAwaiting.setVisibility(View.GONE);
                        layoutAccepted.setVisibility(View.GONE);
                        layoutHistory.setVisibility(View.GONE);
                        Log.d("TAG", "onTabSelected: 1 ");
                        break;
                    case 1:
                        layoutOncoming.setVisibility(View.GONE);
                        layoutAwaiting.setVisibility(View.GONE);
                        layoutAccepted.setVisibility(View.VISIBLE);
                        layoutHistory.setVisibility(View.GONE);
                        Log.d("TAG", "onTabSelected: 2 ");
                        break;
                    case 2:
                        layoutOncoming.setVisibility(View.GONE);
                        layoutAwaiting.setVisibility(View.VISIBLE);
                        layoutAccepted.setVisibility(View.GONE);
                        layoutHistory.setVisibility(View.GONE);
                        Log.d("TAG", "onTabSelected: 3 ");
                        break;
                    case 3:
                        layoutOncoming.setVisibility(View.GONE);
                        layoutAwaiting.setVisibility(View.GONE);
                        layoutAccepted.setVisibility(View.GONE);
                        layoutHistory.setVisibility(View.VISIBLE);
                        Log.d("TAG", "onTabSelected: 4 ");
                        break;


                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



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
                            Log.d("TAG", "onRequestsRetrievedNotNull: "+ acceptedRequestList.size());



                             // 0 - bekleyen kabul edilmiş, 1 - geçmiş kabul edilmiş, 2 - bekleyen onaylanmamış
                        }if(request.getPostTimestamp() - currentTimestamp <= timestampMargin){
                            oncomingRequestList.add(request);

                            if(oncomingRequestList.size() > 0){
                                layoutOnComingProgress.setVisibility(View.GONE);
                                layoutOncomingContent.setVisibility(View.VISIBLE);
                            }
                            else {
                                layoutOnComingProgress.setVisibility(View.GONE);
                                layoutOnComingInfo.setVisibility(View.VISIBLE);
                            }


                        }
                        else if(currentTimestamp >= request.getPostTimestamp()){
                            requestListHistory.add(request);

                        }


                    }
                    Log.d("Tag","AcceptedRequstList size :" + acceptedRequestList.size());
                    Log.d("Tag","historyRequestList size :" + requestListHistory.size());
                    getPosts(acceptedRequestList,0);
                    getPosts(requestListHistory,1);
                    getPosts(oncomingRequestList,3);
                }
            }
        });

        if(requestListHistory.size()>0){
            layoutHistoryContent.setVisibility(View.VISIBLE);
        }
        else{
            layoutHistoryInfo.setVisibility(View.VISIBLE);
        }

        if(acceptedRequestList.size()>0){
            layoutAcceptedContent.setVisibility(View.VISIBLE);
        }
        else {
            layoutAcceptedInfo.setVisibility(View.VISIBLE);
        }

        Log.d("TAGGO", "getAcceptedRequests: " + requestListHistory.size());



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

        if(waitingPostList.size() > 0){
            layoutAwaitingProgress.setVisibility(View.GONE);
            layoutAwaitingContent.setVisibility(View.VISIBLE);
        }
        else{
            layoutAwaitingProgress.setVisibility(View.GONE);
            layoutOnComingInfo.setVisibility(View.VISIBLE);
        }
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
                        }else if(flag == 3){
                            oncomingPostList.add(post);
                            adapterOncoming.notifyDataSetChanged();

                        }
                    }
                });
            }

            if (acceptedPostList.size() >0){
                layoutAcceptedInfo.setVisibility(View.GONE);
                layoutAcceptedContent.setVisibility(View.VISIBLE);
            }
            else {
                layoutAcceptedContent.setVisibility(View.GONE);
                layoutAcceptedInfo.setVisibility(View.VISIBLE);
            }

    }

}