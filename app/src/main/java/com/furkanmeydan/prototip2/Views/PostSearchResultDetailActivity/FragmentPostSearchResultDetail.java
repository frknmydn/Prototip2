package com.furkanmeydan.prototip2.Views.PostSearchResultDetailActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.furkanmeydan.prototip2.DataLayer.Callbacks.PostCallback;
import com.furkanmeydan.prototip2.DataLayer.Callbacks.RequestCallback;
import com.furkanmeydan.prototip2.DataLayer.LocalDataManager;
import com.furkanmeydan.prototip2.DataLayer.PostDAL;
import com.furkanmeydan.prototip2.DataLayer.RequestDAL;
import com.furkanmeydan.prototip2.Models.Post;
import com.furkanmeydan.prototip2.Models.Request;
import com.furkanmeydan.prototip2.R;
import com.furkanmeydan.prototip2.Views.MainActivity.MainActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.JsonObject;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


public class FragmentPostSearchResultDetail extends Fragment {
    private TextView postHeader, postCity, postPassangerCount, postTime, postDescription, carBrand,
    carModel, carYear, carColor, carOptionalInfo;
    private ImageView carPic;
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
    RequestQueue queue;
    Long currentTimestamp;
    ArrayList<Request> requestList;
    boolean isPostOutdated;
    BroadcastReceiver localBroadcastReceiver;


    PopupWindow carPopup;
    View popupView;

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
        requestList = activity.requestList;
        Log.d("Tag","Requestlist Size: "+requestList.size());
        firebaseAuth = FirebaseAuth.getInstance();
        OneSignal.initWithContext(activity);
        authID = firebaseAuth.getCurrentUser().getUid();
        queue = Volley.newRequestQueue(activity);
        Log.d("Tag service",authID+ post.getOwnerID());
        currentTimestamp = Timestamp.now().getSeconds();
        isPostOutdated = post.getTimestamp() <= currentTimestamp - 240;
        localBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String string = intent.getStringExtra("durdur");
                if(string.equals("durdur")){
                    btnEndService.setVisibility(View.GONE);
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(activity).registerReceiver(localBroadcastReceiver,new IntentFilter("durdur"));
    }

