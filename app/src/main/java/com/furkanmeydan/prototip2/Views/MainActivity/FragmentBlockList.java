package com.furkanmeydan.prototip2.Views.MainActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.furkanmeydan.prototip2.Adapters.BlockedUsersAdapter;
import com.furkanmeydan.prototip2.Models.User;
import com.furkanmeydan.prototip2.R;

import java.util.ArrayList;

public class FragmentBlockList extends Fragment {
    RecyclerView rclBlockedList;
    MainActivity activity;
    BlockedUsersAdapter adapter;
    ArrayList<User> listBlockedUsers;



    public FragmentBlockList() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
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
        adapter = new BlockedUsersAdapter(listBlockedUsers,activity);
        rclBlockedList.setAdapter(adapter);
        adapter.notifyDataSetChanged();



    }
}