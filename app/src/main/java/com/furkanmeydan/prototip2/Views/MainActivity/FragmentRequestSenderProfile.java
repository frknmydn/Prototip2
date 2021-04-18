package com.furkanmeydan.prototip2.Views.MainActivity;

import android.content.Intent;
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
import android.widget.Toast;

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
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class FragmentRequestSenderProfile extends Fragment {
    private TextView nameSurname, gender, birthdate, requestText;
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
        requestText = view.findViewById(R.id.fragmentRequestSenderProfileRequestText);
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
        requestText.setText(request.getRequestText());

        Glide.with(activity.getApplicationContext()).load(request.getSenderImage()).apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).into(imageView);


        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String requestID = request.getRequestID();
                String postID = request.getPostID();
                String postownerID = request.getPostOwnerID();
                requestDAL.acceptRequest(postID, postownerID, requestID, new RequestCallback() {
                    @Override
                    public void onRequestAccepted() {
                        super.onRequestAccepted();
                        try {
                            long timestamp = request.getPostTimestamp()-900L;

                            SimpleDateFormat dateCombinedFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date = new Date(TimeUnit.SECONDS.toMillis(timestamp));
                            String dateTime = dateCombinedFormat.format(date) + " GMT+0300";
                            Log.d("Tag","onesignal date: "+dateTime);
                            OneSignal.postNotification(new JSONObject("{'contents': {'en':'Gonderdiginiz isteklerinizden biri onaylandi'}, 'include_player_ids': ['" + request.getOneSignalID() + "']}"), null);

                            OneSignal.postNotification(new JSONObject("{'contents': {'en':'Hatirlatma: yolculuk saatinize 15 dakika kalmistir'}, 'include_player_ids': ['" + request.getOneSignalID() + "']}").put("send_after",dateTime), null);
                            Intent i = new Intent(getContext(), MainActivity.class);
                            Toast.makeText(activity,"İstek gönderildi.",Toast.LENGTH_LONG).show();
                            startActivity(i);
                            activity.finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("Tag","onesignal date: YOH CATCHLEDİ");
                        }

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


