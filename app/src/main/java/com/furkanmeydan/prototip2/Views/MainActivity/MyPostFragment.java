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

import com.furkanmeydan.prototip2.DataLayer.PostCallback;
import com.furkanmeydan.prototip2.DataLayer.PostDAL;
import com.furkanmeydan.prototip2.Models.Post;
import com.furkanmeydan.prototip2.Adapters.myPostRecyclerAdapter;
import com.furkanmeydan.prototip2.R;

import java.util.ArrayList;
import java.util.List;

public class MyPostFragment extends Fragment {

    myPostRecyclerAdapter postRCLAdapter;
    RecyclerView recyclerView;
    ArrayList<Post> posts;

    PostDAL postDAL;


    public MyPostFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        posts = new ArrayList<>();
        postDAL = new PostDAL();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.myPostRCL);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        postRCLAdapter = new myPostRecyclerAdapter(posts);
        recyclerView.setAdapter(postRCLAdapter);

        getMyPosts();


    }

    public void getMyPosts(){
        postDAL.getMyPosts(new PostCallback() {
            @Override
            public void getMyPosts(List<Post> list) {
                super.getMyPosts(list);
                posts.addAll(list);
                postRCLAdapter.notifyDataSetChanged();
            }
        });


    }
}