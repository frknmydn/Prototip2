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

    boolean hasMember;
    int i = 0;

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

    private void setVisibilities(){
        Log.d("TAG", "setVisibilities: acceptedPostList.size()" +acceptedPostList.size());
        Log.d("TAG", "setVisibilities: waitingPostList.size()" +waitingPostList.size());
        Log.d("TAG", "setVisibilities: postListHistory.size()" +postListHistory.size());
        Log.d("TAG", "setVisibilities: oncomingPostList.size()" +oncomingPostList.size());

        if(acceptedPostList.size() > 0){
            layoutAcceptedInfo.setVisibility(View.GONE);
            layoutAcceptedContent.setVisibility(View.VISIBLE);
        }
        else{
            layoutAcceptedInfo.setVisibility(View.VISIBLE);
            layoutAcceptedContent.setVisibility(View.VISIBLE);
        }

        if(waitingPostList.size() > 0){
            layoutAwaitingProgress.setVisibility(View.GONE);
            layoutAwaitingContent.setVisibility(View.VISIBLE);
            layoutAwaitingProgress.setVisibility(View.GONE);
        }
        else {
            layoutAwaitingProgress.setVisibility(View.GONE);
            layoutAwaitingContent.setVisibility(View.GONE);
            layoutAwaitingInfo.setVisibility(View.VISIBLE);
        }

        if(postListHistory.size() > 0){
            layoutHistoryInfo.setVisibility(View.GONE);
            layoutHistoryContent.setVisibility(View.VISIBLE);
        }
        else{
            layoutHistoryContent.setVisibility(View.GONE);
            layoutHistoryInfo.setVisibility(View.VISIBLE);
        }

        if(oncomingPostList.size() > 0){
            layoutOnComingProgress.setVisibility(View.GONE);
            layoutOncomingContent.setVisibility(View.VISIBLE);
            layoutOnComingInfo.setVisibility(View.GONE);
        }
        else {
            layoutOnComingProgress.setVisibility(View.GONE);
            layoutOnComingInfo.setVisibility(View.VISIBLE);
            layoutOncomingContent.setVisibility(View.GONE);

        }
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
                        }if(request.getPostTimestamp() - currentTimestamp <= timestampMargin && request.getPostTimestamp() - currentTimestamp > 0){
                            oncomingRequestList.add(request);
                            acceptedRequestList.add(request);


                        }
                        else if(currentTimestamp >= request.getPostTimestamp()){
                            requestListHistory.add(request);

                        }


                    }
                    getPosts(acceptedRequestList,0);
                    getPosts(requestListHistory,1);
                    getPosts(oncomingRequestList,3);
                    if(oncomingRequestList.size()==0){
                        layoutOnComingProgress.setVisibility(View.GONE);
                        layoutOnComingInfo.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
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
    }


    public void getPosts(ArrayList<Request> requestList, final int flag){
        i = 0;
            for(Request request : requestList){
                int requestListSize = requestList.size();
                Log.d("TAG", "getPosts: requestlistsize: "+ requestListSize);
                i+=1;
                hasMember = false;
                String postID = request.getPostID();
                String postOwnerID = request.getPostOwnerID();

                postDAL.getSinglePost(postOwnerID, postID, new PostCallback() {
                    @Override
                    public void getPost(Post post) {
                        super.getPost(post);
                        Log.d("TAG", "getPost: i = " + i);
                        if(flag == 0){
                            hasMember = true;
                            acceptedPostList.add(post);
                            Log.d("Tag","AcceptedPostList size :" + acceptedPostList.size());
                            adapterAccepted.notifyDataSetChanged();

                            if(hasMember){
                                layoutAcceptedContent.setVisibility(View.VISIBLE);
                                layoutAcceptedInfo.setVisibility(View.GONE);
                            }else{
                                layoutAcceptedContent.setVisibility(View.GONE);
                                layoutAcceptedInfo.setVisibility(View.VISIBLE);
                            }

                        }else if(flag == 1){
                            hasMember = true;
                            postListHistory.add(post);
                            Log.d("Tag","HistoryList size :" + postListHistory.size());
                            adapterAcceptedHistory.notifyDataSetChanged();

                            if(hasMember){
                                layoutHistoryContent.setVisibility(View.VISIBLE);
                                layoutHistoryInfo.setVisibility(View.GONE);

                            }else{
                                layoutHistoryContent.setVisibility(View.GONE);
                                layoutHistoryInfo.setVisibility(View.VISIBLE);
                            }

                        }else if(flag == 2){
                            hasMember = true;
                            waitingPostList.add(post);
                            Log.d("Tag","waitingpostList size :" + waitingPostList.size());
                            adapterWaiting.notifyDataSetChanged();

                            if(hasMember){
                                layoutAwaitingContent.setVisibility(View.VISIBLE);
                                layoutAwaitingInfo.setVisibility(View.GONE);

                            }else{
                                layoutAwaitingContent.setVisibility(View.GONE);
                                layoutAwaitingInfo.setVisibility(View.VISIBLE);
                            }
                        }else if(flag == 3){
                            hasMember = true;
                            oncomingPostList.add(post);

                            Log.d("Tag","oncomingPostList size :" + oncomingPostList.size());
                            adapterOncoming.notifyDataSetChanged();

                            if(hasMember){
                            layoutOnComingProgress.setVisibility(View.GONE);
                            layoutOncomingContent.setVisibility(View.VISIBLE);
                            layoutOnComingInfo.setVisibility(View.GONE);
                            }else{
                                layoutOnComingProgress.setVisibility(View.GONE);
                                layoutOncomingContent.setVisibility(View.GONE);
                                layoutOnComingInfo.setVisibility(View.VISIBLE);
                            }

                        }
                        if(i == requestListSize-1 || i == requestListSize){
                            if(acceptedPostList.size() == 0){

                            }
                            if(oncomingPostList.size() == 0){
                                layoutOnComingProgress.setVisibility(View.GONE);
                                layoutOncomingContent.setVisibility(View.GONE);
                                layoutOnComingInfo.setVisibility(View.VISIBLE);
                            }
                            if(waitingPostList.size() == 0){

                            }
                            if(postListHistory.size() == 0){

                            }
                        }
                    }
                });
            }
    }
}