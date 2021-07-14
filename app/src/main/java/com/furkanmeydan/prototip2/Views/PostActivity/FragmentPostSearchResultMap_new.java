package com.furkanmeydan.prototip2.Views.PostActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.furkanmeydan.prototip2.DataLayer.LocalDataManager;
import com.furkanmeydan.prototip2.DataLayer.PostDAL;
import com.furkanmeydan.prototip2.Models.Post;
import com.furkanmeydan.prototip2.Models.Request;
import com.furkanmeydan.prototip2.R;
import com.furkanmeydan.prototip2.Views.PostSearchResultDetailActivity.PostSearchResultDetailActivity;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;


public class FragmentPostSearchResultMap_new extends Fragment implements OnMapReadyCallback {

    PostDAL postDAL;
    Double lat1, lat2, lng1, lng2;
    String cord1 = "";
    String cord2 = "";
    String cord3 = "";
    String cord4 = "";
    int counter = 0;
    Button btnChangeFragment;
    private GoogleMap mMap;
    LocationManager locationManager;
    PostActivity activity;
    //Bundle bundle;
    LocalDataManager localDataManager;

    private MapView mapView;
    ImageButton btnClear;
    PlacesClient placesClient;



    public FragmentPostSearchResultMap_new() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (PostActivity) getActivity();
        localDataManager = new LocalDataManager();
        /*
        if(getArguments() != null){
            bundle = getArguments();
        }

         */
        if (activity != null) {
            Places.initialize(activity,"AIzaSyAkR63wvDhI3bukQYRSBxXtarR_e2G_t1I");

            placesClient = Places.createClient(activity);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_post_search_result_map_new, container, false);
        mapView = rootView.findViewById(R.id.MapViewResultNew);
        mapView.onCreate(savedInstanceState);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        mapView.onResume();

        mapView.getMapAsync(this);


        return rootView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0){
            if (requestCode == 1 && ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Location lastLocation = locationManager.getLastKnownLocation(locationManager.PASSIVE_PROVIDER);
                if (lastLocation != null) {
                    LatLng lastUserLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation, 16));
                }
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

            btnClear = view.findViewById(R.id.IBResultMapClear);
            postDAL = new PostDAL();
            btnChangeFragment = view.findViewById(R.id.btnChangeFragmentMap);




    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap= googleMap;

        lat1 = localDataManager.getSharedPreferenceForDouble(activity,"requestLat1",0d);
        lng1 = localDataManager.getSharedPreferenceForDouble(activity, "requestLng1",0d);
        lat2 = localDataManager.getSharedPreferenceForDouble(activity, "requestLat2",0d);
        lng2 = localDataManager.getSharedPreferenceForDouble(activity, "requestLng2",0d);

        if(lat1 != 0d && lng1 != 0d && lat2 != 0d && lng2 != 0d){
            Log.d("Tag", "counter: " + counter);
            Log.d("Tag", "lat1: " + String.valueOf(lat1));
            Log.d("Tag", "lng1: " + String.valueOf(lng1));
            Log.d("Tag", "lng2: " + String.valueOf(lng2));
            Log.d("Tag", "lat2: " + String.valueOf(lat1));
            LatLng latLng1 = new LatLng(lat1,lng1);
            addMarker(latLng1);
            LatLng latLng2 = new LatLng(lat2,lng2);
            addMarker(latLng2);
        }

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.searchPostPlacesFragment);
        if (autocompleteFragment != null) {
            autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.LAT_LNG, Place.Field.ADDRESS, Place.Field.ID, Place.Field.NAME));

            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(@NonNull @NotNull Place place) {
                    String test = place.getAddress();
                    LatLng latLng = place.getLatLng();
                    Log.d("Tag","Latlng "+ String.valueOf(latLng));
                    Log.d("Tag","Address "+ String.valueOf(test));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                }

                @Override
                public void onError(@NonNull @NotNull Status status) {

                }
            });
        }


        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                addMarker(latLng);

            }
        });

        if (Build.VERSION.SDK_INT >= 23) {
            if (activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            } else {
                // locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
                //mMap.clear();


                Location lastLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                if (lastLocation != null) {
                    LatLng lastUserLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation, 16));
                }


            }
        } else {

            //mMap.clear();


            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            if (lastLocation != null) {
                LatLng lastUserLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation, 14));
            }


        }

        /*
        btnChangeFragment.setOnClickListener(view -> {
            if (lat1 != null && lng1 != null && lat2 != null && lng2 != null) {

                int direction = postDAL.findDirection(lat1, lng1, lat2, lng2);
                bundle.putDouble("userlat1", lat1);
                bundle.putDouble("userlat2", lat2);
                bundle.putDouble("userlng1", lng1);
                bundle.putDouble("userlng2", lng2);
                bundle.putInt("direction", direction);

                //Kullanıcı istek gönderirken kullanmak için



                activity.changeFragmentArgs(new PostSearchResultFragment(), bundle);
            }
        });

         */

        btnClear.setOnClickListener(view -> {
            mMap.clear();
            counter = 0;
            localDataManager.removeSharedPreference(activity,"requestLat1");
            localDataManager.removeSharedPreference(activity,"requestLat2");
            localDataManager.removeSharedPreference(activity,"requestLng1");
            localDataManager.removeSharedPreference(activity,"requestLng2");

        });


    }


    public void addMarker(LatLng latLng) {
        Log.d("Tag", "addMarker: "+counter);
        if (counter < 2) {
            if (counter == 0) {
                lat1 = latLng.latitude;
                lng1 = latLng.longitude;

                cord1 = lat1.toString();
                cord2 = lng1.toString();


                localDataManager.setSharedPreferenceForDouble(activity, "requestLat1", lat1);
                localDataManager.setSharedPreferenceForDouble(activity, "requestLng1", lng1);
                activity.bundle.putDouble("userlat1",lat1);
                activity.bundle.putDouble("userlng1",lng1);
                mMap.addMarker(new MarkerOptions().title("Biniş").position(latLng));


            } else if (counter == 1) {
                lat2 = latLng.latitude;
                lng2 = latLng.longitude;

                cord3 = lat2.toString();
                cord4 = lng2.toString();


                localDataManager.setSharedPreferenceForDouble(activity, "requestLat2", lat2);
                localDataManager.setSharedPreferenceForDouble(activity, "requestLng2", lng2);
                activity.bundle.putDouble("userlat2",lat2);
                activity.bundle.putDouble("userlng2",lng2);
                mMap.addMarker(new MarkerOptions().title("İniş").position(latLng));


            }
            counter++;
        }




    }
}