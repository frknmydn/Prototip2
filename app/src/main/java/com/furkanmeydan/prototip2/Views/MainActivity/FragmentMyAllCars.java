package com.furkanmeydan.prototip2.Views.MainActivity;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.furkanmeydan.prototip2.Adapters.MyCarsAdapter;
import com.furkanmeydan.prototip2.DataLayer.Callbacks.AppDatabase;
import com.furkanmeydan.prototip2.DataLayer.Callbacks.CarCallback;
import com.furkanmeydan.prototip2.DataLayer.CarDAL;
import com.furkanmeydan.prototip2.Models.Car;
import com.furkanmeydan.prototip2.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class FragmentMyAllCars extends Fragment {

    RecyclerView rclMyCars;
    Button btnGoToAddCarPage;
    MainActivity activity;
    ConstraintLayout layoutContent, layoutInfo, layoutProgress;
    ArrayList<Car> carList;
    CarDAL carDAL;
    AppDatabase appDatabase;
    MyCarsAdapter adapter;

    String userid;

    public FragmentMyAllCars() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (MainActivity) getActivity();
        carList = new ArrayList<>();
        carDAL = new CarDAL();
        appDatabase = Room.databaseBuilder(activity,
                AppDatabase.class, "carsDB").build();

        userid = activity.firebaseAuth.getUid();

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("TAG", "onStart: 1");
        try {
            getCarsWithRoom();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("TAG", "onStart: 2");
        Log.d("TAG", "onStart: carlist" + carList.size());


        Log.d("zaaaa", "onStart: " + carList.size());
        if(carList.size()==0){
            getCars();
        }
        Log.d("TAG", "onStart: 4");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_my_all_cars, container, false);

    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layoutContent = view.findViewById(R.id.layoutMyAllCarsContent);
        layoutInfo = view.findViewById(R.id.layoutMyAllCarsInfo);
        layoutProgress = view.findViewById(R.id.layoutMyAllCarsProgress);

        rclMyCars = view.findViewById(R.id.RCLMyAllCars);
        rclMyCars.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MyCarsAdapter(carList,activity);
        rclMyCars.setAdapter(adapter);
        btnGoToAddCarPage = view.findViewById(R.id.btnGoToAddCarPage);



        layoutProgress.setVisibility(View.VISIBLE);

        /*
        AsyncTask.execute(() -> {

            carList.addAll((ArrayList<Car>) appDatabase.carDao().getAllCars());
            //carList = (ArrayList<Car>) appDatabase.carDao().getAllCars();
            adapter.notifyDataSetChanged();
            layoutProgress.setVisibility(View.GONE);
            layoutContent.setVisibility(View.VISIBLE);

            Log.d("TAGGO", "run: cars" + carList.size());
        });

         */

        Log.d("TAGGO", "onViewCreated: carlist size 0  --" + carList.size());

        btnGoToAddCarPage.setOnClickListener(v -> activity.changeFragment(new FragmentMyCars()));

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
                carList.addAll(appDatabase.carDao().getAllCars());
            }
        };
        t1.start();
        t1.join();
        if(carList.size()>0){
            layoutProgress.setVisibility(View.GONE);
            layoutContent.setVisibility(View.VISIBLE);
        }
    }

    public void getCars(){

        Log.d("TAGGO", "onViewCreated: carlist size 1 -- " + carList.size());
        if(carList.size()==0){
            carDAL.getMyCars(userid,new CarCallback() {
                @Override
                public void getMyCars(List<Car> cars) {
                    super.getMyCars(cars);
                    Log.d("TAGGO", "onViewCreated: carlist size 2 -- " + cars.size());
                    //carList.clear();
                    carList.addAll(cars);
                    adapter.notifyDataSetChanged();
                    Log.d("TAGGO", "onViewCreated: carlist size 2 -- " + carList.size());
                    layoutProgress.setVisibility(View.GONE);
                    layoutContent.setVisibility(View.VISIBLE);
                    //layoutInfo.setVisibility(View.VISIBLE);
                }

            });
        }

    }
}