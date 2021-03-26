package com.furkanmeydan.prototip2.Views.UploadPostActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.furkanmeydan.prototip2.DataLayer.LocalDataManager;
import com.furkanmeydan.prototip2.DataLayer.PostCallback;
import com.furkanmeydan.prototip2.DataLayer.PostDAL;
import com.furkanmeydan.prototip2.R;
import com.furkanmeydan.prototip2.Views.MainActivity.MainActivity;
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

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    public void AUPClearSharedPref(){
        //Clearsharedpref bütün sharedpref bilgilerini sildiğinden user'ın silinmemesi için teker teker post detaylarını siliyoruz
        localDataManager.removeSharedPreference(getApplicationContext(), "lat_1");
        localDataManager.removeSharedPreference(getApplicationContext(), "lat_2");
        localDataManager.removeSharedPreference(getApplicationContext(), "lng_1");
        localDataManager.removeSharedPreference(getApplicationContext(), "lng_2");
        localDataManager.removeSharedPreference(getApplicationContext(), "cardetail");
        localDataManager.removeSharedPreference(getApplicationContext(), "description");
        localDataManager.removeSharedPreference(getApplicationContext(), "destination");
        localDataManager.removeSharedPreference(getApplicationContext(), "city");
        localDataManager.removeSharedPreference(getApplicationContext(), "passengercount");
        localDataManager.removeSharedPreference(getApplicationContext(), "timestamp");
        localDataManager.removeSharedPreference(getApplicationContext(), "date");
        localDataManager.removeSharedPreference(getApplicationContext(), "time");
    }

}