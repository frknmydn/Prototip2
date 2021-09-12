package com.furkanmeydan.prototip2.Views.UploadPostActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.furkanmeydan.prototip2.DataLayer.LocalDataManager;
import com.furkanmeydan.prototip2.R;
import com.furkanmeydan.prototip2.Views.PostActivity.PostActivity;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


public class UploadMapFragment2 extends Fragment implements OnMapReadyCallback {
    private MapView mapView;
    ImageButton btnMapClear;
    LocalDataManager localDataManager;
    int counter = 0;
    Double lat1, lat2, lng1, lng2;
    String cord1 = "";
    String cord2 = "";
    String cord3 = "";
    String cord4 = "";
    SearchView searchView;
    ArrayList<String> addressArray;
    List<Address> addressList;
    AutocompleteSupportFragment autocompleteFragment;

    LocationManager locationManager;
    private GoogleMap googleMappo;
    UploadPostActivity postActivity;
    PlacesClient placesClient;
    OnMapReadyCallback callback;
    TabLayout tabLayout;
    Button btnAddMarker,btnZoomToMe;
    TextView txtMarkerStatus;
    ImageView imgMarker;


    public UploadMapFragment2() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        postActivity = (UploadPostActivity) getActivity();
        addressArray = new ArrayList<>();

        Places.initialize(postActivity,"AIzaSyAkR63wvDhI3bukQYRSBxXtarR_e2G_t1I");

