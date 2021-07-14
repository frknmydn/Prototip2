package com.furkanmeydan.prototip2.DataLayer;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import com.furkanmeydan.prototip2.DataLayer.Callbacks.PostCallback;
import com.furkanmeydan.prototip2.DataLayer.Callbacks.RequestCallback;
import com.furkanmeydan.prototip2.Models.CollectionHelper;
import com.furkanmeydan.prototip2.Models.Post;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

public class PostDAL {
    Pattern pattern1 = Pattern.compile("[^A-Za-z0-9\\sğĞüÜşŞiİöÖçÇı.:,;()']");

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private String userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
    LocalDataManager localDataManager = new LocalDataManager();
    LocalDataManager localDataManagerUser = new LocalDataManager();


    public void uploadPost(String ownerId, Context context, PostCallback postCallback) {

        Timestamp nowTimestamp = Timestamp.now();

        String citySharedPrefSpinner = localDataManager.getSharedPreference(context, "city", null);
        String marker1City = localDataManager.getSharedPreference(context, "marker1city", null);
        String marker2City = localDataManager.getSharedPreference(context, "marker2city", null);
        long timestamp = localDataManager.getSharedPreferenceForLong(context, "timestamp", 0L);
        int passengerCount = localDataManager.getSharedPreferenceForInt(context, "passengercount", -1);
        String carDetail = localDataManager.getSharedPreference(context, "cardetail", null);
        String description = localDataManager.getSharedPreference(context, "description", null);
        String destination = localDataManager.getSharedPreference(context, "destination", null);

        double fromLat = localDataManager.getSharedPreferenceForDouble(context, "lat_1", 0d);
        double toLat = localDataManager.getSharedPreferenceForDouble(context, "lat_2", 0d);
        double fromLng = localDataManager.getSharedPreferenceForDouble(context, "lng_1", 0d);
        double toLng = localDataManager.getSharedPreferenceForDouble(context, "lng_2", 0d);
        String postID = UUID.randomUUID().toString();

        String postOwnerOneSignalID = localDataManager.getSharedPreference(context,"sharedOneSignalID",null);

        String userGender = localDataManagerUser.getSharedPreference(context, "sharedGender", null);


        if (userId.equals(ownerId)) {

            String errors = "";


            if (destination == null || destination.equals("")) {
                Toast.makeText(context, "Lütfen Geçerli İlan Başlığı Girin", Toast.LENGTH_LONG).show();
                errors += "HATA";
            }
            if (passengerCount == -1) {
                Toast.makeText(context, "Lütfen Kaç Yol Arkadaşı Alabileceğiniz Seçin", Toast.LENGTH_LONG).show();
                errors += "HATA";
            }
            if (toLat == 0 || toLng == 0 || fromLat == 0 || fromLng == 0 || marker1City == null || marker2City == null) {
                Toast.makeText(context, "Lütfen Haritadan Kalkış ve Varış Noktalarını Belirtin", Toast.LENGTH_LONG).show();
                errors += "HATA";

            }

            if (citySharedPrefSpinner == null) {
                Toast.makeText(context, "Lütfen İlanın Şehrini Seçiniz.", Toast.LENGTH_LONG).show();
                errors += "HATA";

            }

            if (marker1City != null && !marker1City.equals(marker2City)) {
                Toast.makeText(context, "Lütfen Haridata Varış ve Kalkış Noktalarını Aynı Şehir İçin Belirtin", Toast.LENGTH_LONG).show();
                errors += "HATA";
            }

            if (marker1City != null && marker1City.equals(marker2City) && citySharedPrefSpinner != null) {
                if (!citySharedPrefSpinner.equals(marker1City)) {
                    Toast.makeText(context, "Lütfen Haridata Varış ve Kalkış Noktalarını Aynı Şehir İçin Belirtin", Toast.LENGTH_LONG).show();
                    errors += "HATA";
                }
            }
            if (carDetail == null || carDetail.equals("")) {
                Toast.makeText(context, "Lütfen Araç Hakkında Bilgi Verin", Toast.LENGTH_LONG).show();
                errors += "HATA";
            }

            if (carDetail != null && !carDetail.equals("")) {
                if (carDetail.length() < 15) {
                    Toast.makeText(context, "Lütfen Araç Hakkında Detaylı Bilgi Verin", Toast.LENGTH_LONG).show();
                    errors += "HATA";
                }

                if (pattern1.matcher(carDetail).find()) {
                    Toast.makeText(context, "Lütfen Araç Detayı İçin Geçerli Karakterler Kullanın", Toast.LENGTH_LONG).show();
                    errors += "HATA";
                }

            }

            if (description == null || description.equals("")) {
                Toast.makeText(context, "Lütfen Yolculuk Hakkında Bilgi Verin", Toast.LENGTH_LONG).show();
                errors += "HATA";
            }

            //if(description!=null && !description.equals("")) çalışmazsa diye burda duruyo
            else {
                if (description.length() < 15) {
                    Toast.makeText(context, "Lütfen Yolculuk Hakkında Detaylı Bilgi Verin", Toast.LENGTH_LONG).show();
                    errors += "HATA";
                }

                if (pattern1.matcher(description).find()) {
                    Toast.makeText(context, "Lütfen Yolculuk Detayı İçin Geçerli Karakterler Kullanın", Toast.LENGTH_LONG).show();
                    errors += "HATA";
                }
            }


            if (timestamp == 0L) {
                Toast.makeText(context, "Lütfen Tarih ve Saati seçin", Toast.LENGTH_LONG).show();
                errors += "HATA";

            }
            if (timestamp != 0L) {
                long secondsNowTimestamp = nowTimestamp.getSeconds();
                long timestamp14Days = 1209600L;
                if (timestamp < secondsNowTimestamp) {
                    Toast.makeText(context, "Geçmişe İlan Veremezsiniz :)", Toast.LENGTH_LONG).show();
                    errors += "HATA";
                }

                if (timestamp > timestamp14Days + secondsNowTimestamp) {
                    Toast.makeText(context, "Sadece Gelecek İki Hafta İçin İlan Verebilirsiniz", Toast.LENGTH_LONG).show();
                    errors += "HATA";
                }
                if(timestamp < secondsNowTimestamp + 3600L ){
                    Toast.makeText(context, "Minimum 1 saat sonrasına ilan koyabilirsiniz", Toast.LENGTH_LONG).show();
                    errors += "HATA";
                }

            }


            if (errors.isEmpty()) {

                int direction = findDirection(fromLat, fromLng, toLat, toLng);

                ArrayList<String> wishArray = new ArrayList<>();

                Post post = new Post(postID,ownerId,citySharedPrefSpinner, passengerCount, destination, description, timestamp, carDetail, toLat, toLng, fromLat, fromLng, 1,0, userGender, direction,wishArray,postOwnerOneSignalID);
                firestore.collection(CollectionHelper.USER_COLLECTION).document(userId).collection(CollectionHelper.POST_COLLECTION).document(postID).set(post);
                postCallback.onPostAdded(post);
            }


        }

    }

