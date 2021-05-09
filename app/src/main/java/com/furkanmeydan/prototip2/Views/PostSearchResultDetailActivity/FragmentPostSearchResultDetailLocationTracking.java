package com.furkanmeydan.prototip2.Views.PostSearchResultDetailActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.furkanmeydan.prototip2.Models.Post;
import com.furkanmeydan.prototip2.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;


public class FragmentPostSearchResultDetailLocationTracking extends Fragment {

    MapView mapView;
    private GoogleMap googleMapLocations;
    private PostSearchResultDetailActivity activity;
    private LatLng latLngPosition;
    Post post;
    Double lat,lng;
    String latString,lngString;
    RequestQueue queue;

    public FragmentPostSearchResultDetailLocationTracking() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (PostSearchResultDetailActivity) getActivity();
        queue = Volley.newRequestQueue(activity);
        post = activity.post;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_post_search_result_detail_location_tracking,container,false);
        mapView = (MapView) rootView.findViewById(R.id.mapViewLocationTrackin);
        mapView.onCreate(savedInstanceState);

        mapView.onResume();

        Timer timerRequest = new Timer();
        timerRequest.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d("Tag","TimerRequest içi");
                tryRequest();

                Log.d("Tag","tryRequestSonrası");

            }
        },0,30*1000);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMapLocations = googleMap;
            }
        });
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    public void tryRequest(){
        Log.d("Tag","TryRequest içi");

        String url = "https://carsharingapp.me/api/Positions/GetPositionByPostID/"+post.getPostID();
        Log.d("Tag","String url : " + url);
        JSONObject jsonObject = new JSONObject();

        JsonObjectRequest volleyRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("Tag","onResponse içi");
                    lat = Double.valueOf(response.getString("latitude"));
                    lng = Double.valueOf(response.getString("longitude"));
                    latString = response.getString("latitude");
                    lngString = response.getString("longitude");
                    latLngPosition = new LatLng(lat,lng);
                    googleMapLocations.addMarker(new MarkerOptions().position(latLngPosition).title("Binmek istediği nokta"));
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(latLngPosition).zoom(14).build();
                    googleMapLocations.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    //Log.d("Tag","responseGetString" +response.getString("latitude"));
                    //Log.d("Tag","responseGetString"+response.getString("longitude"));
                    Log.d("Tag","Lat "+lat);
                    Log.d("Tag","Lng "+lng);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("Tag", "onResponse çalışıyor");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Tag", "onErrorResponse çalışıyor: "+ error.getMessage());
            }
        });

        queue.add(volleyRequest);

    }
}