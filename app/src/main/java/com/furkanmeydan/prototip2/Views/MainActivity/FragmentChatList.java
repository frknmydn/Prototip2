package com.furkanmeydan.prototip2.Views.MainActivity;


import android.app.Activity;
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

import com.furkanmeydan.prototip2.Adapters.ChatListAdapter;
import com.furkanmeydan.prototip2.DataLayer.Callbacks.MessageCallback;
import com.furkanmeydan.prototip2.DataLayer.MessageDAL;
import com.furkanmeydan.prototip2.Models.Request;
import com.furkanmeydan.prototip2.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;


public class FragmentChatList extends Fragment {

    ArrayList<Request> profilesForOwner;
    ArrayList<Request> resultList;
    MainActivity activity;

    MessageDAL messageDAL;
    RecyclerView recyclerView;

    ChatListAdapter chatListAdapter;

    public FragmentChatList() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        messageDAL = new MessageDAL();
        profilesForOwner = new ArrayList<>();
        resultList = new ArrayList<>();
        activity = (MainActivity) getActivity();
        chatListAdapter = new ChatListAdapter(profilesForOwner,activity,messageDAL);
        messageDAL.getProfilesForRequestSender(new MessageCallback() {
            @Override
            public void onProfilesLoaded(List<Request> users) {
                super.onProfilesLoaded(users);
                profilesForOwner.addAll(users);
                messageDAL.getProfilesForPostOwner(new MessageCallback() {
                    @Override
                    public void onProfilesLoaded(List<Request> users) {
                        super.onProfilesLoaded(users);
                        profilesForOwner.addAll(users);
                        chatListAdapter.notifyDataSetChanged();
                    }
                });
                Log.d("TAG List", "onViewCreated: " + profilesForOwner.size());
                Log.d("TAG List", "onViewCreated: " + "profilesForOwner.size()");
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        recyclerView = view.findViewById(R.id.RCLFragmentChatList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(chatListAdapter);








        //Eğer bu işlem düzgün şekilde çalışırsa daha sonra ikinci metot içinde
        //tek arraylist içine add yapılaiblir.



    }
}