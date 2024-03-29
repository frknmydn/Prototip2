package com.furkanmeydan.prototip2.Views.UploadPostActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.furkanmeydan.prototip2.DataLayer.LocalDataManager;
import com.furkanmeydan.prototip2.DataLayer.Callbacks.PostCallback;
import com.furkanmeydan.prototip2.DataLayer.NotificationManager;
import com.furkanmeydan.prototip2.DataLayer.PostDAL;
import com.furkanmeydan.prototip2.Models.Car;
import com.furkanmeydan.prototip2.Models.ConnectionChecker;
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

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class UploadPostActivity extends AppCompatActivity {
    LocalDataManager localDataManager;
    PostDAL postDAL;
    UploadPostDetailFragment uploadPostDetailFragment;
    UploadMapFragment2 uploadMapFragment2;
    FirebaseAuth firebaseAuth;
    String userId;
    TabLayout tabLayout;
    public Car car;
    public PopupWindow carPopup;
    public View popupView;
    public ConstraintLayout layoutCar;
    public TextView txtCarInfo;
    public Button btnAddPost;
    public Post postToPush;
    ConnectionChecker connectionChecker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        car = null;
        postToPush = new Post();
        connectionChecker = new ConnectionChecker();
        uploadMapFragment2 = new UploadMapFragment2();
        uploadPostDetailFragment = new UploadPostDetailFragment();

        setContentView(R.layout.activity_upload_post_new);
        localDataManager = new LocalDataManager();
        try {
            changeFragment(uploadPostDetailFragment);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        postDAL = new PostDAL();
        firebaseAuth= FirebaseAuth.getInstance();
        userId= Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        carPopup = new PopupWindow(inflater.inflate(R.layout.popup_select_car,null,false), ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
        carPopup.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupView = carPopup.getContentView();


        tabLayout = findViewById(R.id.tabLayout);
        btnAddPost = findViewById(R.id.btnUploadPostNew);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        try {
                            changeFragment(uploadPostDetailFragment);
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.d("TAG", "onTabSelected: 1 ");
                        break;

                    case 1:
                        uploadPostDetailFragment.saveDetails();
                        try {
                            changeFragment(uploadMapFragment2);
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
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


    public void changeFragment(Fragment fragment) throws IOException, InterruptedException {
        if(connectionChecker.isConnected()){
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.popBackStack();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            //fragmentTransaction.addToBackStack("PostCallback");
            fragmentTransaction.replace(R.id.postUploadFrameLayout2,fragment);
            fragmentTransaction.commit();
        }
        else{
            connectionChecker.showWindow(this);
        }

    }


    public void AUPDetailFragment(View view) throws IOException, InterruptedException {
        changeFragment(new UploadPostDetailFragment());
        //onBackPressed();
    }

    public void AUPMapFragment(View view) throws IOException, InterruptedException {
        changeFragment(new UploadMapFragment2());
        //onBackPressed();
    }



    public void AUPUploadPost(View view){
        uploadPostDetailFragment.saveDetails();
        postDAL.uploadPost(userId, getApplicationContext(),car,postToPush, new PostCallback() {
            @Override
            public void onPostAdded(Post post) {
                super.onPostAdded(post);


                //dialog.setContentView(R.layout.popup_fill_profile);
                //dialog.show();
                try {
                    OSDeviceState device = OneSignal.getDeviceState();
                    long timestamp = post.getTimestamp()-900L;
                    SimpleDateFormat dateCombinedFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(TimeUnit.SECONDS.toMillis(timestamp));
                    String dateTime = dateCombinedFormat.format(date) + " GMT+0300";

                    //OneSignal.postNotification(new JSONObject("{'contents': {'en':'Hatirlatma: yolculuk saatinize 15 dakika kalmistir'}, 'include_external_user_ids': ['" +userId + "']}").put("send_after",dateTime), null);
                    Thread t1 = new Thread() {
                        @Override
                        public void run() {
                            NotificationManager notificationManager = new NotificationManager(post.getOwnerID(),dateTime,"Yolculuk saatinize 15 dakika kalmistir","Hatirlatma");
                            try {
                                notificationManager.NotificationForFuture();

                            } catch(Throwable t) {
                                Log.d("Tag","Yarra yedik");
                                t.printStackTrace();
                            }

                        }
                    };
                    t1.start();
                    try {
                        t1.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("Tag","onesignal CATCHLEDİ LMAO");
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(UploadPostActivity.this);
                builder.setTitle("BAŞARILI");
                builder.setMessage("Oluşturduğunuz ilan başarılı şekilde yüklenmiştir");
                builder.setIcon(R.drawable.ic_green_check);
                builder.setCancelable(false);
                builder.setPositiveButton("Kapat", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        AUPClearSharedPref();
                        startActivity(i);
                        finish();
                    }
                });
                builder.show();

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