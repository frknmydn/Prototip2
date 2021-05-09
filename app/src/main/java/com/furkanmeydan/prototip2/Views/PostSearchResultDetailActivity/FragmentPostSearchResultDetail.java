package com.furkanmeydan.prototip2.Views.PostSearchResultDetailActivity;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.furkanmeydan.prototip2.DataLayer.LocalDataManager;
import com.furkanmeydan.prototip2.DataLayer.Callbacks.PostCallback;
import com.furkanmeydan.prototip2.DataLayer.PostDAL;
import com.furkanmeydan.prototip2.DataLayer.Callbacks.RequestCallback;
import com.furkanmeydan.prototip2.DataLayer.RequestDAL;
import com.furkanmeydan.prototip2.Models.Post;
import com.furkanmeydan.prototip2.Models.Request;
import com.furkanmeydan.prototip2.R;
import com.furkanmeydan.prototip2.Views.MainActivity.MainActivity;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class FragmentPostSearchResultDetail extends Fragment {
    private TextView postHeader, postCity, postPassangerCount, postTime, postDescription, postCarDetail;
    private PostSearchResultDetailActivity activity;
    Button btnSendRequest,btnAddToWish, btnStartService, btnEndService, btnLocationTracking;
    LocalDataManager localDataManager;
    Double requestLat1, requestLng1, requestLat2, requestLng2;
    String senderID, senderName, senderImgURL, senderGender,senderEmail,senderBirthdate,senderOneSignalID, authID;
    Post post;
    PostDAL postDAL;
    RequestDAL requestDAL;
    Dialog dialog;
    FirebaseAuth firebaseAuth;

    public FragmentPostSearchResultDetail() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (PostSearchResultDetailActivity) getActivity();
        localDataManager = new LocalDataManager();
        requestDAL = new RequestDAL();
        postDAL = new PostDAL();
        post = activity.post;
        firebaseAuth = FirebaseAuth.getInstance();
        OneSignal.initWithContext(activity);
        authID = firebaseAuth.getCurrentUser().getUid();
        Log.d("Tag service",authID+ post.getOwnerID());
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
        btnAddToWish = view.findViewById(R.id.btnSearchResultDetailAddToWish);
        btnStartService = view.findViewById(R.id.btnSearchResultDetailStartService);
        btnEndService = view.findViewById(R.id.btnSearchResultDetailEndService);
        btnLocationTracking = view.findViewById(R.id.btnSearchResultDetailTrackLocation);
        // Scroll Element
        postCarDetail.setMovementMethod(new ScrollingMovementMethod());
        postDescription.setMovementMethod(new ScrollingMovementMethod());






        btnSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog = new Dialog(activity);
                dialog.setContentView(R.layout.popup_send_request);
                dialog.show();
                final EditText txtRequestText = dialog.findViewById(R.id.edtPopUpRequestText);
                Button btnpopUpSendRequest = dialog.findViewById(R.id.btnPopupSendRequest);
                Button btnpopUpCancel = dialog.findViewById(R.id.btnPopupCancel);


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

                senderOneSignalID = localDataManager.getSharedPreference(activity,"sharedOneSignalID",null);

                btnpopUpCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();

                    }
                });

                btnpopUpSendRequest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestDAL.sendRequest(senderID, senderName, senderGender, senderImgURL, senderBirthdate, senderEmail, post.getPostID(),
                                post.getOwnerID(), requestLat1, requestLng1, requestLat2, requestLng2,post.getDestination(),txtRequestText.getText().toString(),senderOneSignalID,post.getOwnerOneSignalID(),post.getTimestamp(), new RequestCallback() {
                                    @Override
                                    public void onRequestSent() {
                                        super.onRequestSent();


                                        try {
                                            OneSignal.postNotification(new JSONObject("{'contents': {'en':'Aktif ilaniniza bir kullanici tarafindan istek gonderildi'}, 'include_player_ids': ['" + post.getOwnerOneSignalID() + "']}"), null);
                                            Intent i = new Intent(getContext(), MainActivity.class);
                                            Toast.makeText(activity,"İstek gönderildi.",Toast.LENGTH_LONG).show();
                                            dialog.dismiss();
                                            startActivity(i);
                                            activity.finish();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Toast.makeText(activity,"İstek gönderilirken bir hata meydana geldi.",Toast.LENGTH_LONG).show();
                                        }


                                    }
                                });
                    }
                });






            }
        });
        btnAddToWish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addtoWish();

            }
        });

    }

    private void addtoWish() {
        postDAL.addToWish(post.getOwnerID(), post.getPostID(), new PostCallback() {
            @Override
            public void onWishUpdated() {
                super.onWishUpdated();
                btnAddToWish.setClickable(false);
                btnAddToWish.setFocusable(false);
                btnAddToWish.setText("Takip ediyorsunuz");
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



        //Eğer kullanıcı post sahibiyse ve ilan saatine 3 dakikadan az süre kalmışsa
        long ts = Timestamp.now().getSeconds();
        if(activity.post.getTimestamp() - 180 <= ts && activity.firebaseAuth.getCurrentUser().getUid().equals(activity.post.getOwnerID())){
            if(localDataManager.getSharedPreference(activity,"isServiceEnable",null) ==null || localDataManager.getSharedPreference(activity,"isServiceEnable",null).equals("0")){
                btnStartService.setVisibility(View.VISIBLE);
            }
            else if(localDataManager.getSharedPreference(activity,"isServiceEnable",null).equals("1")){
                btnEndService.setVisibility(View.VISIBLE);
            }

        }



        btnStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    for(Request request: activity.requestList){
                        OneSignal.postNotification(new JSONObject("{'contents': {'en':'Kayit oldugunuz yolculuk baslamistir.'}, 'include_player_ids': ['" + request.getOneSignalID() + "']}"), null);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Intent serviceIntent = new Intent(activity, LocationService.class);
                serviceIntent.putExtra("action","1");
                serviceIntent.putExtra("inputExtra", "Yol arkadaşlarınız yolculuğunuz boyunca sizi takip edebilir");
                serviceIntent.putExtra("postOwnerID",activity.post.getOwnerID());
                serviceIntent.putExtra("postID",activity.post.getPostID());
                serviceIntent.putExtra("authID",authID);

                ContextCompat.startForegroundService(activity, serviceIntent);
                localDataManager.setSharedPreference(activity,"isServiceEnable","1");
                btnStartService.setVisibility(View.GONE);
                btnEndService.setVisibility(View.VISIBLE);

            }
        });

        btnEndService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnEndService.setVisibility(View.GONE);
                btnStartService.setVisibility(View.VISIBLE);
                Intent serviceIntent = new Intent(activity, LocationService.class);
                serviceIntent.putExtra("action","0");
                ContextCompat.startForegroundService(activity, serviceIntent);
                localDataManager.setSharedPreference(activity,"isServiceEnable","0");

            }
        });

        requestDAL.getRequest(post.getPostID(), post.getOwnerID(), new RequestCallback() {
            @Override
            public void onRequestRetrievedNotNull() {
                super.onRequestRetrievedNotNull();

                btnSendRequest.setClickable(false);
                btnSendRequest.setFocusable(false);
                btnSendRequest.setText("İstek gönderildi");
            }
        });

        if(post.getWishArray().contains(activity.firebaseAuth.getCurrentUser().getUid())){
            btnAddToWish.setClickable(false);
            btnAddToWish.setFocusable(false);
            btnAddToWish.setText("Takip ediyorsunuz");
        }

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