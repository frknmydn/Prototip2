package com.furkanmeydan.prototip2.Views.OpeningActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.furkanmeydan.prototip2.Models.ConnectionChecker;
import com.furkanmeydan.prototip2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ActivityOpeningPage extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseUser fuser;
    ConnectionChecker connectionChecker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_page);

        connectionChecker = new ConnectionChecker();
        firebaseAuth = FirebaseAuth.getInstance();
        fuser = firebaseAuth.getCurrentUser();
        changeFragment(new OpeningPageLogoFragment());



        /*
        try {
            if(connectionChecker.isConnected()){
                if (fuser != null) {
                    Intent i = new Intent(this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
                else{
                    Intent i = new Intent(this, LoginRegisterActivity.class);
                    startActivity(i);
                    finish();
                }
            }
            else{
                connectionChecker.showWindow(this);
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();

        }

         */
    }

    public void changeFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //fragmentTransaction.addToBackStack("ProfileCallback");
        fragmentTransaction.replace(R.id.openingPageContainer,fragment);
        fragmentTransaction.commit();
    }




}