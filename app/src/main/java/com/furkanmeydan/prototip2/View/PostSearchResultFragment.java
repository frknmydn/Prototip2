package com.furkanmeydan.prototip2.View;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.furkanmeydan.prototip2.DataLayer.PostCallback;
import com.furkanmeydan.prototip2.DataLayer.PostDAL;
import com.furkanmeydan.prototip2.Model.Post;
import com.furkanmeydan.prototip2.R;
import com.google.firebase.Timestamp;

import java.sql.Time;
import java.util.List;

public class PostSearchResultFragment extends Fragment {
    PostDAL postDAL;
    PostActivity postActivity;
    private String city;
    private String gender;
    private long timestamp1;
    private long timestamp2;

    String genderr;

    public PostSearchResultFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        postDAL = new PostDAL();
        postActivity = (PostActivity) getActivity();
        if (getArguments() != null) {
            city = getArguments().getString("city");
            gender = getArguments().getString("gender");
            timestamp1 = getArguments().getLong("timestamp1");
            timestamp2 = getArguments().getLong("timestamp2");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_search_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        postDAL.getPosts(timestamp1, timestamp2, gender, city, postActivity, new PostCallback() {
            @Override
            public void getPosts(List<Post> list) {
                super.getPosts(list);

                for (Post post : list){
                    String docId = post.getDescription();
                    Log.d("TagPostGetFragment", docId);
                }

                //Log.d("TagPostGetFragment", list.get(0).getDestination());

            }
        });
        Log.d("TagSearchResult",city);Log.d("TagSearchResult",gender);
        Log.d("TagSearchResult", String.valueOf(timestamp1));Log.d("TagSearchResult", String.valueOf(timestamp2));
    }
}