        placesClient = Places.createClient(postActivity);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_upload_map2, container, false);
        mapView = rootView.findViewById(R.id.mapViewUploadPost);
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
            if (requestCode == 1 && ContextCompat.checkSelfPermission(postActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Location lastLocation = locationManager.getLastKnownLocation(locationManager.PASSIVE_PROVIDER);
                if (lastLocation != null) {
                    LatLng lastUserLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                    googleMappo.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation, 16));
                }
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        localDataManager = new LocalDataManager();
        //postActivity.findViewById(R.id.btnUploadPostMap).setClickable(false);
        //postActivity.findViewById(R.id.btnUploadPostDetails).setClickable(true);
        btnMapClear = view.findViewById(R.id.imageViewClearMapUploadPost);
        btnAddMarker = view.findViewById(R.id.btnAddMarker);
        txtMarkerStatus = view.findViewById(R.id.txtMarkerStatus);
        txtMarkerStatus.setText("Tahmini Kalkış Noktası");
        imgMarker = view.findViewById(R.id.imgMarker);
        btnZoomToMe = view.findViewById(R.id.btnZoomToMe);




        /*
        tabLayout = postActivity.findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        postActivity.changeFragment(new UploadPostDetailFragment());
                        break;
                    case 1:
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


         */

    }


    /*
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        if(requestCode == 95957){
            if(resultCode == Activity.RESULT_OK){
                Place place = Autocomplete.getPlaceFromIntent(data);
                LatLng latLng = place.getLatLng();
                Log.d("Tag","Latlng"+ String.valueOf(latLng));
                googleMappo.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

     */

    public void addMarker(LatLng latLng) {
        Log.d("TAG", "addMarker: " + "içine girildi");

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
                postActivity.postToPush.setFromLat(lat1);
                postActivity.postToPush.setFromLng(lng1);
                localDataManager.setSharedPreference(postActivity, "marker1city", addressList.get(0).getAdminArea());


                googleMappo.addMarker(new MarkerOptions().title("Kalkış: " + addressArray.get(0)).position(latLng));
                txtMarkerStatus.setText("Tahmini Varış Noktası");
                addressArray.clear();


            } else if (counter == 1) {
                lat2 = latLng.latitude;
                lng2 = latLng.longitude;

                cord3 = lat2.toString();
                cord4 = lng2.toString();


                localDataManager.setSharedPreferenceForDouble(postActivity, "lat_2", lat2);
                localDataManager.setSharedPreferenceForDouble(postActivity, "lng_2", lng2);
                postActivity.postToPush.setToLat(lat2);
                postActivity.postToPush.setToLng(lng2);
                localDataManager.setSharedPreference(postActivity, "marker2city", addressList.get(0).getAdminArea());

                Log.d("Tag", String.valueOf(localDataManager.getSharedPreferenceForDouble(postActivity, "lat_1",  0d)));
                Log.d("Tag", String.valueOf(localDataManager.getSharedPreferenceForDouble(postActivity, "lng_1",  0d)));
                Log.d("Tag", String.valueOf(localDataManager.getSharedPreferenceForDouble(postActivity, "lat_2", 0d)));
                Log.d("Tag", String.valueOf(localDataManager.getSharedPreferenceForDouble(postActivity, "lng_2", 0d)));

                System.out.println(localDataManager.getSharedPreferenceForDouble(postActivity,"lat_1",0d));

                googleMappo.addMarker(new MarkerOptions().title("Varış: " + addressArray.get(0)).position(latLng));
                addressArray.clear();
                txtMarkerStatus.setVisibility(View.GONE);
                imgMarker.setVisibility(View.GONE);



            }


            counter++;


        }



    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMappo= googleMap;

        btnAddMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMarker(googleMappo.getCameraPosition().target);
            }
        });


        FragmentManager fm = getChildFragmentManager();

        autocompleteFragment = (AutocompleteSupportFragment)
                fm.findFragmentById(R.id.uploadPostPlacesFragment);



         if(autocompleteFragment!=null) {

                 autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.LAT_LNG, Place.Field.ADDRESS, Place.Field.ID, Place.Field.NAME));

                 autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                     @Override
                     public void onPlaceSelected(@NonNull @NotNull Place place) {
                         String test = place.getAddress();
                         LatLng latLng = place.getLatLng();
                         Log.d("Tag", "Latlng " + String.valueOf(latLng));
                         Log.d("Tag", "Address " + String.valueOf(test));
                         googleMappo.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                     }

                     @Override
                     public void onError(@NonNull @NotNull Status status) {

                     }
                 });


        }



        locationManager = (LocationManager) postActivity.getSystemService(Context.LOCATION_SERVICE);


        /*
        if (Build.VERSION.SDK_INT >= 23) {
            if (postActivity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            } else {
                // locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
                googleMappo.clear();


                Location lastLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                if (lastLocation != null) {
                    LatLng lastUserLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                    googleMappo.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation, 16));
                }


            }
        } else {

            googleMappo.clear();


            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            if (lastLocation != null) {
                LatLng lastUserLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                googleMappo.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation, 14));
            }


        }


         */

        zoomOnMe();

        btnZoomToMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomOnMe();
            }
        });

        btnMapClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleMappo.clear();
                localDataManager.removeSharedPreference(postActivity, "lat_1");
                localDataManager.removeSharedPreference(postActivity, "lng_1");
                localDataManager.removeSharedPreference(postActivity, "lat_2");
                localDataManager.removeSharedPreference(postActivity, "lng_2");
                counter = 0;
                addressArray.clear();
                imgMarker.setVisibility(View.VISIBLE);
                txtMarkerStatus.setText("Tahmini Kalkış Noktası");
                txtMarkerStatus.setVisibility(View.VISIBLE);

            }
        });

                    /*
                    postActivity.findViewById(R.id.btnUploadPost).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            postActivity.AUPUploadPost(view);
                        }
                    });

                     */

        double sharedLat1 = localDataManager.getSharedPreferenceForDouble(postActivity, "lat_1", 0d);
        double sharedLng1 = localDataManager.getSharedPreferenceForDouble(postActivity, "lng_1", 0d);
        double sharedLat2 = localDataManager.getSharedPreferenceForDouble(postActivity, "lat_2", 0d);
        double sharedLng2 = localDataManager.getSharedPreferenceForDouble(postActivity, "lng_2", 0d);

        Log.d("TAG SHARED ", String.valueOf(sharedLat1));

        if (sharedLat1 != 0 && sharedLat2 != 0 && sharedLng1 != 0 && sharedLng2 != 0) {

            LatLng latlng1 = new LatLng(sharedLat1, sharedLng1);
            googleMappo.addMarker(new MarkerOptions().title("Kalkış").position(latlng1));
            LatLng latln2 = new LatLng(sharedLat2, sharedLng2);
            googleMappo.addMarker(new MarkerOptions().title("Varış").position(latln2));
            counter = 2;
            txtMarkerStatus.setVisibility(View.GONE);
            imgMarker.setVisibility(View.GONE);
        }

    }

    public void zoomOnMe(){
        if (Build.VERSION.SDK_INT >= 23) {
            if (postActivity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            } else {
                // locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);


                Location lastLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                if (lastLocation != null) {
                    LatLng lastUserLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                    googleMappo.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation, 16));
                }


            }
        } else {



            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            if (lastLocation != null) {
                LatLng lastUserLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                googleMappo.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation, 14));
            }


        }
    }

}