package com.furkanmeydan.prototip2.DataLayer;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.furkanmeydan.prototip2.Model.CollectionHelper;
import com.furkanmeydan.prototip2.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class ProfileDAL {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();


    String userid= Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

    public ProfileDAL() {

    }

    public void getProfile(String id, final ProfileCallback profileCallback) {

        firestore.collection(CollectionHelper.USER_COLLECTION).document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    User user = task.getResult().toObject(User.class);
                    if (user != null) {
                        if (user.getBirthDate() != null && !user.getEmail().equals("") && user.getGender() != null && !user.getNameSurname().equals("") && user.getProfilePicture() != null) {
                            Log.d("Tag", "Döküman var");
                            Log.d("Tag", user.getBirthDate() + "doğum tarihi");
                            profileCallback.getUser(user);
                        } else {
                            profileCallback.nullUser();
                        }

                    }
                    else{
                        profileCallback.nullUser();
                    }

                }

            }
        });

    }

    public void uploadProfile(String nameSurname, String email, String birthday
            , String gender, String picURI, Context context,ProfileCallback profileCallback){
        nameSurname=nameSurname.trim();
        email=email.trim();



        if(nameSurname.equals("")){
            Toast.makeText(context,"Lütfen Adınızı Soyadınızı Eksiksiz Girin",Toast.LENGTH_LONG).show();
        }

        else if (email.equals("")){
            Toast.makeText(context,"Lütfen Mail Adresinizi Eksiksiz Girin",Toast.LENGTH_LONG).show();
        }
        else if (birthday.equals("")){
            Toast.makeText(context,"Lütfen Doğum Tarihinizi Girin",Toast.LENGTH_LONG).show();
        }
        else if (picURI==null){
            Toast.makeText(context,"Lütfen Profil Fotoğrafı Seçin",Toast.LENGTH_LONG).show();
        }
        else {
            User newUser = new User(nameSurname,birthday,picURI,email,gender);
            firestore.collection(CollectionHelper.USER_COLLECTION).document(userid).set(newUser);
            profileCallback.uploadProfile();
        }

    }


}
