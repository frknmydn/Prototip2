package com.furkanmeydan.prototip2.Views.UploadPostActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UploadMapFragment extends Fragment {


    UploadPostActivity postActivity;
    Button btnMapClear;
    LocalDataManager localDataManager;
    int counter = 0;
    Double lat1, lat2, lng1, lng2;
    String cord1 = "";
    String cord2 = "";
    String cord3 = "";
    String cord4 = "";
    ArrayList<String> addressArray;
    List<Address> addressList;

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

            btnMapClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mMap.clear();
                    localDataManager.removeSharedPreference(postActivity, "lat_1");
                    localDataManager.removeSharedPreference(postActivity, "lng_1");
                    localDataManager.removeSharedPreference(postActivity, "lat_2");
                    localDataManager.removeSharedPreference(postActivity, "lng_2");
                    counter = 0;
                    addressArray.clear();

                }
            });
            postActivity.findViewById(R.id.btnUploadPost).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    postActivity.AUPUploadPost(view);
                }
            });

            double sharedLat1 = localDataManager.getSharedPreferenceForDouble(postActivity, "lat_1", 0d);
            double sharedLng1 = localDataManager.getSharedPreferenceForDouble(postActivity, "lng_1", 0d);
            double sharedLat2 = localDataManager.getSharedPreferenceForDouble(postActivity, "lat_2", 0d);
            double sharedLng2 = localDataManager.getSharedPreferenceForDouble(postActivity, "lng_2", 0d);
            Log.d("TAG SHARED ", String.valueOf(sharedLat1));

            if (sharedLat1 != 0 && sharedLat2 != 0 && sharedLng1 != 0 && sharedLng2 != 0) {

                LatLng latlng1 = new LatLng(sharedLat1, sharedLng1);
                mMap.addMarker(new MarkerOptions().title("Kalkış").position(latlng1));
                LatLng latln2 = new LatLng(sharedLat2, sharedLng2);
                mMap.addMarker(new MarkerOptions().title("Varış").position(latln2));
            }

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


            addressArray = new ArrayList<>();

            mapFragment.getMapAsync(callback);


        }
    }

    public void addMarker(LatLng latLng) {

        String address = "";

        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

        if (counter < 2) {

            try {

                addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);


                if (addressList != null && addressList.size() > 0) {
                    if (addressList.get(0).getThoroughfare() != null && !addressList.get(0).getThoroughfare().equals("Unnamed Road")) {
                        address += addressList.get(0).getThoroughfare();
                        address += " " + addressList.get(0).getAdminArea();
                        //address += addressList.get(0).getSubAdminArea();
                        //address += addressList.get(0).getPostalCode();
                        addressArray.add(address);

                    } else {
                        address += addressList.get(0).getAdminArea();
                        addressArray.add(address);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (counter == 0) {
                lat1 = latLng.latitude;
                lng1 = latLng.longitude;

                cord1 = lat1.toString();
                cord2 = lng1.toString();


                localDataManager.setSharedPreferenceForDouble(postActivity, "lat_1", lat1);
                localDataManager.setSharedPreferenceForDouble(postActivity, "lng_1", lng1);
                localDataManager.setSharedPreference(postActivity, "marker1city", addressList.get(0).getAdminArea());


                mMap.addMarker(new MarkerOptions().title("Kalkış: " + addressArray.get(0)).position(latLng));
                addressArray.clear();


            } else if (counter == 1) {
                lat2 = latLng.latitude;
                lng2 = latLng.longitude;

                cord3 = lat2.toString();
                cord4 = lng2.toString();


                localDataManager.setSharedPreferenceForDouble(postActivity, "lat_2", lat2);
                localDataManager.setSharedPreferenceForDouble(postActivity, "lng_2", lng2);
                localDataManager.setSharedPreference(postActivity, "marker2city", addressList.get(0).getAdminArea());

                Log.d("Tag", String.valueOf(localDataManager.getSharedPreferenceForDouble(postActivity, "lat_1",  0d)));
                Log.d("Tag", String.valueOf(localDataManager.getSharedPreferenceForDouble(postActivity, "lng_1",  0d)));
                Log.d("Tag", String.valueOf(localDataManager.getSharedPreferenceForDouble(postActivity, "lat_2", 0d)));
                Log.d("Tag", String.valueOf(localDataManager.getSharedPreferenceForDouble(postActivity, "lng_2", 0d)));

                System.out.println(localDataManager.getSharedPreferenceForDouble(postActivity,"lat_1",0d));

                mMap.addMarker(new MarkerOptions().title("Varış: " + addressArray.get(0)).position(latLng));
                addressArray.clear();


            }


            counter++;


        }



    }
    

}