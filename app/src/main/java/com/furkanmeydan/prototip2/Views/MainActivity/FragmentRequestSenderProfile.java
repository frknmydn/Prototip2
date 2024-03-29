package com.furkanmeydan.prototip2.Views.MainActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.furkanmeydan.prototip2.DataLayer.BlockDAL;
import com.furkanmeydan.prototip2.DataLayer.Callbacks.BlockCallback;
import com.furkanmeydan.prototip2.DataLayer.Callbacks.PostCallback;
import com.furkanmeydan.prototip2.DataLayer.Callbacks.QuestionCallback;
import com.furkanmeydan.prototip2.DataLayer.Callbacks.RequestCallback;
import com.furkanmeydan.prototip2.DataLayer.NotificationManager;
import com.furkanmeydan.prototip2.DataLayer.PostDAL;
import com.furkanmeydan.prototip2.DataLayer.QuestionDAL;
import com.furkanmeydan.prototip2.DataLayer.RequestDAL;
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

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


public class FragmentRequestSenderProfile extends Fragment {
    private TextView nameSurname, gender, birthdate, requestText;
    private ImageView imageView;
    private Button btnShowComments, btnAccept, btnDecline,btnBlock;
    RequestDAL requestDAL;
    BlockDAL blockDAL;
    PostDAL postDAL;
    QuestionDAL questionDAL;
    Request request;

    Dialog dialog;

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
        blockDAL = new BlockDAL();
        postDAL = new PostDAL();
        questionDAL = new QuestionDAL();

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
        mapView = rootView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.onResume();

