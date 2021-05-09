package com.furkanmeydan.prototip2.Views.PostSearchResultDetailActivity;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.furkanmeydan.prototip2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

public class LocationService extends Service {
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    public static final int TWO_MINUTES = 1000*60;
    public LocationManager locationManager;
    public myLocationListener listener;
    public Location previousBestLocation = null;
    RequestQueue queue;
    String url,postOwnerID,postID, authID;



    @Override
    public void onCreate() {
        super.onCreate();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new myLocationListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 0, (LocationListener) listener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 0, listener);

        Log.d("TAGGGG","Çalışıyor");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        queue = Volley.newRequestQueue(this);
        url = "https://carsharingapp.me/api/Positions";

        Log.d("TAGGGG","Çalışıyor");
        Log.d("TAGGGG",intent.getStringExtra("action"));
        if (intent.getStringExtra("action").equals("1")) {
            authID = intent.getStringExtra("authID");
            postOwnerID = intent.getStringExtra("postOwnerID");
            postID = intent.getStringExtra("postID");
            Log.d("Tag service",authID+ postOwnerID);

        String input = intent.getStringExtra("inputExtra");
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, PostSearchResultDetailActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Yolculuk kaydınız başlamıştır.")
                .setContentText(input)
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_light)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
    }
        else if (intent.getStringExtra("action").equals("0")){
            stopForeground(true);
            stopSelf();
        }
        //do heavy work on a background thread
        //stopSelf();
        return START_NOT_STICKY;
    }

    public boolean isBetterLocation(Location location,Location currentBestLocation){
        if(currentBestLocation == null){
            //Önceden bir konum yoksa her türlü yeni konum alınıyor
            return true;
        }

        // Yeni konumun zamanı eski mi yeni mi kontrolü
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < - TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        //Eğer bir önceki konumun üstünden iki dakika geçmiş ise yeni konumu al
        if(isSignificantlyNewer){
            return true;
            //Eğer yeni konumun zamanı iki dakakadan önce ise yeni konumu boşver
        } else if(isSignificantlyOlder){
            return false;
        }

        //Yeni konumun doğruluk kontrolü
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        //Aynı providerlardan geliyorlarsa kontrolü
        boolean isFromSameProvider = isSameProvider(location.getProvider(), currentBestLocation.getProvider());

        // Accuracy ve Zaman kavramını kullanarak konum kalitesi kontrolü
        if(isMoreAccurate){
            return true;
        } else if(isNewer && !isLessAccurate){
            return true;
        } else if(isNewer && !isSignificantlyLessAccurate && isFromSameProvider){
            return true;
        }
        return false;

    }

    private boolean isSameProvider(String provider1, String provider2){
        if(provider1 == null){
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(listener);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    public static Thread performOnBackgroundThread(final Runnable runnable) {
        final Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } finally {

                }
            }
        };
        t.start();
        return t;
    }

    public class myLocationListener implements LocationListener{

        @Override
        public void onLocationChanged(@NonNull Location location) {
            Log.d("Tag","Location has been changed!");
            if(isBetterLocation(location,previousBestLocation)){
                if(authID.equals(postOwnerID)){
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    long time = location.getTime() / 1000;
                    Log.d("Tag","Service içinden gelen yeni değerlerin latı: "+String.valueOf(latitude)+" longitude: "+String.valueOf(longitude)+" time: "+time);


                    JSONObject jsonObject = new JSONObject();

                    try {

                        jsonObject.put("postID",postID);
                        jsonObject.put("latitude",latitude);
                        jsonObject.put("longitude",longitude);
                        jsonObject.put("timestamp",time);
                        jsonObject.put("carOwnerID",postOwnerID);

                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,url, jsonObject, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                Toast.makeText(getApplicationContext(),"Response: " + response.toString(),Toast.LENGTH_LONG).show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(),"VolleyError",Toast.LENGTH_LONG).show();
                            }

                        });
                        queue.add(request);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }




                }

            }

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {
            Toast.makeText( getApplicationContext(), "Gps Açıldı", Toast.LENGTH_SHORT ).show();
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
            Toast.makeText( getApplicationContext(), "Gps Kapatıldı", Toast.LENGTH_SHORT ).show();
        }
    }

}



