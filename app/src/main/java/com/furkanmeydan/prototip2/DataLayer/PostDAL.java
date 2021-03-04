package com.furkanmeydan.prototip2.DataLayer;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.furkanmeydan.prototip2.Model.CollectionHelper;
import com.furkanmeydan.prototip2.Model.Post;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

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
        String timestamp = localDataManager.getSharedPreference(context,"timestamp",null);
        String passengerCount = localDataManager.getSharedPreference(context,"passengercount",null);
        String carDetail = localDataManager.getSharedPreference(context,"cardetail",null);
        String description = localDataManager.getSharedPreference(context,"description",null);
        String destination = localDataManager.getSharedPreference(context,"destination",null);
        String fromLat = localDataManager.getSharedPreference(context,"lat_1",null);
        String toLat = localDataManager.getSharedPreference(context,"lat_2",null);
        String fromLng = localDataManager.getSharedPreference(context,"lng_1",null);
        String toLng = localDataManager.getSharedPreference(context,"lng_2",null);





        if(userId.equals(ownerId)){

            String errors = "";


            if(destination==null || destination.equals("")){
                Toast.makeText(context,"Lütfen Geçerli İlan Başlığı Girin",Toast.LENGTH_LONG).show();
                errors += "HATA";
            }
            if(passengerCount==null){
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


            if(timestamp==null){
                Toast.makeText(context,"Lütfen Tarih ve Saati seçin",Toast.LENGTH_LONG).show();
                errors += "HATA";

            }
            if(timestamp!=null){
                long longPostTimestamp = Long.parseLong(timestamp);
                long secondsNowTimestamp = nowTimestamp.getSeconds();
                long timestamp14Days = 1209600L;
                if(longPostTimestamp<secondsNowTimestamp){
                    Toast.makeText(context,"Geçmişe İlan Veremezsiniz :)",Toast.LENGTH_LONG).show();
                    errors += "HATA";
                }

                if(longPostTimestamp>timestamp14Days+secondsNowTimestamp){
                    Toast.makeText(context,"Sadece Gelecek İki Hafta İçin İlan Verebilirsiniz",Toast.LENGTH_LONG).show();
                    errors += "HATA";
                }

            }

            if(errors.isEmpty()){
                Post post = new Post(citySharedPrefSpinner,ownerId,passengerCount,destination,description,timestamp,carDetail,toLat,toLng,fromLat,fromLng);
                firestore.collection(CollectionHelper.POST_COLLECTION).add(post);
                postCallback.onPostAdded();
            }





        }

    }


}
