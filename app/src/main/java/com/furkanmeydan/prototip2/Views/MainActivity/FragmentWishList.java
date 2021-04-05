package com.furkanmeydan.prototip2.Views.MainActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.furkanmeydan.prototip2.Adapters.SearchResultRecyclerAdapter;
import com.furkanmeydan.prototip2.DataLayer.PostDAL;
import com.furkanmeydan.prototip2.Models.Post;
import com.furkanmeydan.prototip2.R;

import java.util.ArrayList;


public class FragmentWishList extends Fragment {
    RecyclerView wishRCL;
    SearchResultRecyclerAdapter adapter;
    ArrayList<Post> postList;
    PostDAL postDAL;


    public FragmentWishList() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postList = new ArrayList<>();
        postDAL = new PostDAL();


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

        wishRCL = view.findViewById(R.id.RCLWishList);
        adapter = new SearchResultRecyclerAdapter(postList);

        wishRCL.setAdapter(adapter);
    }
}