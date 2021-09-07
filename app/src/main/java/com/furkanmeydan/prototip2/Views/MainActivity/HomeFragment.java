package com.furkanmeydan.prototip2.Views.MainActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.furkanmeydan.prototip2.DataLayer.Callbacks.AppDatabase;
import com.furkanmeydan.prototip2.DataLayer.Callbacks.CarCallback;
import com.furkanmeydan.prototip2.DataLayer.Callbacks.ProfileCallback;
import com.furkanmeydan.prototip2.DataLayer.CarDAL;
import com.furkanmeydan.prototip2.DataLayer.ProfileDAL;
import com.furkanmeydan.prototip2.Models.Car;
import com.furkanmeydan.prototip2.Models.User;
import com.furkanmeydan.prototip2.R;
import com.furkanmeydan.prototip2.Views.PostActivity.PostActivity;
import com.furkanmeydan.prototip2.Views.UploadPostActivity.UploadPostActivity;
import com.onesignal.OSDeviceState;
import com.onesignal.OneSignal;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class HomeFragment extends Fragment {


    private Button btnAddPost, btnSearchPost, btnConfirmUser;
    MainActivity mainActivity;

    Dialog dialog;

    String userid;

    ProfileDAL profileDAL;

    AppDatabase appDatabase;
    CarDAL carDAL;
    ArrayList<Car> carList;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity = (MainActivity) getActivity();
        profileDAL = new ProfileDAL();
        carDAL = new CarDAL();
        appDatabase = Room.databaseBuilder(mainActivity,
                AppDatabase.class, "carsDB").build();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




        assert mainActivity != null;
        userid = Objects.requireNonNull(mainActivity.firebaseAuth.getCurrentUser()).getUid();
        Log.d("TAG", "userid " + userid);

        btnConfirmUser =view.findViewById(R.id.btnConfirmUser);
        btnAddPost = view.findViewById(R.id.btnAddPostFragmentH);
        btnSearchPost = view.findViewById(R.id.btnSearchPost);
        carList = new ArrayList<>();

        OneSignal.setExternalUserId(userid);

        btnAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Log.d("Tag", "Onclick'e basıldı");
                profileDAL.getProfile(userid, new ProfileCallback() {
                    @Override
                    public void getUser(User user) {
                        super.getUser(user);

                        carList = new ArrayList<>();

                        try {
                            getCarsWithRoom();

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                        if(carList.size() == 0){
                            Log.d("Tag", "ifcarlist.size  == 0");
                            getCars();
                        }

                        else{
                            Intent i = new Intent(mainActivity, UploadPostActivity.class);
                            startActivity(i);
                        }

                    }

                    @Override
                    public void nullUser() {
                        Log.d("Tag", "nullUser metodu çalıştı");
                        dialog = new Dialog(mainActivity);
                        dialog.setContentView(R.layout.popup_fill_profile);
                        dialog.show();
                        Button btnProfileDialog = dialog.findViewById(R.id.btnPopupGoToProfile);
                        btnProfileDialog.setOnClickListener(view1 -> {
                            mainActivity.changeFragment(new SignUpFragment());
                            dialog.dismiss();
                        });
                    }

                });


            }
        });


        // Search Sayfasına gitme
        btnSearchPost.setOnClickListener(view12 -> {
            Intent i = new Intent(getActivity(), PostActivity.class);
            startActivity(i);
        });

        btnConfirmUser.setOnClickListener(view13 -> {
            mainActivity.changeFragment(new FragmentConfirmUser());
        });
    }

    public void getCarsWithRoom() throws InterruptedException {
        /*
        AsyncTask.execute(() -> {
            carList.addAll(appDatabase.carDao().getAllCars());
        });

         */

        Thread t1 = new Thread() {
            @Override
            public void run() {
                if(carList.size()<=0){
                    Log.d("Tag","GetCarsWithRoom için");
                    carList.addAll(appDatabase.carDao().loadAllCarsByUserID(userid));
                }
            }
        };
        t1.start();
        t1.join();

    }

    public void getCars(){

        Log.d("TAGGO", "onViewCreated: carlist size 1 -- " + carList.size());
        if(carList.size()==0){
            carDAL.getMyCars(userid, new CarCallback() {
                @Override
                public void getMyCars(List<Car> cars) {
                    super.getMyCars(cars);
                    for(Car car : cars){
                        //AsyncTask.execute(() -> appDatabase.carDao().insertAll(car));
                        Thread t1 = new Thread(){
                            @Override
                            public void run() {
                                super.run();
                                appDatabase.carDao().insertAll(car);
                            }
                        };
                        t1.start();
                        try {
                            t1.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Intent i = new Intent(mainActivity, UploadPostActivity.class);
                    startActivity(i);
                }

                @Override
                public void nullCar() {
                    super.nullCar();
                    if(carList.size() == 0){
                        Toast.makeText(mainActivity,"Araç yok",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }
}
