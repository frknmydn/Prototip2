package com.furkanmeydan.prototip2.Views.UploadPostActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.Toast;

import com.furkanmeydan.prototip2.DataLayer.LocalDataManager;
import com.furkanmeydan.prototip2.DataLayer.Callbacks.PostCallback;
import com.furkanmeydan.prototip2.DataLayer.PostDAL;
import com.furkanmeydan.prototip2.Models.Post;
import com.furkanmeydan.prototip2.R;
import com.furkanmeydan.prototip2.Views.MainActivity.MainActivity;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.onesignal.OSDeviceState;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class UploadPostActivity extends AppCompatActivity {
    LocalDataManager localDataManager;
    PostDAL postDAL;
    UploadPostDetailFragment uploadPostDetailFragment;
    UploadMapFragment2 uploadMapFragment2;
    FirebaseAuth firebaseAuth;
    String userId;
    TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        uploadMapFragment2 = new UploadMapFragment2();
        uploadPostDetailFragment = new UploadPostDetailFragment();

        setContentView(R.layout.activity_upload_post_new);
        localDataManager = new LocalDataManager();
        changeFragment(uploadPostDetailFragment);
        postDAL = new PostDAL();
        firebaseAuth= FirebaseAuth.getInstance();
        userId= Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        tabLayout = findViewById(R.id.tabLayout);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        changeFragment(uploadPostDetailFragment);
                        Log.d("TAG", "onTabSelected: 1 ");
                        break;

                    case 1:
                        uploadPostDetailFragment.saveDetails();
                        changeFragment(uploadMapFragment2);
                        Log.d("TAG", "onTabSelected: 2 ");
                        break;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



    }


    public void changeFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        //fragmentTransaction.addToBackStack("PostCallback");
        fragmentTransaction.replace(R.id.postUploadFrameLayout2,fragment);
        fragmentTransaction.commit();
    }


    public void AUPDetailFragment(View view){
        changeFragment(new UploadPostDetailFragment());
        //onBackPressed();
    }

    public void AUPMapFragment(View view){
        changeFragment(new UploadMapFragment2());
        //onBackPressed();
    }



    public void AUPUploadPost(View view){
        postDAL.uploadPost(userId, getApplicationContext(), new PostCallback() {
            @Override
            public void onPostAdded(Post post) {
                super.onPostAdded(post);
                Toast.makeText(getApplicationContext(),"Başarılı",Toast.LENGTH_LONG).show();

                try {
                    OSDeviceState device = OneSignal.getDeviceState();
                    String oneSignalID = device.getUserId();

                    long timestamp = post.getTimestamp()-900L;
                    SimpleDateFormat dateCombinedFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(TimeUnit.SECONDS.toMillis(timestamp));
                    String dateTime = dateCombinedFormat.format(date) + " GMT+0300";

                    OneSignal.postNotification(new JSONObject("{'contents': {'en':'Hatirlatma: yolculuk saatinize 15 dakika kalmistir'}, 'include_player_ids': ['" +oneSignalID + "']}").put("send_after",dateTime), null);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("Tag","onesignal CATCHLEDİ LMAO");
                }
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                AUPClearSharedPref();
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