package com.furkanmeydan.prototip2.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.furkanmeydan.prototip2.DataLayer.LocalDataManager;
import com.furkanmeydan.prototip2.DataLayer.PostDetailDataPasser;
import com.furkanmeydan.prototip2.R;

public class UploadPostActivity extends AppCompatActivity {
    LocalDataManager localDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_post);
        localDataManager = new LocalDataManager();
        changeFragment(new UploadPostDetailFragment());
    }


    public void changeFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //fragmentTransaction.addToBackStack("PostCallback");
        fragmentTransaction.replace(R.id.postUploadFrameLayout,fragment);
        fragmentTransaction.commit();
    }

    public void AUPDetailFragment(View view){
        changeFragment(new UploadPostDetailFragment());
        //onBackPressed();
    }

    public void AUPMapFragment(View view){
        changeFragment(new UploadMapFragment());
        //onBackPressed();
    }

    public void AUPUploadPost(View view){
        String sharedPrefString = localDataManager.getSharedPreference(this,"marker1city",null);

        String sharedPrefDropString = localDataManager.getSharedPreference(this,"city",null);


        Log.d("Tag","sharedPrefString: "+ sharedPrefString);
        Log.d("Tag","sharedPrefDropString: "+ sharedPrefDropString);

        if(sharedPrefString.equals(sharedPrefDropString)){
            Log.d("Tag","sharedPrefString: "+ sharedPrefString);
            Log.d("Tag","sharedPrefDropString: "+ sharedPrefDropString);
        }


    }

}