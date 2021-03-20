package com.furkanmeydan.prototip2.View.PostSearchResultDetailActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.furkanmeydan.prototip2.R;


public class FragmentPostSearchPostOwner extends Fragment {

    PostSearchResultDetailActivity activity;
    private TextView txtNameSurname, txtGender, txtBirthday;
    String ownerId;
    public FragmentPostSearchPostOwner() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (PostSearchResultDetailActivity) getActivity();
        //ownerId = activity.post.get
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_search_post_owner, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtNameSurname = view.findViewById(R.id.postSearchPostOwnerName);
        txtGender = view.findViewById(R.id.postSearchPostOwnerGender);
        txtBirthday = view.findViewById(R.id.postSearchPostOwnerBD);
    }
}