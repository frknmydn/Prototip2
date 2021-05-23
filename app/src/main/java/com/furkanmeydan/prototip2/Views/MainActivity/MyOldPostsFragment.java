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
import android.widget.Button;

import com.furkanmeydan.prototip2.Adapters.myPostRecyclerAdapter;
import com.furkanmeydan.prototip2.DataLayer.Callbacks.PostCallback;
import com.furkanmeydan.prototip2.DataLayer.PostDAL;
import com.furkanmeydan.prototip2.Models.Post;
import com.furkanmeydan.prototip2.R;

import java.util.ArrayList;
import java.util.List;


public class MyOldPostsFragment extends Fragment {

    myPostRecyclerAdapter postRCLAdapter;
    RecyclerView recyclerView;
    ArrayList<Post> posts;
    MainActivity activity;
    Button btnPastPosts;

    PostDAL postDAL;

    public MyOldPostsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postDAL = new PostDAL();
        activity = (MainActivity) getActivity();
        posts = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_old_posts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.myOldPostsRCL);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        postRCLAdapter = new myPostRecyclerAdapter(activity,posts);
        recyclerView.setAdapter(postRCLAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        getMyPosts();
    }

    public void getMyPosts(){
        postDAL.getMyOlderPosts(new PostCallback() {
            @Override
            public void getMyPosts(List<Post> list) {
                super.getMyPosts(list);
                posts.clear();
                posts.addAll(list);
                postRCLAdapter.notifyDataSetChanged();
            }
        });


    }
}