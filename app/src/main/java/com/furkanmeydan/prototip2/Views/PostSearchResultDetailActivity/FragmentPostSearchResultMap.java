package com.furkanmeydan.prototip2.Views.PostSearchResultDetailActivity;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.furkanmeydan.prototip2.MapRouter.FetchURL;
import com.furkanmeydan.prototip2.Models.Request;
import com.furkanmeydan.prototip2.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;

public class FragmentPostSearchResultMap extends Fragment {
    PostSearchResultDetailActivity activity;

    ArrayList<Request> requestList;


    private LatLng marker1, marker2;
    private GoogleMap mMap;
    LocationManager locationManager;

    private Polyline currentPolyline;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (PostSearchResultDetailActivity) getActivity();
        if(activity!=null){
            requestList = activity.requestList;
            Double fromLat = activity.post.getFromLat();
            Double fromLng = activity.post.getFromLng();
            Double toLat = activity.post.getToLat();
            Double toLng = activity.post.getToLng();
            marker1 = new LatLng(fromLat, fromLng);
            marker2 = new LatLng(toLat, toLng);
        }

    }

    private final OnMapReadyCallback callback = new OnMapReadyCallback() {


        @Override
        public void onMapReady(GoogleMap googleMap) {
            MarkerOptions place1 = new MarkerOptions().position(marker1).title("Kalkış");
            MarkerOptions place2 = new MarkerOptions().position(marker2).title("Varış");
            mMap = googleMap;





            locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

            String url = getUrl(place1.getPosition(), place2.getPosition(),"driving");
            new FetchURL(activity).execute(url,"driving");

            for(int i=0; i<requestList.size(); i++){
                LatLng markers = new LatLng(requestList.get(i).getLat1(), requestList.get(i).getLng1());
                googleMap.addMarker(new MarkerOptions().position(markers).title(requestList.get(i).getSenderName()+ "'nin binmek istediği yer."));
            }

            mMap.addMarker(place1);
            mMap.addMarker(place2);

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker1, 16));



            /*
            if (Build.VERSION.SDK_INT >= 23) {
                if(activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                } else {
                    // locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
                    // mMap.clear();


                    Location lastLocation = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
                    if (lastLocation != null) {
                        LatLng lastUserLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation, 16));
                    }
                    else{
                        Log.d("Tag", "Last location null geliyor");
                    }


                }
            } else {
                // locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
                //mMap.clear();


                //mMap.addMarker(place1);
                //mMap.addMarker(place2);



                Location lastLocation = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
                if (lastLocation != null) {
                    LatLng lastUserLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation, 14));
                }
                else{
                    Log.d("Tag", "Last location null sdk <23 geliyor");
                }


            }
            */

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post_search_result_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        //Haritada directions gelmeden fragment değiştirilince kod patlıyor, onu engellemek için
        for(int i = 0; i < activity.navigationView1.getMenu().size();i++){
            activity.navigationView1.getMenu().getItem(i).setEnabled(false);
        }
        if(mapFragment!=null){
            mapFragment.getMapAsync(callback);
        }


    }


    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + "AIzaSyAkR63wvDhI3bukQYRSBxXtarR_e2G_t1I";
    }


    public GoogleMap getmMap() {
        return mMap;
    }

    public Polyline getCurrentPolyline() {
        return currentPolyline;
    }
}




