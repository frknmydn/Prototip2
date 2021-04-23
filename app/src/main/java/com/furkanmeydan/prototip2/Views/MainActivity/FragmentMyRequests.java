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
import android.widget.TextView;

import com.furkanmeydan.prototip2.Adapters.MyAcceptedRequestsAdapter;
import com.furkanmeydan.prototip2.Adapters.MyWaitingRequestsAdapter;
import com.furkanmeydan.prototip2.DataLayer.Callbacks.PostCallback;
import com.furkanmeydan.prototip2.DataLayer.PostDAL;
import com.furkanmeydan.prototip2.DataLayer.Callbacks.RequestCallback;
import com.furkanmeydan.prototip2.DataLayer.RequestDAL;
import com.furkanmeydan.prototip2.Models.Post;
import com.furkanmeydan.prototip2.Models.Request;
import com.furkanmeydan.prototip2.R;

import java.util.ArrayList;
import java.util.List;


public class FragmentMyRequests extends Fragment {
    RecyclerView recyclerViewAccepted, recyclerViewWaiting;
    ConstraintLayout constraintLayoutAccepted, constraintLayoutWaiting;
    MyWaitingRequestsAdapter adapterWaiting;
    MyAcceptedRequestsAdapter adapterAccepted;

    TextView txtNoAcceptedRequests,txtNoWaitingRequests;

    ArrayList<Request> acceptedRequestList;
    ArrayList<Request> waitingRequestList;
    ArrayList<Post> acceptedPostList;
    ArrayList<Post> waitingPostList;

    RequestDAL requestDAL;
    PostDAL postDAL;
    MainActivity mainActivity;

    public FragmentMyRequests() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) getActivity();
        requestDAL = new RequestDAL();
        postDAL = new PostDAL();
        acceptedPostList = new ArrayList<>();
        waitingPostList = new ArrayList<>();
        acceptedRequestList = new ArrayList<>();
        waitingRequestList = new ArrayList<>();
        getAcceptedRequests();
        getAwaitingRequests();
        Log.d("Tag","OnAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_requests, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        constraintLayoutAccepted = view.findViewById(R.id.consLayoutAccepted);
        constraintLayoutWaiting = view.findViewById(R.id.consLayotWaiting);
        txtNoAcceptedRequests = view.findViewById(R.id.txtMyRequestsNoAcceptedRequests);
        txtNoWaitingRequests = view.findViewById(R.id.txtMyRequestsNoWaitingRequests);

        recyclerViewAccepted = view.findViewById(R.id.RCLFragmentMyAcceptedRequests);
        recyclerViewWaiting = view.findViewById(R.id.RCLFragmentMyWaitingRequests);
        recyclerViewAccepted.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewWaiting.setLayoutManager(new LinearLayoutManager(getContext()));

        adapterAccepted = new MyAcceptedRequestsAdapter(acceptedPostList);
        adapterWaiting = new MyWaitingRequestsAdapter(waitingPostList);
        recyclerViewAccepted.setAdapter(adapterAccepted);
        recyclerViewWaiting.setAdapter(adapterWaiting);



        /* ilk başta kullanıcının gönderdiği ve onaylanmış bütün istekleri acceptedRequestList içine atmamız gerekiyor.
        Ardından listedeki her elemanın postid'sine göre gönderdiği postları çekip acceptedPostList'e atmak gerekiyor.
        RecyclerView adapterlarının constructoru Post tipinde olduğu için bu yöntem ile yapılması gerekiyor.
        !Callback metotları eksik

         */
    }

    public void getAcceptedRequests(){
        requestDAL.getAcceptedRequestsISent(new RequestCallback() {
            @Override
            public void onRequestsRetrievedNotNull(List<Request> list) {
                super.onRequestsRetrievedNotNull(list);
                acceptedRequestList.addAll(list);
                if(acceptedRequestList !=null && acceptedRequestList.size() > 0){
                    getPosts(acceptedRequestList,1);
                }else{

                }
            }
        });
    }

    public void getAwaitingRequests(){
        requestDAL.getAwatingRequestsISent(new RequestCallback() {
            @Override
            public void onRequestsRetrievedNotNull(List<Request> list) {
                super.onRequestsRetrievedNotNull(list);
                waitingRequestList.addAll(list);
                if(waitingRequestList !=null && waitingRequestList.size()>0){
                    getPosts(waitingRequestList,0);
                }else{

                }
            }
        });
    }

    public void getPosts(ArrayList<Request> requestList, final int flag){
        Log.d("Tag","getPosts");
        for(Request request : requestList){
            String postID = request.getPostID();
            String postOwnerID = request.getPostOwnerID();


            postDAL.getSinglePost(postOwnerID, postID, new PostCallback() {
                @Override
                public void getPost(Post post) {
                    super.getPost(post);
                    if(flag == 1){
                        acceptedPostList.add(post);
                        adapterAccepted.notifyDataSetChanged();
                        recyclerViewAccepted.setVisibility(View.VISIBLE);
                        txtNoAcceptedRequests.setVisibility(View.GONE);
                    }
                    else if(flag == 0){
                        waitingPostList.add(post);
                        adapterWaiting.notifyDataSetChanged();
                        recyclerViewWaiting.setVisibility(View.VISIBLE);
                        txtNoWaitingRequests.setVisibility(View.GONE);
                    }

                }
            });
        }

    }

}