    public int findDirection(double fromLat, double fromLng, double toLat, double toLng) {
        int direction;
        //Kuzey
        if (toLat > fromLat) {
            //Doğu
            if (toLng > fromLng) {
                direction = 0;
            }
            //Batı
            else {
                direction = 3;
            }

        }
        //Güney
        else {
            //Doğu
            if (toLng > fromLng) {
                direction = 1;

            } else {
                direction = 2;
            }

        }

        return direction;
    }

    public void getPostsWithGender(long timestamp1, long timestamp2, String genderString, String cityString, int direction, final PostCallback postCallback) {

        /*
            double fromLat = localDataManager.getSharedPreferenceForDouble(context, "lat_1", 0d);
            double toLat = localDataManager.getSharedPreferenceForDouble(context, "lat_2", 0d);
            double fromLng = localDataManager.getSharedPreferenceForDouble(context, "lng_1", 0d);
            double toLng = localDataManager.getSharedPreferenceForDouble(context, "lng_2", 0d);
         */


        /*
        if(userToLat > userFromLat){
            if(userToLng > userFromLng){
                firestore.collectionGroup(CollectionHelper.POST_COLLECTION).
                        whereEqualTo(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_STATUS,1)
                        .whereEqualTo(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_CITY,cityString)
                        .whereGreaterThan(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_TIMESTAMP,timestamp1)
                        .whereLessThan(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_TIMESTAMP,timestamp2)
                        .whereEqualTo(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_GENDER,genderString)
                        .whereGreaterThan(CollectionHelper.POST_TOLAT,userFromLat)
                        .whereGreaterThan(CollectionHelper.POST_TOLNG,userFromLng).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {

                            if (task.getResult() != null) {
                                List<Post> list = task.getResult().toObjects(Post.class);
                                System.out.println("Gelen liste boyutu: "+ list.size());
                                postCallback.getPosts(list);

                            }
                        }

                    }
                });

            }else if(userToLng < userFromLng){
                (firestore.collectionGroup(CollectionHelper.POST_COLLECTION).
                        whereEqualTo(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_STATUS,1)
                        .whereEqualTo(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_CITY,cityString)
                        .whereGreaterThan(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_TIMESTAMP,timestamp1)
                        .whereLessThan(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_TIMESTAMP,timestamp2)
                        .whereEqualTo(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_GENDER,genderString))
                        .whereGreaterThan(CollectionHelper.POST_TOLAT,userFromLat)
                        .whereLessThan(CollectionHelper.POST_TOLNG,userFromLng).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {

                            if (task.getResult() != null) {
                                List<Post> list = task.getResult().toObjects(Post.class);
                                System.out.println("Gelen liste boyutu: "+ list.size());
                                postCallback.getPosts(list);

                            }
                        }

                    }
                });
            }

        }
        else if(userToLat < userFromLat){
            if(userToLng> userFromLng){
                (firestore.collectionGroup(CollectionHelper.POST_COLLECTION).
                        whereEqualTo(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_STATUS,1)
                        .whereEqualTo(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_CITY,cityString)
                        .whereGreaterThan(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_TIMESTAMP,timestamp1)
                        .whereLessThan(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_TIMESTAMP,timestamp2)
                        .whereEqualTo(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_GENDER,genderString))
                        .whereLessThan(CollectionHelper.POST_TOLAT,userFromLat)
                        .whereGreaterThan(CollectionHelper.POST_TOLNG,userFromLng)
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful()) {

                            if (task.getResult() != null) {
                                List<Post> list = task.getResult().toObjects(Post.class);
                                System.out.println("Gelen liste boyutu: "+ list.size());
                                postCallback.getPosts(list);

                            }
                        }
                    }
                });



            }else if(userToLng < userFromLng){
                (firestore.collectionGroup(CollectionHelper.POST_COLLECTION).
                        whereEqualTo(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_STATUS,1)
                        .whereEqualTo(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_CITY,cityString)
                        .whereGreaterThan(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_TIMESTAMP,timestamp1)
                        .whereLessThan(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_TIMESTAMP,timestamp2)
                        .whereEqualTo(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_GENDER,genderString))
                        .whereLessThan(CollectionHelper.POST_TOLAT,userFromLat)
                        .whereLessThan(CollectionHelper.POST_TOLNG,userFromLng)
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {

                            if (task.getResult() != null) {
                                List<Post> list = task.getResult().toObjects(Post.class);
                                System.out.println("Gelen liste boyutu: "+ list.size());
                                postCallback.getPosts(list);

                            }
                        }

                    }
                });
            }

        }

         */



        firestore.collectionGroup(CollectionHelper.POST_COLLECTION)
                .whereEqualTo(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_STATUS, 1)
                .whereEqualTo(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_CITY, cityString)
                .whereEqualTo(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_GENDER, genderString)
                .whereEqualTo(CollectionHelper.POST_DIRECTION, direction)
                .whereGreaterThan(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_TIMESTAMP, timestamp1)
                .whereLessThan(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_TIMESTAMP, timestamp2)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        if (task.getResult() != null) {
                            List<Post> list = task.getResult().toObjects(Post.class);
                            System.out.println("Gelen liste boyutu: " + list.size());
                            postCallback.getPosts(list);

                        }
                    }
                });


    }


    public void getPostsWithOUTGender(long timestamp1, long timestamp2, String cityString, Context context, int direction, final PostCallback postCallback) {

        double fromLat = localDataManager.getSharedPreferenceForDouble(context, "lat_1", 0d);
        double toLat = localDataManager.getSharedPreferenceForDouble(context, "lat_2", 0d);
        double fromLng = localDataManager.getSharedPreferenceForDouble(context, "lng_1", 0d);
        double toLng = localDataManager.getSharedPreferenceForDouble(context, "lng_2", 0d);

        /*
        if(userToLat > userFromLat){
            if(userToLng > userFromLng){
                firestore.collectionGroup(CollectionHelper.POST_COLLECTION).
                        whereEqualTo(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_STATUS,1)
                        .whereEqualTo(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_CITY,cityString)
                        .whereGreaterThan(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_TIMESTAMP,timestamp1)
                        .whereLessThan(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_TIMESTAMP,timestamp2)
                        .whereGreaterThan(CollectionHelper.POST_TOLAT,userFromLat)
                        .whereGreaterThan(CollectionHelper.POST_TOLNG,userFromLng).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {

                            if (task.getResult() != null) {
                                List<Post> list = task.getResult().toObjects(Post.class);
                                System.out.println("Gelen liste boyutu: "+ list.size());
                                postCallback.getPosts(list);

                            }
                        }

                    }
                });

            }else if(userToLng < userFromLng){
                firestore.collectionGroup(CollectionHelper.POST_COLLECTION).
                        whereEqualTo(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_STATUS,1)
                        .whereEqualTo(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_CITY,cityString)
                        .whereGreaterThan(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_TIMESTAMP,timestamp1)
                        .whereLessThan(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_TIMESTAMP,timestamp2)
                        .whereGreaterThan(CollectionHelper.POST_TOLAT,userFromLat)
                        .whereLessThan(CollectionHelper.POST_TOLNG,userFromLng).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {

                            if (task.getResult() != null) {
                                List<Post> list = task.getResult().toObjects(Post.class);
                                System.out.println("Gelen liste boyutu: "+ list.size());
                                postCallback.getPosts(list);

                            }
                        }

                    }
                });
            }

        }
        else if(userToLat < userFromLat){
            if(userToLng> userFromLng){
                firestore.collectionGroup(CollectionHelper.POST_COLLECTION).
                        whereEqualTo(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_STATUS,1)
                        .whereEqualTo(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_CITY,cityString)
                        .whereGreaterThan(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_TIMESTAMP,timestamp1)
                        .whereLessThan(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_TIMESTAMP,timestamp2)
                        .whereLessThan(CollectionHelper.POST_TOLAT,userFromLat)
                        .whereGreaterThan(CollectionHelper.POST_TOLNG,userFromLng)
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful()) {

                            if (task.getResult() != null) {
                                List<Post> list = task.getResult().toObjects(Post.class);
                                System.out.println("Gelen liste boyutu: "+ list.size());
                                postCallback.getPosts(list);

                            }
                        }
                    }
                });



            }else if(userToLng < userFromLng){
                firestore.collectionGroup(CollectionHelper.POST_COLLECTION).
                        whereEqualTo(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_STATUS,1)
                        .whereEqualTo(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_CITY,cityString)
                        .whereGreaterThan(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_TIMESTAMP,timestamp1)
                        .whereLessThan(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_TIMESTAMP,timestamp2)
                        .whereLessThan(CollectionHelper.POST_TOLAT,userFromLat)
                        .whereLessThan(CollectionHelper.POST_TOLNG,userFromLng)
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {

                            if (task.getResult() != null) {
                                List<Post> list = task.getResult().toObjects(Post.class);
                                System.out.println("Gelen liste boyutu: "+ list.size());
                                postCallback.getPosts(list);

                            }
                        }

                    }
                });
            }

        }

         */


        firestore.collectionGroup(CollectionHelper.POST_COLLECTION)
                .whereEqualTo(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_STATUS, 1)
                .whereEqualTo(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_CITY, cityString)
                .whereEqualTo(CollectionHelper.POST_DIRECTION, direction)
                .whereGreaterThan(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_TIMESTAMP, timestamp1)
                .whereLessThan(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_TIMESTAMP, timestamp2)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult() != null) {

                            List<Post> list = task.getResult().toObjects(Post.class);
                            System.out.println("Gelen liste boyutu: " + list.size());
                            postCallback.getPosts(list);

                        }
                    }
                });


    }


    public void getMyPosts(final PostCallback postCallback) {

        long currentTimestamp = Timestamp.now().getSeconds();

        firestore.collection(CollectionHelper.USER_COLLECTION)
                .document(userId)
                .collection(CollectionHelper.POST_COLLECTION)
                .whereEqualTo(CollectionHelper.POST_STATUS,1)
                .whereGreaterThan(CollectionHelper.POST_TIMESTAMP, currentTimestamp - 10*60)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult() != null) {
                            List<Post> list = task.getResult().toObjects(Post.class);
                            postCallback.getMyPosts(list);
                        }


                    }
                });
    }
    public void getMyOlderPosts(final PostCallback postCallback) {

        long currentTimestamp = Timestamp.now().getSeconds();

        firestore.collection(CollectionHelper.USER_COLLECTION)
                .document(userId)
                .collection(CollectionHelper.POST_COLLECTION)
                //.whereEqualTo(CollectionHelper.POST_STATUS,2) Daha süresi geçen postların statusunu otomatik 2 yapma fonksiyonumuz yok
                .whereLessThan(CollectionHelper.POST_TIMESTAMP, currentTimestamp)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult() != null) {
                            List<Post> list = task.getResult().toObjects(Post.class);
                            postCallback.getMyPosts(list);
                        }

                    }
                });
    }
    public void getMyDeletedPosts(final PostCallback postCallback) {

        firestore.collection(CollectionHelper.USER_COLLECTION)
                .document(userId)
                .collection(CollectionHelper.POST_COLLECTION)
                .whereEqualTo(CollectionHelper.POST_STATUS,0)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult() != null) {
                            List<Post> list = task.getResult().toObjects(Post.class);
                            postCallback.getMyPosts(list);
                        }


                    }
                });
    }

    public List<Post> filterWithLTGLNG(List<Post> posts, double userToLat, double userToLng, double userFromLat, double userFromLng) {
        List<Post> filteredList = new ArrayList<>();


        if (posts != null && posts.size()>0) {
            int postDirection = posts.get(0).getDirection();
            if (postDirection == 0) {
                for (Post i : posts) {

                    if (i.getToLat() + 0.003d > userToLat && i.getToLng() + 0.003d > userToLng && i.getFromLat() - 0.003d < userFromLat && i.getFromLng() - 0.003d < userFromLng) {
                        filteredList.add(i);
                    }


                }

            } else if (postDirection == 1) {
                for (Post i : posts) {

                    if (i.getToLat() - 0.003d < userToLat && i.getToLng() + 0.003d > userToLng && i.getFromLat() + 0.003d > userFromLat && i.getFromLng() - 0.003d < userFromLng) {
                        filteredList.add(i);
                    }


                }

            } else if (postDirection == 2) {
                for (Post i : posts) {

                    if (i.getToLat() - 0.003d < userToLat && i.getToLng() - 0.003d < userToLng && i.getFromLat() + 0.003d > userFromLat && i.getFromLng() + 0.003d > userFromLng) {
                        filteredList.add(i);
                    }


                }

            } else if (postDirection == 3) {
                for (Post i : posts) {

                    if (i.getToLat() + 0.003d > userToLat && i.getToLng() - 0.003d < userToLng && i.getFromLat() - 0.003d < userFromLat && i.getFromLng() + 0.003d > userFromLng) {
                        filteredList.add(i);
                    }


                }

            }

        }


        /*
        //Yukarı sağa
        if(userToLat > userFromLat) { // yukarı gitmek istiyorsa
            if(userToLng > userFromLng) { // sağa gitmek istiyorsa
                for(Post i :posts ){
                    if(i.getToLat()>userFromLat){
                        if(i.getToLng()>userFromLng){
                            //
                            if(i.getToLat()+0.003d>userToLat && i.getToLng()+0.003d>userToLng && i.getFromLat()-0.003d<userFromLat && i.getFromLng()-0.003d<userFromLng){
                                filteredList.add(i);
                            }

                        }
                    }
                }

            }
            //Yukarı sola
            else if(userToLng<userFromLng){
                for(Post i : posts){
                    if(i.getToLat()>userFromLat){
                        if(i.getToLng()<userFromLng){
                            if(i.getToLat()+0.003d>userToLat && i.getToLng()-0.003d<userToLng && i.getFromLat()-0.003d<userFromLat && i.getFromLng()+0.003d>userFromLng){
                                filteredList.add(i);
                            }

                        }
                    }
                }
            }
        }
        //Aşağı sağa
        else if(userToLat <= userFromLat){
            if(userToLng > userFromLng) { // sağa gitmek istiyorsa
                for(Post i :posts ){
                    if(i.getToLat()<userFromLat){
                        if(i.getToLng()>userFromLng){
                            if(i.getToLat()-0.003d<userToLat && i.getToLng()+0.003d>userToLng && i.getFromLat()+0.003d>userFromLat && i.getFromLng()-0.003d<userFromLng){
                                filteredList.add(i);
                            }

                        }
                    }
                }

            }
            //Aşağı sola
            else if(userToLng<userFromLng){
                for(Post i : posts){
                    if(i.getToLat()<userFromLat){
                        if(i.getToLng()<userFromLng){
                            if(i.getToLat()-0.003d<userToLat && i.getToLng()-0.003d<userToLng && i.getFromLat()+0.003d>userFromLat && i.getFromLng()+0.003d>userFromLng){
                                filteredList.add(i);
                            }

                        }
                    }
                }
            }
        }


         */
        return filteredList;
    }

    public void deletePost(Post post, final PostCallback callback){
        firestore.collection(CollectionHelper.USER_COLLECTION).document(post.getOwnerID())
                .collection(CollectionHelper.POST_COLLECTION)
                .document(post.getPostID()).update(CollectionHelper.POST_STATUS, 0).addOnCompleteListener(task -> callback.onPostDeleted());
    }


    public void getSinglePost(String postOwnerID, String postID, final PostCallback callback){
        firestore.collection(CollectionHelper.USER_COLLECTION).document(postOwnerID)
                 .collection(CollectionHelper.POST_COLLECTION).document(postID).get()
                 .addOnCompleteListener(task -> {
                     if(task.isSuccessful()){
                         if(task.getResult() !=null){
                             Post post = task.getResult().toObject(Post.class);
                             callback.getPost(post);
                         }
                     }
                 });

    }
    public void addToWish(String postOwnerID, String postID, final PostCallback callback){


        firestore.collection(CollectionHelper.USER_COLLECTION).document(postOwnerID).collection(CollectionHelper.POST_COLLECTION)
                 .document(postID).get().addOnCompleteListener(task -> {
                    if(task.isSuccessful() &&task.getResult() !=null) {
                        DocumentReference ref = task.getResult().getReference();
                        Post post = task.getResult().toObject(Post.class);
                        ArrayList<String> wishArray = post.getWishArray();
                        if(!wishArray.contains(userId)){
                            wishArray.add(userId);
                        }


                        ref.update(CollectionHelper.POST_WISHARRAY,wishArray).addOnCompleteListener(task1 -> callback.onWishUpdated());
                    }
                 });

    }

    public void removeFromWishListForBlock(String blockerID, String blockedID, PostCallback callback){

        firestore.collection(CollectionHelper.USER_COLLECTION).document(blockerID).collection(CollectionHelper.POST_COLLECTION)
                .whereArrayContains(CollectionHelper.POST_WISHARRAY,blockedID).get().addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult() !=null){
                        for(DocumentSnapshot ds : task.getResult().getDocuments()){
                            ds.getReference().update(CollectionHelper.POST_WISHARRAY, FieldValue.arrayRemove(blockedID));
                        }
                        callback.deleteWishOnBlock();
                    }
                });


    }

    public void getWishList(final PostCallback callback){
        firestore.collectionGroup(CollectionHelper.POST_COLLECTION)
                .whereArrayContains(CollectionHelper.POST_WISHARRAY,userId)
                .whereEqualTo(CollectionHelper.POST_STATUS,1)
                .get().addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult()!=null){
                        if(task.getResult().toObjects(Post.class).size() > 0){
                            List<Post> postList = task.getResult().toObjects(Post.class);
                            Log.d("Tag","PostDAL getWishList size:" +" "+  postList.size());
                            callback.OnWishListRetrieved(postList);
                        }
                        else{
                            Log.d("Tag","PostDAL GetWishList patliyor");
                        }
                    }
                });
    }

    public void startPost(String postOwnerID, String postID, final PostCallback callback){

        firestore.collection(CollectionHelper.USER_COLLECTION).document(postOwnerID).collection(CollectionHelper.POST_COLLECTION)
                .document(postID).get().addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult() !=null){
                        DocumentReference dr = task.getResult().getReference();
                        dr.update(CollectionHelper.POST_HASSTARTED,1);
                        callback.onPostUpdated();
                    }
                });


    }
    public void decreasePassengerCount(String postID, String postOwnerID, final RequestCallback callback){

        firestore.collection(CollectionHelper.USER_COLLECTION)
                .document(postOwnerID).collection(CollectionHelper.POST_COLLECTION)
                .document(postID).get().addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult() !=null){
                        DocumentSnapshot ds = task.getResult();
                        DocumentReference dr = ds.getReference();
                        Long passengerCount = (Long) ds.get(CollectionHelper.POST_PASSENGERCOUNT);
                        if(passengerCount > 0L){
                            dr.update(CollectionHelper.POST_PASSENGERCOUNT,FieldValue.increment(-1));
                            passengerCount--;
                            if(passengerCount == 0L){
                                dr.update(CollectionHelper.POST_STATUS, 3).addOnCompleteListener(task1 -> callback.onRequestAccepted());
                            }
                            callback.onRequestAccepted();
                        }




                    }
                });

    }
    public Bundle checkArgs(Timestamp timestamp1, Timestamp timestamp2, String genderString, String cityString, Context context) {
        String error = "";

        Bundle args = new Bundle();

        if (timestamp1 == null || timestamp2 == null) {
            Toast.makeText(context, "Lütfen Tarih ve Saat Aralığını Belirleyin", Toast.LENGTH_LONG).show();
            error += "HATA";
        }

        if (cityString == null || cityString.equals("")) {
            Toast.makeText(context, "Lütfen Şehir Belirtin", Toast.LENGTH_LONG).show();
            error += "HATA";
        }
        if (genderString == null || genderString.equals("") || genderString.equals("Cinsiyet")) {
            Toast.makeText(context, "Lütfen Cinsiyet Belirtin", Toast.LENGTH_LONG).show();
            error += "HATA";
        }
        if (timestamp1 != null && timestamp2 != null) {
            if (timestamp1.getSeconds() >= timestamp2.getSeconds()) {
                Toast.makeText(context, "Lütfen Saat Aralığını Doğru Belirtin", Toast.LENGTH_LONG).show();
                error += "HATA";
            }
        }
        if (error.isEmpty()) {


            args.putLong("timestamp1", timestamp1.getSeconds());
            args.putLong("timestamp2", timestamp2.getSeconds());
            args.putString("gender", genderString);
            args.putString("city", cityString);
            return args;
        } else {
            return null;
        }


    }
}