        mapView.getMapAsync(mMap -> {
            googleMap = mMap;
            //googleMap.setMyLocationEnabled(true);

            googleMap.addMarker(new MarkerOptions().position(latlng1).title("Binmek istediği nokta"));
            googleMap.addMarker(new MarkerOptions().position(latLng2).title("İnmek istediği nokta"));
            CameraPosition cameraPosition = new CameraPosition.Builder().target(latlng1).zoom(14).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


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
        btnBlock = view.findViewById(R.id.fragmentRequestSenderProfileBtnBlock);

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

                        Thread t1 = new Thread(){
                            @Override
                            public void run() {
                                super.run();

                                try {
                                    long timestamp = request.getPostTimestamp()-900L;

                                    @SuppressLint("SimpleDateFormat") SimpleDateFormat dateCombinedFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    Date date = new Date(TimeUnit.SECONDS.toMillis(timestamp));
                                    String dateTime = dateCombinedFormat.format(date) + " GMT+0300";
                                    Log.d("Tag","onesignal date: "+dateTime);
                                    //OneSignal.postNotification(new JSONObject("{'contents': {'en':'Gonderdiginiz isteklerinizden biri onaylandi'}, 'include_external_user_ids': ['" + request.getSenderID() + "']}"), null);
                                    NotificationManager deneme = new NotificationManager(request.getSenderID(),dateTime,"Gonderdiginiz isteklerinizden biri onaylandi","Onay");
                                    deneme.NotificationForNow();
                            /*
                            ///// DENEME DENEMEE DENEME DENEME
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
                                                +   "\"include_external_user_ids\": [\""+request.getSenderID()+"\"],"
                                                +   "\"channel_for_external_user_ids\": \"push\","
                                                +   "\"data\": {\"foo\": \"bar\"},"
                                                +   "\"contents\": {\"en\": \"Gonderdiginiz isteklerinizden biri onaylandi\"},"
                                                +   "\"headings\": {\"en\": \"Onay\"}"
                                                + "}";

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

                                        //String jsonResponse;

                                        url = new URL("https://onesignal.com/api/v1/notifications");
                                         con = (HttpURLConnection)url.openConnection();
                                        con.setUseCaches(false);
                                        con.setDoOutput(true);
                                        con.setDoInput(true);

                                        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                                        con.setRequestProperty("Authorization", "Basic ODkxMDQwZTQtNDJiMS00Y2IzLTgwNGYtMjExNTFhZGEwNWFl");
                                        con.setRequestMethod("POST");
                                        Log.d("Tag" , "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" + request.getSenderID());

                                         strJsonBody = "{"
                                                +   "\"app_id\": \"f374fb5a-1e58-45e9-9f0e-acf96f92c166\","
                                                +   "\"include_external_user_ids\": [\""+request.getSenderID()+"\"],"
                                                +   "\"channel_for_external_user_ids\": \"push\","
                                                +   "\"data\": {\"foo\": \"bar\"},"
                                                +   "\"contents\": {\"en\": \"Ilan sahibi 15 dakika sonra yolculuga baslayacaktir\"},"
                                                +   "\"headings\": {\"en\": \"Hatirlatma\"},"
                                                +   "\"send_after\": \""+dateTime+"\""
                                                + "}";

                                        Log.d("Tag","strJsonBody:\n" + strJsonBody);

                                         sendBytes = strJsonBody.getBytes("UTF-8");
                                        con.setFixedLengthStreamingMode(sendBytes.length);

                                         outputStream = con.getOutputStream();
                                        outputStream.write(sendBytes);

                                         httpResponse = con.getResponseCode();
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
                                        Log.d("Tag","jsonResponse for scheduled:\n" + jsonResponse);




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


                            */

                                    Toast.makeText(activity,"İstek Onaylandı.",Toast.LENGTH_LONG).show();
                                    //OneSignal.postNotification(new JSONObject("{'contents': {'en':'Hatirlatma: yolculuk saatinize 15 dakika kalmistir'}, 'include_external_user_ids': ['" + request.getSenderID() + "']}").put("send_after",dateTime), null);


                            /*
                            Intent i = new Intent(getContext(), MainActivity.class);
                            startActivity(i);
                            activity.finish();
                            */
                                    activity.changeFragment(new FragmentRequestsToMyPosts());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Log.d("Tag","onesignal date: YOH CATCHLEDİ");
                                }
                            }
                        };

                        t1.start();
                        try {
                            t1.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
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


        btnBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(activity);
                dialog.setContentView(R.layout.popup_blockuser_notif);
                dialog.show();
                Button btnBlockYes = dialog.findViewById(R.id.btnPopupBlockYes);
                Button btnBlockNo = dialog.findViewById(R.id.btnPopupBlockNo);
                final EditText edtBlockReason = dialog.findViewById(R.id.edtPopupBlockReason);


                btnBlockYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String userid = activity.userId;

                        final String blockReason = edtBlockReason.getText().toString();
                            requestDAL.getRequestStatusToBlock(userid, request.getSenderID(), new RequestCallback() {
                                @Override
                                public void onAcceptedRequestSearchResult(boolean flag) {
                                    super.onAcceptedRequestSearchResult(flag);
                                    if(!flag){
                                        requestDAL.getRequestStatusToBlock(request.getSenderID(), userid, new RequestCallback() {
                                            @Override
                                            public void onAcceptedRequestSearchResult(boolean flag) {
                                                super.onAcceptedRequestSearchResult(flag);
                                                if(!flag){
                                                    if(blockDAL.checkReason(blockReason,activity)){
                                                        blockDAL.blockUser(userid, request.getSenderID(), blockReason, new BlockCallback() {
                                                            @Override
                                                            public void onUserBlocked() {
                                                                super.onUserBlocked();
                                                                blockDAL.blockUser(request.getSenderID(), userid, null, new BlockCallback() {
                                                                    @Override
                                                                    public void onUserBlocked() {
                                                                        super.onUserBlocked();
                                                                        requestDAL.deleteRequestsOnBlock(userid, request.getSenderID(), new RequestCallback() {
                                                                            @Override
                                                                            public void onRequestsDeletedOnBlock() {
                                                                                super.onRequestsDeletedOnBlock();
                                                                                requestDAL.deleteRequestsOnBlock(request.getSenderID(), userid, new RequestCallback() {
                                                                                    @Override
                                                                                    public void onRequestsDeletedOnBlock() {
                                                                                        super.onRequestsDeletedOnBlock();
                                                                                        postDAL.removeFromWishListForBlock(userid, request.getSenderID(), new PostCallback() {
                                                                                            @Override
                                                                                            public void deleteWishOnBlock() {
                                                                                                super.deleteWishOnBlock();
                                                                                                postDAL.removeFromWishListForBlock(request.getSenderID(), userid, new PostCallback() {
                                                                                                    @Override
                                                                                                    public void deleteWishOnBlock() {
                                                                                                        super.deleteWishOnBlock();
                                                                                                        questionDAL.removeQuestionforBlock(userid, request.getSenderID(), new QuestionCallback() {
                                                                                                            @Override
                                                                                                            public void onQuestionRemovedForBlock() {
                                                                                                                super.onQuestionRemovedForBlock();
                                                                                                                questionDAL.removeQuestionforBlock(request.getSenderID(), userid, new QuestionCallback() {
                                                                                                                    @Override
                                                                                                                    public void onQuestionRemovedForBlock() {
                                                                                                                        super.onQuestionRemovedForBlock();
                                                                                                                        dialog.dismiss();
                                                                                                                        /*
                                                                                                                        Intent i = new Intent(activity,MainActivity.class);
                                                                                                                        startActivity(i);
                                                                                                                        activity.finish();

                                                                                                                         */
                                                                                                                        activity.changeFragment(new HomeFragment());
                                                                                                                    }
                                                                                                                });
                                                                                                            }
                                                                                                        });

                                                                                                    }
                                                                                                });
                                                                                            }
                                                                                        });

                                                                                    }
                                                                                });
                                                                            }
                                                                        });

                                                                    }
                                                                });
                                                            }
                                                        });
                                                    }

                                                }else{
                                                    Toast.makeText(activity,"Bu kullanıcadan size gelen onaylı bir istek bulunmaktadır",Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    }else{
                                        Toast.makeText(activity,"Bu kullanıcya yolladığınız onaylı bir isteğiniz bulunmaktadır",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        dialog.dismiss();
                    }
                });
                btnBlockNo.setOnClickListener(view1 -> dialog.dismiss());
            }
        });

        btnShowComments.setOnClickListener(view12 -> {

        });
    }
}


