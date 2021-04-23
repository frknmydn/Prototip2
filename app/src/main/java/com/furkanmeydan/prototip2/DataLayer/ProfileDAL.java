package com.furkanmeydan.prototip2.DataLayer;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.furkanmeydan.prototip2.DataLayer.Callbacks.ProfileCallback;
import com.furkanmeydan.prototip2.Models.CollectionHelper;
import com.furkanmeydan.prototip2.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.onesignal.OSDeviceState;
import com.onesignal.OneSignal;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.Objects;
import java.util.regex.Pattern;

public class ProfileDAL {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    EmailValidator validator = EmailValidator.getInstance();


    //İsim soyisimde Alphanumeric karakter olup olmadığının kontrolü için
    Pattern pattern = Pattern.compile("[^a-zA-ZğĞüÜşŞiİöÖçÇı\\s]");

    String userid= Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

    public ProfileDAL() {

    }

    public void getProfile(final String id, final ProfileCallback profileCallback) {

        firestore.collection(CollectionHelper.USER_COLLECTION).document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    Log.d("Tag", "Task başarılı");
                    User user = task.getResult().toObject(User.class);
                    Log.d("Tag", "User oluşturuldu");
                    Log.d("Tag", "Userid:"+id);
                    if (user != null) {
                        Log.d("Tag", "User null değil");
                        if (user.getBirthDate() != null && !user.getEmail().equals("") && user.getGender() != null && !user.getNameSurname().equals("") && user.getProfilePicture() != null) {
                            Log.d("Tag", "Döküman var");
                            Log.d("Tag", user.getOneSignalID() + " onesignalID");
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
        nameSurname=nameSurname.trim().replaceAll(" +"," ");
        email=email.trim();



        if(nameSurname.equals("")){
            Toast.makeText(context,"Lütfen Adınızı Soyadınızı Eksiksiz Girin",Toast.LENGTH_LONG).show();
        }
        else if(pattern.matcher(nameSurname).find()){
            Toast.makeText(context,"Lütfen Geçerli Bilgi Girin",Toast.LENGTH_LONG).show();
        }

        else if (!validator.isValid(email)){
            Toast.makeText(context,"Lütfen Geçerli bir Email Adresi Girin",Toast.LENGTH_LONG).show();
        }


        else if (birthday.equals("")){
            Toast.makeText(context,"Lütfen Doğum Tarihinizi Girin",Toast.LENGTH_LONG).show();
        }
        else if (picURI==null){
            Toast.makeText(context,"Lütfen Profil Fotoğrafı Seçin",Toast.LENGTH_LONG).show();
        }
        else {
            OSDeviceState device = OneSignal.getDeviceState();
            String oneSignalID = device.getUserId();
            User newUser = new User(nameSurname,birthday,picURI,email,gender,oneSignalID);
            firestore.collection(CollectionHelper.USER_COLLECTION).document(userid).set(newUser);
            profileCallback.uploadProfile(newUser);
        }

    }


}
