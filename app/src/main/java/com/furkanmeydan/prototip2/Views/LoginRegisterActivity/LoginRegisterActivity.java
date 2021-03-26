package com.furkanmeydan.prototip2.Views.LoginRegisterActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.furkanmeydan.prototip2.R;
import com.google.firebase.auth.FirebaseAuth;

public class LoginRegisterActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        firebaseAuth=FirebaseAuth.getInstance();

    }
}