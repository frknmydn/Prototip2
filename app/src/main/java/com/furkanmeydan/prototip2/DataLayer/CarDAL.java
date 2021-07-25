package com.furkanmeydan.prototip2.DataLayer;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.Room;

import com.furkanmeydan.prototip2.DataLayer.Callbacks.AppDatabase;
import com.furkanmeydan.prototip2.DataLayer.Callbacks.CarCallback;
import com.furkanmeydan.prototip2.Models.Car;
import com.furkanmeydan.prototip2.Models.CollectionHelper;
import com.furkanmeydan.prototip2.Models.Request;
import com.furkanmeydan.prototip2.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.List;

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

    public void uploadCar(int carID, String brand, String model, String color,
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
            Car newCar = new Car(carID,year,brand,model,color,type,optionalInfo, picURL);
            firestore.collection(CollectionHelper.USER_COLLECTION).document(userid).collection(CollectionHelper.CAR_COLLECTION).add(newCar);
            carCallback.uploadCar(newCar);

            AsyncTask.execute(() -> db.carDao().insertAll(newCar));
        }



    }
}
