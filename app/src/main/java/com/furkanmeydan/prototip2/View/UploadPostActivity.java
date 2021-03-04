package com.furkanmeydan.prototip2.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.furkanmeydan.prototip2.DataLayer.LocalDataManager;
import com.furkanmeydan.prototip2.DataLayer.PostCallback;
import com.furkanmeydan.prototip2.DataLayer.PostDAL;
import com.furkanmeydan.prototip2.DataLayer.PostDetailDataPasser;
import com.furkanmeydan.prototip2.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class UploadPostActivity extends AppCompatActivity {
    LocalDataManager localDataManager;
    PostDAL postDAL;
    FirebaseAuth firebaseAuth;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_post);
        localDataManager = new LocalDataManager();
        changeFragment(new UploadPostDetailFragment());
        postDAL = new PostDAL();
        firebaseAuth= FirebaseAuth.getInstance();
        userId= Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
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
        postDAL.uploadPost(userId, getApplicationContext(), new PostCallback() {
            @Override
            public void onPostAdded() {
                super.onPostAdded();
                Toast.makeText(getApplicationContext(),"Başarılı",Toast.LENGTH_LONG).show();

                AUPClearSharedPref();

                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    public void AUPClearSharedPref(){
        localDataManager.clearSharedPreference(getApplicationContext());
    }

}