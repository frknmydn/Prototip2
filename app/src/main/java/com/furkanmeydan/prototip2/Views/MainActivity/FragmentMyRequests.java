package com.furkanmeydan.prototip2.Views.MainActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.furkanmeydan.prototip2.Adapters.MyAcceptedRequestsAdapter;
import com.furkanmeydan.prototip2.Adapters.MyWaitingRequestsAdapter;
import com.furkanmeydan.prototip2.DataLayer.PostDAL;
import com.furkanmeydan.prototip2.DataLayer.RequestCallback;
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

    ArrayList<Request> acceptedRequestList;
    ArrayList<Request> waitingRequestList;
    ArrayList<Post> acceptedPostList;
    ArrayList<Post> waitingPostList;

    RequestDAL requestDAL;
    PostDAL postDAL;
    MainActivity mainActivity;
    Request request;

    public FragmentMyRequests() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity = (MainActivity) getActivity();
        requestDAL = new RequestDAL();
        postDAL = new PostDAL();
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
        recyclerViewAccepted = view.findViewById(R.id.RCLFragmentMyAcceptedRequests);
        recyclerViewWaiting = view.findViewById(R.id.RCLFragmentMyWaitingRequests);



        /* ilk başta kullanıcının gönderdiği ve onaylanmış bütün istekleri acceptedRequestList içine atmamız gerekiyor.
        Ardından listedeki her elemanın postid'sine göre gönderdiği postları çekip acceptedPostList'e atmak gerekiyor.
        RecyclerView adapterlarının constructoru Post tipinde olduğu için bu yöntem ile yapılması gerekiyor.
        !Callback metotları eksik

         */
    }
}