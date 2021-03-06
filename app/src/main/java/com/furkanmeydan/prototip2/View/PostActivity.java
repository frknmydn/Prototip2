package com.furkanmeydan.prototip2.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.furkanmeydan.prototip2.R;

public class PostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        changeFragment(new PostSearchFragment());
    }

    public void changeFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //fragmentTransaction.addToBackStack("PostCallback");
        fragmentTransaction.replace(R.id.postContainerLayout,fragment);
        fragmentTransaction.commit();
    }

    public void changeFragmentArgs(Fragment fragment,Bundle args){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragment.setArguments(args);
        fragmentTransaction.replace(R.id.postContainerLayout,fragment);
        fragmentTransaction.commit();
    }
}