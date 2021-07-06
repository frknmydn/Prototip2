package com.furkanmeydan.prototip2.Views.MainActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.furkanmeydan.prototip2.Adapters.BlockedUsersAdapter;
import com.furkanmeydan.prototip2.DataLayer.BlockDAL;
import com.furkanmeydan.prototip2.DataLayer.Callbacks.BlockCallback;
import com.furkanmeydan.prototip2.DataLayer.Callbacks.ProfileCallback;
import com.furkanmeydan.prototip2.DataLayer.ProfileDAL;
import com.furkanmeydan.prototip2.Models.Block;
import com.furkanmeydan.prototip2.Models.User;
import com.furkanmeydan.prototip2.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentBlockList extends Fragment {
    RecyclerView rclBlockedList;
    MainActivity activity;
    BlockedUsersAdapter adapter;
    ArrayList<User> listBlockedUsers;
    BlockDAL blockDAL;
    ProfileDAL profileDAL;
    ConstraintLayout layoutInfo;


    public FragmentBlockList() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        blockDAL = new BlockDAL();
        profileDAL = new ProfileDAL();
        listBlockedUsers = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_block_list, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rclBlockedList = view.findViewById(R.id.RCLBlockedUsers);
        rclBlockedList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BlockedUsersAdapter(listBlockedUsers,activity);
        rclBlockedList.setAdapter(adapter);
        layoutInfo = view.findViewById(R.id.consLayoutblock);


    }

    @Override
    public void onStart() {
        super.onStart();
        getData();
    }

    public void getData(){
        blockDAL.getBlockedList(new BlockCallback() {
            @Override
            public void onListRetrieved(List<Block> list) {
                super.onListRetrieved(list);
                for(Block block : list){
                    profileDAL.getProfile(block.getUserBlockedID(), new ProfileCallback() {
                        @Override
                        public void getUser(User user) {
                            super.getUser(user);
                            listBlockedUsers.clear();
                            listBlockedUsers.add(user);
                            adapter.notifyDataSetChanged();
                            if(listBlockedUsers.size() > 0){
                                layoutInfo.setVisibility(View.INVISIBLE);
                                rclBlockedList.setVisibility(View.VISIBLE);

                            }
                        }
                    });
                }


            }
        });

    }
}