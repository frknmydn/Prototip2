package com.furkanmeydan.prototip2.Views.MainActivity;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.furkanmeydan.prototip2.Adapters.SearchResultRecyclerAdapter;
import com.furkanmeydan.prototip2.DataLayer.Callbacks.PostCallback;
import com.furkanmeydan.prototip2.DataLayer.PostDAL;
import com.furkanmeydan.prototip2.Models.Post;
import com.furkanmeydan.prototip2.R;

import java.util.ArrayList;
import java.util.List;


public class FragmentWishList extends Fragment {
    MainActivity activity;
    RecyclerView wishRCL;
    SearchResultRecyclerAdapter adapter;
    ArrayList<Post> postList;
    PostDAL postDAL;
    ConstraintLayout layoutWishInfo;
    ImageView imgInfo;


    public FragmentWishList() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        postList = new ArrayList<>();
        postDAL = new PostDAL();
        activity = (MainActivity) getActivity();


        getData();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wish_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layoutWishInfo = view.findViewById(R.id.layoutQuestions);
        imgInfo = view.findViewById(R.id.imageView3);
        wishRCL = view.findViewById(R.id.RCLWishList);
        wishRCL.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SearchResultRecyclerAdapter(postList, activity);
        wishRCL.setAdapter(adapter);

    }


    public void getData(){
        postDAL.getWishList(new PostCallback() {
            @Override
            public void OnWishListRetrieved(List<Post> list) {
                super.OnWishListRetrieved(list);

                postList.addAll(list);
                adapter.notifyDataSetChanged();

                if(postList!=null){
                    layoutWishInfo.setVisibility(View.GONE);
                    wishRCL.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}