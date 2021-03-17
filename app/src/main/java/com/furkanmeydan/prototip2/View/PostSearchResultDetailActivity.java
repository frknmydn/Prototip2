package com.furkanmeydan.prototip2.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.furkanmeydan.prototip2.DataLayer.ProfileDAL;
import com.furkanmeydan.prototip2.Model.Post;
import com.furkanmeydan.prototip2.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class PostSearchResultDetailActivity extends AppCompatActivity {
    ProfileDAL profileDAL;
    Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_search_result_detail);

        init();

        Log.d("TagggUUU",post.getCarDetail());
    }


    public void init(){

        Intent i = getIntent();
        Bundle bundle = i.getBundleExtra("bundle");

        if(bundle !=null){
            post = (Post) bundle.getSerializable("post");
        }
        profileDAL = new ProfileDAL();
    }

    public void changeFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //fragmentTransaction.addToBackStack("PostCallback");
        fragmentTransaction.replace(R.id.PostSearchResultContainer,fragment);
        fragmentTransaction.commit();
    }

    public void changeFragmentArgs(Fragment fragment,Bundle args){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragment.setArguments(args);
        fragmentTransaction.replace(R.id.PostSearchResultContainer,fragment);
        fragmentTransaction.commit();
    }
}