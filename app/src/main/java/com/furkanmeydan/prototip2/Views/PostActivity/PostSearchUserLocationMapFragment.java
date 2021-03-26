package com.furkanmeydan.prototip2.Views.PostActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.furkanmeydan.prototip2.DataLayer.LocalDataManager;
import com.furkanmeydan.prototip2.DataLayer.PostDAL;
import com.furkanmeydan.prototip2.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class PostSearchUserLocationMapFragment extends Fragment {

    PostDAL postDAL;
    Double lat1, lat2, lng1, lng2;
    String cord1 = "";
    String cord2 = "";
    String cord3 = "";
    String cord4 = "";
    int counter = 0;
    Button btnClear,btnChangeFragment;
    private GoogleMap mMap;
    LocationManager locationManager;
    PostActivity postActivity;
    Bundle bundle;
    LocalDataManager localDataManager;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

            if (Build.VERSION.SDK_INT >= 23) {
                if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                } else {
                    // locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
                    mMap.clear();


                    Location lastLocation = locationManager.getLastKnownLocation(locationManager.PASSIVE_PROVIDER);
                    if (lastLocation != null) {
                        LatLng lastUserLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation, 16));
                    }


                }
            } else {

                mMap.clear();

                Location lastLocation = locationManager.getLastKnownLocation(locationManager.PASSIVE_PROVIDER);
                if (lastLocation != null) {
                    LatLng lastUserLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation, 14));
                }


            }

            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    addMarker(latLng);
                }
            });

            btnChangeFragment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int direction = postDAL.findDirection(lat1,lng1,lat2,lng2);
                    bundle.putDouble("userlat1", lat1);
                    bundle.putDouble("userlat2", lat2);
                    bundle.putDouble("userlng1", lng1);
                    bundle.putDouble("userlng2", lng2);
                    bundle.putInt("direction",direction);

                    //Kullanıcı istek gönderirken kullanmak için
                    localDataManager.setSharedPreferenceForDouble(postActivity,"requestLat1",lat1);
                    localDataManager.setSharedPreferenceForDouble(postActivity,"requestLng1",lng1);
                    localDataManager.setSharedPreferenceForDouble(postActivity,"requestLat2",lat2);
                    localDataManager.setSharedPreferenceForDouble(postActivity,"requestLng2",lng2);



                    postActivity.changeFragmentArgs(new PostSearchResultFragment(),bundle);
                }
            });

            btnClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mMap.clear();
                    counter = 0;

                }
            });


        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postActivity = (PostActivity) getActivity();
        localDataManager = new LocalDataManager();

        if(getArguments() !=null){
            bundle = getArguments();

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post_search_user_location_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);

            postDAL = new PostDAL();
            btnChangeFragment = view.findViewById(R.id.btnUserLocationMapChangeFragment);
            btnClear = view.findViewById(R.id.btnUserLocationMapClear);
        }

    }

    public void addMarker(LatLng latLng){
        if(counter<2){
            if (counter == 0) {
                lat1 = latLng.latitude;
                lng1 = latLng.longitude;

                cord1 = lat1.toString();
                cord2 = lng1.toString();



                mMap.addMarker(new MarkerOptions().title("Biniş").position(latLng));



            } else if (counter == 1) {
                lat2 = latLng.latitude;
                lng2 = latLng.longitude;

                cord3 = lat2.toString();
                cord4 = lng2.toString();


                mMap.addMarker(new MarkerOptions().title("İniş").position(latLng));


            }
            counter++;
        }


    }



}