package com.furkanmeydan.prototip2.DataLayer;

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


    public void getMyCar(){

    }

    public void uploadCar(int carID, String brand, String model, String color,
                          String type, String optionalInfo, int year, String picURL,
                          CarCallback carCallback){
        Car newCar = new Car(carID,year,brand,model,color,type,optionalInfo, picURL);
        firestore.collection(CollectionHelper.USER_COLLECTION).document(userid).collection(CollectionHelper.CAR_COLLECTION).document(picURL).set(newCar);
        carCallback.uploadCar(newCar);

    }
}
