package com.furkanmeydan.prototip2.Views.OpeningActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.furkanmeydan.prototip2.Models.ConnectionChecker;
import com.furkanmeydan.prototip2.R;
import com.furkanmeydan.prototip2.Views.LoginRegisterActivity.LoginRegisterActivity;
import com.furkanmeydan.prototip2.Views.MainActivity.MainActivity;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;


public class OpeningPageLogoFragment extends Fragment {
    ActivityOpeningPage activityOpeningPage;
    ConnectionChecker connectionChecker;
    ImageView imgLogo;
    TextView slogan;
    ProgressBar pg;


    public OpeningPageLogoFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityOpeningPage = (ActivityOpeningPage) getActivity();
        connectionChecker = new ConnectionChecker();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_opening_page_logo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imgLogo = view.findViewById(R.id.imageView8);
        slogan = view.findViewById(R.id.textView69);
        pg = view.findViewById(R.id.progressBar8);




    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            Thread.sleep(5500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            if(connectionChecker.isConnected()){
                if (activityOpeningPage.fuser != null) {
                    Intent i = new Intent(activityOpeningPage, MainActivity.class);
                    startActivity(i);
                    activityOpeningPage.finish();
                }
                else{
                    Intent i = new Intent(activityOpeningPage, LoginRegisterActivity.class);
                    startActivity(i);
                    activityOpeningPage.finish();
                }
            }
            else{
                connectionChecker.showWindow(activityOpeningPage);
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();

        }
    }
}