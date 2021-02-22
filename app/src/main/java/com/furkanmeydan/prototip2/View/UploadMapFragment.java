package com.furkanmeydan.prototip2.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.furkanmeydan.prototip2.DataLayer.LocalDataManager;
import com.furkanmeydan.prototip2.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.CollectionReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class UploadMapFragment extends Fragment {


    UploadPostActivity postActivity;
    Button btnMapClear;
    LocalDataManager localDataManager;
    int counter = 0;
    Double lat1,lat2,lng1,lng2;
    String cord1="";
    String cord2="";
    String cord3="";
    String cord4="";
    ArrayList<String> addressArray;

    LocationManager locationManager;
    private GoogleMap mMap;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;

            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {

                addMarker(latLng);

                }
            });

            if (Build.VERSION.SDK_INT >= 23) {
                if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                } else {
                    // locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
                    mMap.clear();


                    Location lastLocation = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
                    if (lastLocation != null) {
                        LatLng lastUserLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation, 16));
                    }


                }
            } else {

                mMap.clear();


                Location lastLocation = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
                if (lastLocation != null) {
                    LatLng lastUserLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation, 14));
                }


            }

            btnMapClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mMap.clear();
                    localDataManager.clearSharedPreference(getActivity());
                    counter = 0;
                }
            });

        }



    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_upload_map, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        postActivity = (UploadPostActivity) getActivity();

        localDataManager = new LocalDataManager();
        postActivity.findViewById(R.id.btnUploadPostMap).setClickable(false);
        postActivity.findViewById(R.id.btnUploadPostDetails).setClickable(true);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {

            btnMapClear = view.findViewById(R.id.btnMapClear);

            addressArray=new ArrayList<>();

            mapFragment.getMapAsync(callback);


        }
    }

    public void addMarker(LatLng latLng){

        String address= "";

        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

        if(counter<2) {

            try {

                List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

                if (addressList != null && addressList.size() > 0) {
                    if (addressList.get(0).getThoroughfare() != null) {
                        address += addressList.get(0).getThoroughfare();
                        if (addressList.get(0).getSubThoroughfare() != null) {
                            address += addressList.get(0).getSubThoroughfare();

                            addressArray.add(address);


                        }

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            if(counter == 0){
                lat1 = latLng.latitude;
                lng1 = latLng.longitude;

                cord1 = lat1.toString();
                cord2 = lng1.toString();






            }
            else if(counter ==1){
                lat2 = latLng.latitude;
                lng2 = latLng.longitude;

                cord3 = lat2.toString();
                cord4 = lng2.toString();


                localDataManager.setSharedPreference(postActivity,"lat_1",cord1);
                localDataManager.setSharedPreference(postActivity,"lng_1",cord2);
                localDataManager.setSharedPreference(postActivity,"lat_2",cord3);
                localDataManager.setSharedPreference(postActivity,"lng_2",cord4);

                Log.d("Tag",localDataManager.getSharedPreference(postActivity,"lat_1","YOK"));
                Log.d("Tag",localDataManager.getSharedPreference(postActivity,"lng_1","YOK"));
                Log.d("Tag",localDataManager.getSharedPreference(postActivity,"lat_2","YOK"));
                Log.d("Tag",localDataManager.getSharedPreference(postActivity,"lng_2","YOK"));

                addressArray.clear();


            }

            mMap.addMarker(new MarkerOptions().title(address).position(latLng));

            counter++;
        }


    }

}