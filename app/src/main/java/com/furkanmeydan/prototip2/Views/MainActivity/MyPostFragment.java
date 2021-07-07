package com.furkanmeydan.prototip2.Views.MainActivity;

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
import android.widget.Button;

import com.furkanmeydan.prototip2.DataLayer.Callbacks.PostCallback;
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
    MainActivity activity;
    Button btnPastPosts;
    ConstraintLayout layoutInfo,layoutProgress,layoutContents;

    PostDAL postDAL;


    public MyPostFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        posts = new ArrayList<>();
        postDAL = new PostDAL();
        activity = (MainActivity) getActivity();

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

        layoutContents = view.findViewById(R.id.myPostsContentsLayout);
        layoutContents.setVisibility(View.INVISIBLE);
        layoutProgress = view.findViewById(R.id.myPostsProgressLayout);
        layoutProgress.setVisibility(View.VISIBLE);
        layoutInfo = view.findViewById(R.id.consLayoutMyPosts);
        layoutInfo.setVisibility(View.INVISIBLE);

        recyclerView = view.findViewById(R.id.myPostRCL);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        postRCLAdapter = new myPostRecyclerAdapter(activity,posts);
        recyclerView.setAdapter(postRCLAdapter);
        btnPastPosts = view.findViewById(R.id.myPostBtnPastPosts);

        //getMyPosts();


        btnPastPosts.setOnClickListener(view1 -> {
            activity.changeFragment(new MyOldPostsFragment());
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        getMyPosts();
    }

    public void getMyPosts(){
        postDAL.getMyPosts(new PostCallback() {
            @Override
            public void getMyPosts(List<Post> list) {
                super.getMyPosts(list);
                posts.clear();
                posts.addAll(list);
                Log.d("TAGGY",String.valueOf(posts.size()));
                if(posts.size()>0){
                    layoutProgress.setVisibility(View.INVISIBLE);
                    layoutContents.setVisibility(View.VISIBLE);
                    postRCLAdapter.notifyDataSetChanged();
                    recyclerView.setVisibility(View.VISIBLE);
                }else{
                    layoutProgress.setVisibility(View.INVISIBLE);
                    layoutInfo.setVisibility(View.VISIBLE);
                }
            }
        });


    }
}