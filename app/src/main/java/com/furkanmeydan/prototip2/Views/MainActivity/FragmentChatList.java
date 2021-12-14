package com.furkanmeydan.prototip2.Views.MainActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.furkanmeydan.prototip2.DataLayer.Callbacks.MessageCallback;
import com.furkanmeydan.prototip2.DataLayer.MessageDAL;
import com.furkanmeydan.prototip2.Models.Request;
import com.furkanmeydan.prototip2.R;

import java.util.ArrayList;
import java.util.List;


public class FragmentChatList extends Fragment {

    ArrayList<Request> profilesForOwner;
    ArrayList<Request> profilesForSender;
    MessageDAL messageDAL;

    public FragmentChatList() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        messageDAL = new MessageDAL();
        profilesForOwner = new ArrayList<>();
        profilesForSender = new ArrayList<>();
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

        messageDAL.getProfilesForRequestSender(new MessageCallback() {
            @Override
            public void onProfilesLoaded(List<Request> users) {
                super.onProfilesLoaded(users);
                profilesForSender.addAll(users);
                Log.d("TAG", "onProfilesLoaded: " + users.get(0).getPostOwnerName());
                Log.d("TAG", "onProfilesLoaded: " + users.get(0).getPostOwnerProfilePicture());
            }
        });

        messageDAL.getProfilesForPostOwner(new MessageCallback() {
            @Override
            public void onProfilesLoaded(List<Request> users) {
                super.onProfilesLoaded(users);
                profilesForOwner.addAll(users);
                Log.d("TAG", "onProfilesLoaded: " + users.get(0).getSenderName());
                Log.d("TAG", "onProfilesLoaded: " + users.get(0).getSenderImage());
            }
        });
    }
}