package com.furkanmeydan.prototip2.DataLayer;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.furkanmeydan.prototip2.Model.CollectionHelper;
import com.furkanmeydan.prototip2.Model.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class PostDAL {
    Pattern pattern1= Pattern.compile("[^A-Za-z0-9\\sğĞüÜşŞiİöÖçÇı.:,;()']");

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private String userId= Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
    LocalDataManager localDataManager = new LocalDataManager();


    public void uploadPost(String ownerId, Context context, PostCallback postCallback){

        Timestamp nowTimestamp = Timestamp.now();

        String citySharedPrefSpinner = localDataManager.getSharedPreference(context,"city",null);
        String marker1City = localDataManager.getSharedPreference(context,"marker1city",null);
        String marker2City = localDataManager.getSharedPreference(context,"marker2city",null);
        long timestamp = localDataManager.getSharedPreferenceForLong(context,"timestamp",0L);
        int passengerCount = localDataManager.getSharedPreferenceForInt(context,"passengercount",-1);
        String carDetail = localDataManager.getSharedPreference(context,"cardetail",null);
        String description = localDataManager.getSharedPreference(context,"description",null);
        String destination = localDataManager.getSharedPreference(context,"destination",null);
        String fromLat = localDataManager.getSharedPreference(context,"lat_1",null);
        String toLat = localDataManager.getSharedPreference(context,"lat_2",null);
        String fromLng = localDataManager.getSharedPreference(context,"lng_1",null);
        String toLng = localDataManager.getSharedPreference(context,"lng_2",null);
        String userGender = localDataManager.getSharedPreference(context,"sharedGender",null);




        if(userId.equals(ownerId)){

            String errors = "";


            if(destination==null || destination.equals("")){
                Toast.makeText(context,"Lütfen Geçerli İlan Başlığı Girin",Toast.LENGTH_LONG).show();
                errors += "HATA";
            }
            if(passengerCount==-1){
                Toast.makeText(context,"Lütfen Kaç Yol Arkadaşı Alabileceğiniz Seçin",Toast.LENGTH_LONG).show();
                errors += "HATA";
            }
            if(toLat==null || toLng==null || fromLat==null || fromLng == null || marker1City==null || marker2City ==null){
                Toast.makeText(context,"Lütfen Haritadan Kalkış ve Varış Noktalarını Belirtin",Toast.LENGTH_LONG).show();
                errors += "HATA";
            }

            if(citySharedPrefSpinner==null){
                Toast.makeText(context,"Lütfen İlanın Şehrini Seçiniz.",Toast.LENGTH_LONG).show();
                errors += "HATA";

            }

            if(marker1City!=null && !marker1City.equals(marker2City)){
                Toast.makeText(context,"Lütfen Haridata Varış ve Kalkış Noktalarını Aynı Şehir İçin Belirtin",Toast.LENGTH_LONG).show();
                errors += "HATA";
            }

            if(marker1City!=null && marker1City.equals(marker2City) && citySharedPrefSpinner!=null){
                if(!citySharedPrefSpinner.equals(marker1City)){
                    Toast.makeText(context,"Lütfen Haridata Varış ve Kalkış Noktalarını Aynı Şehir İçin Belirtin",Toast.LENGTH_LONG).show();
                    errors += "HATA";
                }
            }
            if(carDetail == null || carDetail.equals("")  ){
                Toast.makeText(context,"Lütfen Araç Hakkında Bilgi Verin",Toast.LENGTH_LONG).show();
                errors += "HATA";
            }

            if(carDetail!=null && !carDetail.equals("")){
                if(carDetail.length()<15){
                    Toast.makeText(context,"Lütfen Araç Hakkında Detaylı Bilgi Verin",Toast.LENGTH_LONG).show();
                    errors += "HATA";
                }

                if(pattern1.matcher(carDetail).find()){
                    Toast.makeText(context,"Lütfen Araç Detayı İçin Geçerli Karakterler Kullanın",Toast.LENGTH_LONG).show();
                    errors += "HATA";
                }

            }

            if(description==null || description.equals("")){
                Toast.makeText(context,"Lütfen Yolculuk Hakkında Bilgi Verin",Toast.LENGTH_LONG).show();
                errors += "HATA";
            }

            //if(description!=null && !description.equals("")) çalışmazsa diye burda duruyo
            else {
                if(description.length()<15){
                    Toast.makeText(context,"Lütfen Yolculuk Hakkında Detaylı Bilgi Verin",Toast.LENGTH_LONG).show();
                    errors += "HATA";
                }

                if(pattern1.matcher(description).find()){
                    Toast.makeText(context,"Lütfen Yolculuk Detayı İçin Geçerli Karakterler Kullanın",Toast.LENGTH_LONG).show();
                    errors += "HATA";
                }
            }


            if(timestamp==0L){
                Toast.makeText(context,"Lütfen Tarih ve Saati seçin",Toast.LENGTH_LONG).show();
                errors += "HATA";

            }
            if(timestamp!=0L){
                long secondsNowTimestamp = nowTimestamp.getSeconds();
                long timestamp14Days = 1209600L;
                if(timestamp<secondsNowTimestamp){
                    Toast.makeText(context,"Geçmişe İlan Veremezsiniz :)",Toast.LENGTH_LONG).show();
                    errors += "HATA";
                }

                if(timestamp>timestamp14Days+secondsNowTimestamp){
                    Toast.makeText(context,"Sadece Gelecek İki Hafta İçin İlan Verebilirsiniz",Toast.LENGTH_LONG).show();
                    errors += "HATA";
                }

            }

            if(errors.isEmpty()){

                Post post = new Post(citySharedPrefSpinner,passengerCount,destination,description,timestamp,carDetail,toLat,toLng,fromLat,fromLng,1,userGender);
                firestore.collection(CollectionHelper.USER_COLLECTION).document(userId).collection(CollectionHelper.POST_COLLECTION).add(post);
                postCallback.onPostAdded();
            }





        }

    }

    public void getPosts(long timestamp1, long timestamp2, String genderString, String cityString, Context context, final PostCallback postCallback){

        firestore.collectionGroup(CollectionHelper.POST_COLLECTION).whereGreaterThan(CollectionHelper.POST_TIMESTAMP,timestamp1)
                .whereLessThan(CollectionHelper.POST_TIMESTAMP,timestamp2)
                .whereEqualTo(CollectionHelper.USER_GENDER,genderString).
                whereEqualTo(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_CITY,cityString).
                get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){
                    if(task.getResult() !=null){
                        Log.d("TagPostGetOnComplete", "For Loop Öncesi");
                        List<Post> list= new ArrayList<>();
                        list = task.getResult().toObjects(Post.class);

                        Log.d("TagPostGetOnComplete", list.get(0).getCity());
                        postCallback.getPosts(list);


                    }else{
                        Log.d("TagPostGetOnComplete","NULL GELİY");
                    }

                }else{

                    Log.d("TagPostGetOnComplete","SUCCESSFUL DEĞİL");
                }
            }
        });
    }


    public Bundle checkArgs(Timestamp timestamp1, Timestamp timestamp2, String genderString, String cityString, Context context){
        String error= "";
        Bundle args = new Bundle();

        if(timestamp1 == null || timestamp2 ==null){
            Toast.makeText(context,"Lütfen Tarih ve Saat Aralığını Belirleyin",Toast.LENGTH_LONG).show();
            error+= "HATA";
        }

        if(cityString == null || cityString.equals("")){
            Toast.makeText(context,"Lütfen Şehir Belirtin",Toast.LENGTH_LONG).show();
            error+= "HATA";
        }
        if(genderString == null || genderString.equals("") || genderString.equals("Cinsiyet")){
            Toast.makeText(context,"Lütfen Cinsiyet Belirtin",Toast.LENGTH_LONG).show();
            error+= "HATA";
        }
        if(timestamp1 !=null && timestamp2 !=null){
            if(timestamp1.getSeconds() >= timestamp2.getSeconds()){
                Toast.makeText(context,"Lütfen Saat Aralığını Doğru Belirtin",Toast.LENGTH_LONG).show();
                error+= "HATA";
            }
        }
        if(error.isEmpty()) {


            args.putLong("timestamp1", timestamp1.getSeconds());
            args.putLong("timestamp2", timestamp2.getSeconds());
            args.putString("gender", genderString);
            args.putString("city", cityString);
            return args;
        }
        else{
            return null;
        }


    }
}
