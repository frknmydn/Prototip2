package com.furkanmeydan.prototip2.Views.OpeningActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
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
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class OpeningPageLogoFragment extends Fragment {
    ActivityOpeningPage activityOpeningPage;
    ConnectionChecker connectionChecker;
    ImageView imgLogo;
    TextView slogan;
    ProgressBar pg;
    boolean flag = false;
    boolean windowOpen = false;


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


        // Create an executor that executes tasks in the main thread.
        Executor mainExecutor = ContextCompat.getMainExecutor(activityOpeningPage);

        // Create an executor that executes tasks in a background thread.
        ScheduledExecutorService backgroundExecutor = Executors.newSingleThreadScheduledExecutor();

        backgroundExecutor.schedule(new Runnable() {
            @Override
            public void run() {
                while (!flag) {
                    try {
                        checkConnection();
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    // Update UI on the main thread
                    mainExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            // You code logic goes here.
                            if (flag) {
                                if (activityOpeningPage.fuser != null) {
                                    Intent i = new Intent(activityOpeningPage, MainActivity.class);
                                    startActivity(i);
                                    activityOpeningPage.finish();
                                    backgroundExecutor.shutdown();
                                } else {
                                    Intent i = new Intent(activityOpeningPage, LoginRegisterActivity.class);
                                    startActivity(i);
                                    activityOpeningPage.finish();
                                    backgroundExecutor.shutdown();
                                }

                            } else {
                                try {
                                    if(!windowOpen){
                                        connectionChecker.showWindow(activityOpeningPage);
                                        windowOpen = true;
                                    }

                                    Log.d("TAG", "flag is false: ");
                                } catch (IOException | InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });


                }
            }
        },2, TimeUnit.SECONDS);




    }

    public void checkConnection() throws IOException, InterruptedException {
        Log.d("TAG", "checkConnection: " + flag);
        if(!flag){
            flag = connectionChecker.isConnected();
        }

    }


}