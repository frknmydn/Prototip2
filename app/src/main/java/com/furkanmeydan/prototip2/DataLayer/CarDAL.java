package com.furkanmeydan.prototip2.DataLayer;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Room;

import com.furkanmeydan.prototip2.DataLayer.Callbacks.AppDatabase;
import com.furkanmeydan.prototip2.DataLayer.Callbacks.CarCallback;
import com.furkanmeydan.prototip2.Models.Car;
import com.furkanmeydan.prototip2.Models.CollectionHelper;
import com.furkanmeydan.prototip2.Models.Request;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class CarDAL {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userid= firebaseAuth.getCurrentUser().getUid();
    AppDatabase db;


    public void getMyCars(String useridd,CarCallback carCallback){
        firestore.collection(CollectionHelper.USER_COLLECTION).
                document(useridd).collection(CollectionHelper.CAR_COLLECTION).get().addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult() !=null){
                        if(task.getResult().toObjects(Request.class).size() > 0){
                            List<Car> list = task.getResult().toObjects(Car.class);
                            Log.d("TAGGO", "getMyCars: " + list.size());
                            carCallback.getMyCars(list);
                        }
                    }
                });

    }

    public void uploadCar(String brand, String model, String color,
                          String type, String optionalInfo, int year, String picURL, Context context,
                          CarCallback carCallback){
        db = Room.databaseBuilder(context,
                AppDatabase.class, "carsDB").build();

        if(brand==null){
            Toast.makeText(context, "Lütfen marka seçiniz",Toast.LENGTH_LONG).show();
        }
        else if(model==null){
            Toast.makeText(context, "Lütfen model seçiniz",Toast.LENGTH_LONG).show();
        }
        else if(year == 0){
            Toast.makeText(context, "Lütfen model yılını seçiniz",Toast.LENGTH_LONG).show();
        }
        else if(picURL == null) {
            Toast.makeText(context, "Lütfen aracınıza ait fotoğraf yükleyiniz", Toast.LENGTH_LONG).show();

        }
        else {
            String carDocID = UUID.randomUUID().toString();
            Car newCarRoom = new Car(carDocID,year,brand,model,color,type,optionalInfo, picURL, userid);

            Car newCar = new Car(0,carDocID,brand,model,color,type,optionalInfo,picURL,year,userid);

            firestore.collection(CollectionHelper.USER_COLLECTION).document(userid)
                    .collection(CollectionHelper.CAR_COLLECTION)
                    .document(carDocID).set(newCar)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {
                    carCallback.uploadCar(newCar);
                }
            });


            AsyncTask.execute(() -> db.carDao().insertAll(newCarRoom));
        }



    }

    public void deleteCar(String carID, CarCallback callback){
            firestore.collection(CollectionHelper.USER_COLLECTION)
                    .document(userid).collection(CollectionHelper.CAR_COLLECTION)
                    .document(carID)
                    .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {
                    callback.deleteCar();
                }
            });
    }
}
