package com.furkanmeydan.prototip2.Views.PostActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.furkanmeydan.prototip2.DataLayer.LocalDataManager;
import com.furkanmeydan.prototip2.DataLayer.PostDAL;
import com.furkanmeydan.prototip2.Models.ConnectionChecker;
import com.furkanmeydan.prototip2.R;
import com.furkanmeydan.prototip2.Views.UploadPostActivity.UploadMapFragment2;
import com.furkanmeydan.prototip2.Views.UploadPostActivity.UploadPostDetailFragment;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;

public class PostActivity extends AppCompatActivity {
    Button btnSearch;
    TabLayout searchTabLayout;
    LocalDataManager localDataManager;
    PostSearchFragment postSearchFragment;
    Bundle bundle;
    PostDAL postDAL;
    FragmentPostSearchResultMap_new fragmentPostSearchResultMapNew;
    ConnectionChecker connectionChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        connectionChecker = new ConnectionChecker();
        bundle = new Bundle();
        postDAL = new PostDAL();
        localDataManager = new LocalDataManager();
        postSearchFragment = new PostSearchFragment();
        fragmentPostSearchResultMapNew = new FragmentPostSearchResultMap_new();
        try {
            changeFragment(postSearchFragment);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        btnSearch = findViewById(R.id.btnSearchPostNew);
        searchTabLayout = findViewById(R.id.tabLayoutPostSearch);

        searchTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        try {
                            changeFragment(postSearchFragment);
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                        fragmentPostSearchResultMapNew.counter = 0;
                        break;
                    case 1:
                        postSearchFragment.saveDetails();
                        try {
                            changeFragment(fragmentPostSearchResultMapNew);
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
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

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //postSearchFragment.saveDetails();

                double lat1 = bundle.getDouble("userlat1");
                double lng1 = bundle.getDouble("userlng1");
                double lat2 = bundle.getDouble("userlat2");
                double lng2 = bundle.getDouble("userlng2");

                Log.d("Tag", "lat1: "+lat1);
                Log.d("Tag", "lng1: "+lng1);
                Log.d("Tag", "lat2: "+lat2);
                Log.d("Tag", "lng2: "+lng2);

                /*
                if(lat1 == 0d || lng1 == 0d || lat2 == 0d || lng2 == 0d){
                    Toast.makeText(getApplicationContext(),"Lütfen haritada inis ve kalkis noktalarinizi belirtiniz",Toast.LENGTH_LONG).show();
                }

                else{
                    int direction = postDAL.findDirection(lat1, lng1, lat2, lng2);
                    bundle.putInt("direction", direction);

                    //Kullanıcı istek gönderirken kullanmak için


                    changeFragmentArgs(new PostSearchResultFragment(), bundle);
                }

                 */

                int direction = postDAL.findDirection(lat1, lng1, lat2, lng2);
                bundle.putInt("direction", direction);

                //Kullanıcı istek gönderirken kullanmak için

                if(bundle.getString("gender") != null){
                    try {
                        changeFragmentArgs(new PostSearchResultFragment(), bundle);
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    searchTabLayout.setVisibility(View.GONE);
                }

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
            fragmentTransaction.replace(R.id.consLayoutPostSearch,fragment);
            fragmentTransaction.commit();
        }
        else{
            connectionChecker.showWindow(this);
        }


    }

    public void changeFragmentArgs(Fragment fragment,Bundle args) throws IOException, InterruptedException {
        if(connectionChecker.isConnected()){
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragment.setArguments(args);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.replace(R.id.consLayoutPostSearch,fragment);
            fragmentTransaction.commit();
        }
        else{
            connectionChecker.showWindow(this);
        }


    }
}