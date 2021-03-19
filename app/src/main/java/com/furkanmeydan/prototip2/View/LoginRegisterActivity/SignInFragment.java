package com.furkanmeydan.prototip2.View.LoginRegisterActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.furkanmeydan.prototip2.R;
import com.furkanmeydan.prototip2.View.LoginRegisterActivity.LoginRegisterActivity;
import com.furkanmeydan.prototip2.View.MainActivity.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class SignInFragment extends Fragment {
    EditText edtPhone, edtPassword;
    Button btnsignIn, btnsignUp;
    String stringPhone, stringPassword;
    LoginRegisterActivity loginRegisterActivity;
    FirebaseAuth firebaseAuth;
    FirebaseUser fuser;
    Dialog dialog;
    ProgressBar signinPB;
    static final long COUNTDOWN_IN_MILLIS = 60000;
    CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    TextView txtTimer;


    public SignInFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        //loginregister activity'nin içinde oluşturulan firebaseAuth variableına erişim sağlanabiliyor ve aktivite oluşturulabiliyor.


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        firebaseAuth = FirebaseAuth.getInstance();
        fuser = firebaseAuth.getCurrentUser();
        if (fuser != null) {
            Intent i = new Intent(getActivity(), MainActivity.class);
            startActivity(i);

        }

        loginRegisterActivity = (LoginRegisterActivity) getActivity();
        dialog = new Dialog(loginRegisterActivity);
        dialog.setContentView(R.layout.popup_verification);

        if (fuser != null) {
            Intent i = new Intent(getActivity(), MainActivity.class);
            startActivity(i);
            getActivity().finish();
        }

        edtPhone = view.findViewById(R.id.signInEDTPhone);

        btnsignIn = view.findViewById(R.id.btnSignIn);
        signinPB = view.findViewById(R.id.signinPB);


        btnsignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn(view);
            }
        });


    }

    public void signIn(final View view) {


        stringPhone = edtPhone.getText().toString();
        if (stringPhone.isEmpty()) {
            Toast.makeText(loginRegisterActivity.getApplicationContext(), "Telefon numaranızı girin", Toast.LENGTH_LONG).show();
        } else {
            signinPB.setVisibility(View.VISIBLE);
            PhoneAuthOptions options = PhoneAuthOptions.newBuilder(loginRegisterActivity.firebaseAuth)
                    .setPhoneNumber("+90" + stringPhone)
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setActivity(loginRegisterActivity)
                    .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        @Override
                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                            signinUser(view, phoneAuthCredential);
                        }

                        @Override
                        public void onVerificationFailed(@NonNull FirebaseException e) {

                        }

                        @Override
                        public void onCodeSent(@NonNull final String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                            super.onCodeSent(s, forceResendingToken);

                            //Popup

                            txtTimer = dialog.findViewById(R.id.txtPopTime);
                            final EditText edtVerify = dialog.findViewById(R.id.edtpopVerify);
                            Button edtButton = dialog.findViewById(R.id.edtpopButton);

                            edtButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String verificationCode = edtVerify.getText().toString();
                                    if (s.isEmpty()) {
                                        return;
                                    }
                                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(s, verificationCode);
                                    signinUser(view, credential);
                                }
                            });
                            signinPB.setVisibility(View.INVISIBLE);
                            dialog.show();

                            //geri sayım işlemleri.
                            timeLeftInMillis = COUNTDOWN_IN_MILLIS;
                            startCountDown();
                        }
                    })
                    .build();

            PhoneAuthProvider.verifyPhoneNumber(options);
        }

    }

    private void startCountDown() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMillis = l;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                if (dialog.isShowing()) {
                    timeLeftInMillis = 0;
                    dialog.dismiss();
                }


            }
        }.start();
    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeForMatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        txtTimer.setText(timeForMatted);

    }

    public void signinUser(final View view, PhoneAuthCredential credential) {
        loginRegisterActivity.firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {


                    Intent i = new Intent(loginRegisterActivity, MainActivity.class);
                    dialog.dismiss();
                    startActivity(i);
                    loginRegisterActivity.finish();

                } else {
                    Toast.makeText(loginRegisterActivity.getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}