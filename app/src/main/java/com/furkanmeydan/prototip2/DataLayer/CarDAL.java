package com.furkanmeydan.prototip2.DataLayer;

import android.content.Context;
import android.os.AsyncTask;

import androidx.room.Room;

import com.furkanmeydan.prototip2.DataLayer.Callbacks.AppDatabase;
import com.furkanmeydan.prototip2.DataLayer.Callbacks.CarCallback;
import com.furkanmeydan.prototip2.Models.Car;
import com.furkanmeydan.prototip2.Models.CollectionHelper;
import com.furkanmeydan.prototip2.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class CarDAL {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userid= firebaseAuth.getCurrentUser().getUid();
    AppDatabase db;


    public void getMyCar(){

    }

    public void uploadCar(int carID, String brand, String model, String color,
                          String type, String optionalInfo, int year, String picURL, Context context,
                          CarCallback carCallback){
        db = Room.databaseBuilder(context,
                AppDatabase.class, "carsDB").build();
        Car newCar = new Car(carID,year,brand,model,color,type,optionalInfo, picURL);
        firestore.collection(CollectionHelper.USER_COLLECTION).document(userid).collection(CollectionHelper.CAR_COLLECTION).add(newCar);
        carCallback.uploadCar(newCar);

        AsyncTask.execute(() -> db.carDao().insertAll(newCar));

    }
}