    @Override
    public void onStop() {
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(localBroadcastReceiver);
        super.onStop();

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

        carBrand = view.findViewById(R.id.txtCarBrand3);
        carModel = view.findViewById(R.id.txtCarModel);
        carYear = view.findViewById(R.id.txtCarYear);
        carColor = view.findViewById(R.id.txtCarColor);
        carOptionalInfo = view.findViewById(R.id.txtCarOptionalInfo);
        carPic = view.findViewById(R.id.imageViewCarPic);

        postHeader = view.findViewById(R.id.txtSearchResultDetailHeader);
        postCity = view.findViewById(R.id.txtSearchResultDetailCity);
        postPassangerCount = view.findViewById(R.id.txtSearchResultDetailPasangerCount);
        postTime = view.findViewById(R.id.txtSearchResultDetailDateTime);
        postDescription = view.findViewById(R.id.txtSearchResultDetailDescription);
        btnSendRequest = view.findViewById(R.id.btnSearchResultDetailSendRequest);
        btnAddToWish = view.findViewById(R.id.btnSearchResultDetailAddToWish);
        btnStartService = view.findViewById(R.id.btnSearchResultDetailStartService);
        btnEndService = view.findViewById(R.id.btnSearchResultDetailEndService);
        btnLocationTracking = view.findViewById(R.id.btnSearchResultDetailTrackLocation);
        // Scroll Element
        postDescription.setMovementMethod(new ScrollingMovementMethod());

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        carPopup = new PopupWindow(inflater.inflate(R.layout.popup_car_image, null, false), ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        carPopup.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupView = carPopup.getContentView();


        carPic.setOnClickListener(v -> {
            carPopup.showAtLocation(v, Gravity.CENTER, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            ImageView carImageZoom = popupView.findViewById(R.id.popup_car_image);
            Glide.with(activity.getApplicationContext()).load(activity.post.getCar().getPicURL()).apply(RequestOptions.skipMemoryCacheOf(true))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).into(carImageZoom);

        });




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

                btnpopUpCancel.setOnClickListener(view1 -> dialog.dismiss());

                btnpopUpSendRequest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        requestDAL.sendRequest(senderID, senderName, senderGender, senderImgURL, senderBirthdate, senderEmail, post.getPostID(),
                                post.getOwnerID(), requestLat1, requestLng1, requestLat2, requestLng2,post.getDestination(),txtRequestText.getText().toString(),senderOneSignalID,post.getOwnerOneSignalID(),post.getTimestamp(), new RequestCallback() {
                                    @Override
                                    public void onRequestSent() {
                                        super.onRequestSent();

                                        /*
                                    try {
                                        JSONObject deneme = new JSONObject();
                                        deneme.put("app_id", "f374fb5a-1e58-45e9-9f0e-acf96f92c166");
                                        deneme.put("include_external_user_ids", post.getOwnerID());
                                        deneme.put("channel_for_external_user_ids","push");
                                        deneme.put("data","bar");
                                        deneme.put("contents",new JSONObject("{'en':'Aktif ilaniniza bir kullanici tarafindan istek gonderildi'}"));
                                        OneSignal.postNotification(deneme, new OneSignal.PostNotificationResponseHandler() {
                                            @Override
                                            public void onSuccess(JSONObject response) {
                                                Log.d("TAGGO","notif gönderildi");

                                                Intent i = new Intent(getContext(), MainActivity.class);
                                                Toast.makeText(activity,"İstek gönderildi.",Toast.LENGTH_LONG).show();
                                                dialog.dismiss();
                                                startActivity(i);
                                                activity.finish();

                                            }

                                            @Override
                                            public void onFailure(JSONObject response) {
                                                Log.d("TAGGO","notif gönderilmedi");

                                            }
                                        });


                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                        System.out.print("ananı sikim");
                                    }

                                         */

                                        Thread t1 = new Thread() {
                                            @Override
                                            public void run() {
                                                try {

                                                    String jsonResponse;

                                                    URL url = new URL("https://onesignal.com/api/v1/notifications");
                                                    HttpURLConnection con = (HttpURLConnection)url.openConnection();
                                                    con.setUseCaches(false);
                                                    con.setDoOutput(true);
                                                    con.setDoInput(true);

                                                    con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                                                    con.setRequestProperty("Authorization", "Basic ODkxMDQwZTQtNDJiMS00Y2IzLTgwNGYtMjExNTFhZGEwNWFl");
                                                    con.setRequestMethod("POST");

                                                    String strJsonBody = "{"
                                                            +   "\"app_id\": \"f374fb5a-1e58-45e9-9f0e-acf96f92c166\","
                                                            +   "\"include_external_user_ids\": [\""+post.getOwnerID()+"\"],"
                                                            +   "\"channel_for_external_user_ids\": \"push\","
                                                            +   "\"data\": {\"foo\": \"bar\"},"
                                                            +   "\"contents\": {\"en\": \"Aktif ilaniniza bir kullanici tarafindan istek gonderildi\"},"
                                                            +   "\"headings\": {\"en\": \"İstek\"}"
                                                            + "}";
                                                    /*
                                                    String[] sikis = new String[]{post.getOwnerID()};
                                                    JSONObject deneme = new JSONObject(strJsonBody);
                                                    deneme.put("include_external_user_ids", );
                                                    strJsonBody = deneme.toString();


                                                     */
                                                    Log.d("Tag","strJsonBody:\n" + strJsonBody);

                                                    byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                                                    con.setFixedLengthStreamingMode(sendBytes.length);

                                                    OutputStream outputStream = con.getOutputStream();
                                                    outputStream.write(sendBytes);

                                                    int httpResponse = con.getResponseCode();
                                                    Log.d("Tag","httpResponse: " + httpResponse);

                                                    if (  httpResponse >= HttpURLConnection.HTTP_OK
                                                            && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                                                        Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                                                        jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                                                        scanner.close();
                                                    }
                                                    else {
                                                        Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                                                        jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                                                        scanner.close();
                                                    }
                                                    Log.d("Tag","jsonResponse:\n" + jsonResponse);



                                                } catch(Throwable t) {
                                                    Log.d("Tag","Yarra yedik");
                                                    t.printStackTrace();
                                                }

                                            }
                                        };
                                        t1.start();
                                        try {
                                            t1.join();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        Intent i = new Intent(getContext(), MainActivity.class);
                                        Toast.makeText(activity,"İstek gönderildi.",Toast.LENGTH_LONG).show();
                                        dialog.dismiss();
                                        startActivity(i);
                                        activity.finish();



                                    /*
                                        try {

                                            OneSignal.postNotification(new JSONObject("{'contents': {'en':'Aktif ilaniniza bir kullanici tarafindan istek gonderildi'}, " +
                                                    "'include_external_user_ids': ['" + post.getOwnerID() + "']}"),

                                                    null);
                                            Intent i = new Intent(getContext(), MainActivity.class);
                                            Toast.makeText(activity,"İstek gönderildi.",Toast.LENGTH_LONG).show();
                                            dialog.dismiss();
                                            startActivity(i);
                                            activity.finish();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Toast.makeText(activity,"İstek gönderilirken bir hata meydana geldi.",Toast.LENGTH_LONG).show();
                                        }
                                        */
                                    }
                                });
                    }
                });
            }
        });
        btnAddToWish.setOnClickListener(view12 -> addtoWish());

        btnLocationTracking.setOnClickListener(view13 -> activity.changeFragment(new FragmentPostSearchResultDetailLocationTracking()));

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

    // Eğer API'dan request boş gelmiyor ise tracking sayfasına yönlendircek butonun visibility'sini açmak için
    public void tryRequest(){

        String url = "https://carsharingapp.me/api/Positions/GetPositionByPostID/"+post.getPostID();
        Log.d("Tag","String url : " + url);
        JSONObject jsonObject = new JSONObject();

        JsonObjectRequest volleyRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, jsonObject, response -> {
            try {

                Long responseTimeStamp = response.getLong("timestamp");
                Long timestampDifference = currentTimestamp - responseTimeStamp;
                Log.d("Tag", "current timestamp: "+ currentTimestamp);
                Log.d("Tag", "response timestamp: "+ responseTimeStamp);
                Log.d("Tag", "timestamp arası fark" + timestampDifference);
                if(timestampDifference <= 180 && post.getTimestamp() > currentTimestamp - 180){ //işlemi post sahibi ilanı kapatmadığı sürece yapılması gerektiği için timestamp karşılaştırmsı.
                    btnLocationTracking.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("Tag", "onResponse çalışıyor");

        }, error -> Log.d("Tag", "onErrorResponse çalışıyor: "+ error.getMessage()));

        queue.add(volleyRequest);

    }


    private void setData() {


        postHeader.setText(activity.post.getDestination());
        postCity.setText(activity.post.getCity());
        postPassangerCount.setText(String.valueOf(activity.post.getPassengerCount()));
        postDescription.setText(activity.post.getDescription());

        carBrand.setText(activity.post.getCar().getBrand());
        carModel.setText(activity.post.getCar().getModel());
        carYear.setText(String.valueOf(activity.post.getCar().getYear()));
        carOptionalInfo.setText(activity.post.getCar().getOptionalInfo());
        carColor.setText(activity.post.getCar().getColor());

        Glide.with(activity.getApplicationContext()).load(activity.post.getCar().getPicURL()).apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).into(carPic);
        Log.d("RequestDalPostOwnerId",post.getOwnerID());
        Log.d("RequestDalPostID",post.getPostID());


        if(isPostOutdated){
            btnSendRequest.setVisibility(View.GONE);
            btnAddToWish.setVisibility(View.GONE);
            btnLocationTracking.setVisibility(View.GONE);
            btnStartService.setVisibility(View.GONE);
            btnEndService.setVisibility(View.GONE);
        }

        else {
            //Eğer kullanıcı post sahibiyse ve ilan saatine 3 dakikadan az süre kalmışsa
            long threeMinutesMin = post.getTimestamp() - 180;
            long threeMinutesMax = post.getTimestamp() + 180;

            Log.d("TAG","Timestamp outdated," + isPostOutdated);
            Log.d("TAG","Timestamp min," + threeMinutesMin);
            Log.d("TAG","Timestamp max," + threeMinutesMax);
            Log.d("TAG","Timestamp post," + post.getTimestamp());
            Log.d("TAG","Timestamp current," + currentTimestamp);
            if (activity.firebaseAuth.getCurrentUser().getUid().equals(activity.post.getOwnerID())) {
                Log.d("TAG","İlk if içi");
                if (localDataManager.getSharedPreference(activity, "isServiceEnable", null) == null || localDataManager.getSharedPreference(activity, "isServiceEnable", null).equals("0")) {
                    Log.d("TAG","İkinci if içi");
                    if (currentTimestamp > threeMinutesMin && currentTimestamp < threeMinutesMax) {
                        Log.d("TAG","Üçüncü if içi");
                        btnStartService.setVisibility(View.VISIBLE);
                    }

                } else if (localDataManager.getSharedPreference(activity, "isServiceEnable", null).equals("1")) {
                    btnEndService.setVisibility(View.VISIBLE);
                }

            }


            btnStartService.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                    } else {
                        postDAL.startPost(activity.post.getOwnerID(), activity.post.getPostID(), new PostCallback() {
                            @Override
                            public void onPostUpdated() {
                                super.onPostUpdated();


                        try {
                            for (Request request : activity.requestList) {
                                OneSignal.postNotification(new JSONObject("{'contents': {'en':'Kayit oldugunuz yolculuk baslamistir.'}, 'include_external_user_ids': ['" + request.getSenderID() + "']}"), null);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                        Intent serviceIntent = new Intent(activity, LocationService.class);
                        serviceIntent.putExtra("action", "1");
                        serviceIntent.putExtra("inputExtra", "Yol arkadaşlarınız yolculuğunuz boyunca sizi takip edebilir");
                        serviceIntent.putExtra("postOwnerID", activity.post.getOwnerID());
                        serviceIntent.putExtra("postID", activity.post.getPostID());
                        serviceIntent.putExtra("authID", authID);

                        ContextCompat.startForegroundService(activity, serviceIntent);
                        localDataManager.setSharedPreference(activity, "isServiceEnable", "1");
                        btnStartService.setVisibility(View.GONE);
                        btnEndService.setVisibility(View.VISIBLE);

                            }
                        });
                    }
                }
            });

            btnEndService.setOnClickListener(view -> {
                btnEndService.setVisibility(View.GONE);
                btnStartService.setVisibility(View.VISIBLE);
                Intent serviceIntent = new Intent(activity, LocationService.class);
                serviceIntent.putExtra("action", "0");
                ContextCompat.startForegroundService(activity, serviceIntent);
                localDataManager.setSharedPreference(activity, "isServiceEnable", "0");

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

            if (post.getWishArray().contains(activity.firebaseAuth.getCurrentUser().getUid())) {
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

            Log.d("Tag", "AUTHID : " + authID);
            Log.d("Tag", "RequestList Size : " + requestList.size());
            if (!authID.equals(post.getOwnerID())) {
                for (int i = 0; i < activity.requestList.size(); i++) {
                    Log.d("Tag", "SENDER ID: " + requestList.get(i).getSenderID());

                    if (activity.requestList.get(i).getSenderID().equals(authID)) {

                        tryRequest();
                        break;
                    }
                }
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0){
            if (requestCode == 1 && ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
               Toast.makeText(activity,"Konum izni verilmiştir, lütfen servisi başlatınız",Toast.LENGTH_LONG).show();
                }
            }
        }

    }
