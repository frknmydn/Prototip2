package com.furkanmeydan.prototip2.Views.PostSearchResultDetailActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.furkanmeydan.prototip2.DataLayer.LocalDataManager;
import com.furkanmeydan.prototip2.DataLayer.RequestCallback;
import com.furkanmeydan.prototip2.DataLayer.RequestDAL;
import com.furkanmeydan.prototip2.Models.Post;
import com.furkanmeydan.prototip2.R;
import com.furkanmeydan.prototip2.Views.MainActivity.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class FragmentPostSearchResultDetail extends Fragment {
    private TextView postHeader, postCity, postPassangerCount, postTime, postDescription, postCarDetail;
    private PostSearchResultDetailActivity activity;
    Button btnSendRequest;
    LocalDataManager localDataManager;
    Double requestLat1, requestLng1, requestLat2, requestLng2;
    String senderID, senderName, senderImgURL, senderGender,senderEmail,senderBirthdate;
    Post post;
    RequestDAL requestDAL;

    public FragmentPostSearchResultDetail() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (PostSearchResultDetailActivity) getActivity();
        localDataManager = new LocalDataManager();
        requestDAL = new RequestDAL();
        post = activity.post;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_search_result_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        postHeader = view.findViewById(R.id.txtSearchResultDetailHeader);
        postCity = view.findViewById(R.id.txtSearchResultDetailCity);
        postPassangerCount = view.findViewById(R.id.txtSearchResultDetailPasangerCount);
        postTime = view.findViewById(R.id.txtSearchResultDetailDateTime);
        postDescription = view.findViewById(R.id.txtSearchResultDetailDescription);
        postCarDetail = view.findViewById(R.id.txtSearchResultDetailCarDet);
        btnSendRequest = view.findViewById(R.id.btnSearchResultDetailSendRequest);

        // Scroll Element
        postCarDetail.setMovementMethod(new ScrollingMovementMethod());
        postDescription.setMovementMethod(new ScrollingMovementMethod());



        btnSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestLat1 = localDataManager.getSharedPreferenceForDouble(activity,"requestLat1",0d);
                requestLng1 = localDataManager.getSharedPreferenceForDouble(activity,"requestLng1",0d);
                requestLat2 = localDataManager.getSharedPreferenceForDouble(activity,"requestLat2",0d);
                requestLng2 = localDataManager.getSharedPreferenceForDouble(activity,"requestLng2",0d);
                Log.d("TAGGY", String.valueOf(requestLat1) + String.valueOf(requestLng1) + String.valueOf(requestLat2)+ String.valueOf(requestLng2));
                senderID = activity.firebaseAuth.getCurrentUser().getUid();
                senderName = localDataManager.getSharedPreference(activity,"sharedNameSurname",null);
                senderImgURL = localDataManager.getSharedPreference(activity,"sharedImageURL",null);
                senderGender = localDataManager.getSharedPreference(activity,"sharedGender",null);
                senderEmail = localDataManager.getSharedPreference(activity,"sharedEmail",null);
                senderBirthdate = localDataManager.getSharedPreference(activity,"sharedBirthdate",null);



                requestDAL.sendRequest(senderID, senderName, senderGender, senderImgURL, senderBirthdate, senderEmail, post.getPostID(),
                        post.getOwnerID(), requestLat1, requestLng1, requestLat2, requestLng2,post.getDescription(), new RequestCallback() {
                    @Override
                    public void onRequestSent() {
                        super.onRequestSent();

                        Intent i = new Intent(getContext(), MainActivity.class);
                        Toast.makeText(activity,"İstek gönderildi.",Toast.LENGTH_LONG).show();
                        startActivity(i);
                        activity.finish();

                    }
                });




            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }

    private void setData() {


        postHeader.setText(activity.post.getDestination());
        postCity.setText(activity.post.getCity());
        postPassangerCount.setText(String.valueOf(activity.post.getPassengerCount()));
        postDescription.setText(activity.post.getDescription());
        postCarDetail.setText(activity.post.getCarDetail());

        Log.d("RequestDalPostOwnerId",post.getOwnerID());
        Log.d("RequestDalPostID",post.getPostID());


        requestDAL.getRequest(post.getPostID(), post.getOwnerID(), new RequestCallback() {
            @Override
            public void onRequestRetrievedNotNull() {
                super.onRequestRetrievedNotNull();

                btnSendRequest.setClickable(false);
                btnSendRequest.setFocusable(false);
                btnSendRequest.setText("İstek gönderildi");
            }
        });

        //zaman işlemleri
        long timeStampp = activity.post.getTimestamp();
        Log.d("Post Tag timestamp", String.valueOf(timeStampp));
        SimpleDateFormat dateCombinedFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        Date date = new Date(TimeUnit.SECONDS.toMillis(timeStampp));

        String dateTime = dateCombinedFormat.format(date);
        //
        postTime.setText(dateTime);
    }
}