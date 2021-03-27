package com.furkanmeydan.prototip2.Views.MainActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.furkanmeydan.prototip2.DataLayer.RequestCallback;
import com.furkanmeydan.prototip2.DataLayer.RequestDAL;
import com.furkanmeydan.prototip2.Models.Post;
import com.furkanmeydan.prototip2.Models.Request;
import com.furkanmeydan.prototip2.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class FragmentRequestSenderProfile extends Fragment {
    private TextView nameSurname, gender, birthdate;
    private ImageView imageView;
    private Button btnShowComments, btnAccept, btnDecline;
    RequestDAL requestDAL;
    Request request;

    MapView mapView;
    private GoogleMap googleMap;
    MainActivity activity;
    LatLng latlng1, latLng2;


    public FragmentRequestSenderProfile() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        requestDAL = new RequestDAL();

        if (getArguments() != null) {
            request = (Request) getArguments().getSerializable("request");

        }

        latlng1 = new LatLng(request.getLat1(), request.getLng1());
        latLng2 = new LatLng(request.getLat2(), request.getLng2());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_request_sender_profile, container, false);
        mapView = (MapView) rootView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.onResume();

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                //googleMap.setMyLocationEnabled(true);

                googleMap.addMarker(new MarkerOptions().position(latlng1).title("Binmek istediği nokta"));
                googleMap.addMarker(new MarkerOptions().position(latLng2).title("İnmek istediği nokta"));
                CameraPosition cameraPosition = new CameraPosition.Builder().target(latlng1).zoom(14).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameSurname = view.findViewById(R.id.fragmentRequestSenderProfileNameSurname);
        gender = view.findViewById(R.id.fragmentRequestSenderProfileGender);
        birthdate = view.findViewById(R.id.fragmentRequestSenderProfileBirthday);

        imageView = view.findViewById(R.id.fragmentRequestSenderProfileImg);
        btnAccept = view.findViewById(R.id.fragmentRequestSenderProfileAccept);
        btnDecline = view.findViewById(R.id.fragmentRequestSenderProfileDecline);
        btnShowComments = view.findViewById(R.id.fragmentRequestSenderProfileShowComments);

        nameSurname.setText(request.getSenderName());
        gender.setText(request.getSenderGender());
        birthdate.setText(request.getSenderBirthdate());

        Glide.with(activity.getApplicationContext()).load(request.getSenderImage()).apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).into(imageView);


        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String requestID = request.getRequestID();
                String postID = request.getPostID();
                String postownerID = request.getPostOwnerID();
                requestDAL.acceptRequest(postID, postownerID, requestID, new RequestCallback() {
                    @Override
                    public void onRequestAccepted() {
                        super.onRequestAccepted();
                        activity.changeFragment(new FragmentRequestsToMyPosts());
                    }
                });
            }
        });

        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String requestID = request.getRequestID();
                String postID = request.getPostID();
                String postownerID = request.getPostOwnerID();

                requestDAL.rejectRequest(postID, postownerID, requestID, new RequestCallback() {
                    @Override
                    public void onRequestRejected() {
                        super.onRequestRejected();
                        activity.changeFragment(new FragmentRequestsToMyPosts());
                    }
                });
            }
        });

        btnShowComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}


