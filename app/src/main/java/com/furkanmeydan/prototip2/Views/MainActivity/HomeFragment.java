package com.furkanmeydan.prototip2.Views.MainActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.furkanmeydan.prototip2.Adapters.confimUserRequestSenderAdapter;
import com.furkanmeydan.prototip2.Adapters.confirmUserPostOwnerAdapter;
import com.furkanmeydan.prototip2.DataLayer.Callbacks.AppDatabase;
import com.furkanmeydan.prototip2.DataLayer.Callbacks.CarCallback;
import com.furkanmeydan.prototip2.DataLayer.Callbacks.ProfileCallback;
import com.furkanmeydan.prototip2.DataLayer.Callbacks.RequestCallback;
import com.furkanmeydan.prototip2.DataLayer.CarDAL;
import com.furkanmeydan.prototip2.DataLayer.ProfileDAL;
import com.furkanmeydan.prototip2.DataLayer.RequestDAL;
import com.furkanmeydan.prototip2.Models.Car;
import com.furkanmeydan.prototip2.Models.Request;
import com.furkanmeydan.prototip2.Models.User;
import com.furkanmeydan.prototip2.R;
import com.furkanmeydan.prototip2.Views.PostActivity.PostActivity;
import com.furkanmeydan.prototip2.Views.UploadPostActivity.UploadPostActivity;
import com.onesignal.OneSignal;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class HomeFragment extends Fragment {

    private Button btnAddPost, btnSearchPost;
    MainActivity mainActivity;
    LinearLayout confirmLayoutPostOwner;
    LinearLayout confirmLayoutRequestSender;
    RecyclerView rclConfirmPostOwner;
    RecyclerView rclConfirmRequestSender;
    ArrayList<Request> requestListPostOwnerConfirm;
    ArrayList<Request> requestListRequestSenderConfirm;
    confirmUserPostOwnerAdapter adapterConfirmUserPostOwner;
    confimUserRequestSenderAdapter adapterConfirmUserRequestSender;

    Dialog dialog;

    String userid;

    ProfileDAL profileDAL;
    RequestDAL requestDAL;

    AppDatabase appDatabase;
    CarDAL carDAL;
    ArrayList<Car> carList;
    Dialog popUpNoCar;
    NestedScrollView nestedScrollView;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity = (MainActivity) getActivity();
        profileDAL = new ProfileDAL();
        carDAL = new CarDAL();
        requestDAL = new RequestDAL();
        appDatabase = Room.databaseBuilder(mainActivity,
                AppDatabase.class, "carsDB").build();
        requestListPostOwnerConfirm = new ArrayList<>();
        requestListRequestSenderConfirm = new ArrayList<>();
        adapterConfirmUserPostOwner = new confirmUserPostOwnerAdapter(requestListPostOwnerConfirm,requestDAL,mainActivity,this);
        adapterConfirmUserRequestSender = new confimUserRequestSenderAdapter(requestListRequestSenderConfirm,requestDAL,profileDAL,mainActivity,this);


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                        return true;
                    }
                }
                return false;
            }
        });
        return view;


    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        assert mainActivity != null;
        userid = Objects.requireNonNull(mainActivity.firebaseAuth.getCurrentUser()).getUid();
        Log.d("TAG", "userid " + userid);

        btnAddPost = view.findViewById(R.id.btnAddPostFragmentH);
        btnSearchPost = view.findViewById(R.id.btnSearchPost);
        carList = new ArrayList<>();

        rclConfirmPostOwner = view.findViewById(R.id.RCLHomeConfirm);
        confirmLayoutPostOwner = view.findViewById(R.id.layoutHomeConfirm);

        rclConfirmPostOwner.setAdapter(adapterConfirmUserPostOwner);
        rclConfirmPostOwner.setLayoutManager(new LinearLayoutManager(getContext()));

        confirmLayoutRequestSender = view.findViewById(R.id.layoutHomeConfirmSender);

        rclConfirmRequestSender = view.findViewById(R.id.RCLHomeConfirmSender);
        rclConfirmRequestSender.setAdapter(adapterConfirmUserRequestSender);
        nestedScrollView = view.findViewById(R.id.nestedLayoutHome);

        rclConfirmRequestSender.setLayoutManager(new LinearLayoutManager(getContext()));

        nestedScrollView.setOnTouchListener((view15, motionEvent) -> {
            rclConfirmRequestSender.getParent().requestDisallowInterceptTouchEvent(false);
            rclConfirmPostOwner.getParent().requestDisallowInterceptTouchEvent(false);
            return false;
        });

        rclConfirmRequestSender.setOnTouchListener((view14, motionEvent) -> {
            rclConfirmRequestSender.getParent().requestDisallowInterceptTouchEvent(true);
            return false;
        });

        rclConfirmPostOwner.setOnTouchListener((view14, motionEvent) -> {
            rclConfirmPostOwner.getParent().requestDisallowInterceptTouchEvent(true);
            return false;
        });


        requestDAL.getConfirmUserForPostOwner(new RequestCallback() {
            @Override
            public void onRequestsRetrievedNotNull(List<Request> list) {
                super.onRequestsRetrievedNotNull(list);
                requestListPostOwnerConfirm.addAll(list);
                adapterConfirmUserPostOwner.notifyDataSetChanged();
                confirmLayoutPostOwner.setVisibility(View.VISIBLE);
            }

            @Override
            public void onRequestsRetrievedNull() {
                super.onRequestsRetrievedNull();

            }
        });

        requestDAL.getConfirmUserForRequestSender(new RequestCallback() {
            @Override
            public void onRequestsRetrievedNotNull(List<Request> list) {
                super.onRequestsRetrievedNotNull(list);
                requestListRequestSenderConfirm.addAll(list);
                adapterConfirmUserRequestSender.notifyDataSetChanged();
                confirmLayoutRequestSender.setVisibility(View.VISIBLE);
            }

            @Override
            public void onRequestsRetrievedNull() {
                super.onRequestsRetrievedNull();

            }
        });


        OneSignal.setExternalUserId(userid);

        btnAddPost.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(final View view) {

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
                        //Toast.makeText(mainActivity,"Araç yok",Toast.LENGTH_LONG).show();
                        popUpNoCar = new Dialog(mainActivity);
                        popUpNoCar.setContentView(R.layout.arac_yok_popup);
                        popUpNoCar.show();
                        Button btnGoToCarAddPage = popUpNoCar.findViewById(R.id.btnGoToAddCarPagePopUP);
                        btnGoToCarAddPage.setOnClickListener(view112 -> {
                            mainActivity.changeFragment(new FragmentMyCars());
                            popUpNoCar.dismiss();
                    });}
                }
            });
        }

    }

    public ArrayList<Request> getRequestListPostOwnerConfirm() {
        return requestListPostOwnerConfirm;
    }

    public void setRequestListPostOwnerConfirm(ArrayList<Request> requestListPostOwnerConfirm) {
        this.requestListPostOwnerConfirm = requestListPostOwnerConfirm;
    }

    public ArrayList<Request> getRequestListRequestSenderConfirm() {
        return requestListRequestSenderConfirm;
    }

    public void setRequestListRequestSenderConfirm(ArrayList<Request> requestListRequestSenderConfirm) {
        this.requestListRequestSenderConfirm = requestListRequestSenderConfirm;
    }
}
