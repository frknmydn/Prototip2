package com.furkanmeydan.prototip2.Views.MainActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.furkanmeydan.prototip2.DataLayer.ProfileCallback;
import com.furkanmeydan.prototip2.DataLayer.ProfileDAL;
import com.furkanmeydan.prototip2.Models.User;
import com.furkanmeydan.prototip2.R;
import com.furkanmeydan.prototip2.Views.PostActivity.PostActivity;
import com.furkanmeydan.prototip2.Views.UploadPostActivity.UploadPostActivity;

import java.util.Objects;


public class HomeFragment extends Fragment {


    private Button btnAddPost, btnSearchPost;
    MainActivity mainActivity;

    Dialog dialog;

    String userid;

    ProfileDAL profileDAL;

    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        profileDAL = new ProfileDAL();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mainActivity = (MainActivity) getActivity();
        assert mainActivity != null;
        userid = Objects.requireNonNull(mainActivity.firebaseAuth.getCurrentUser()).getUid();
        Log.d("TAG", "userid " + userid);
        btnAddPost = view.findViewById(R.id.btnAddPostFragmentH);
        btnSearchPost = view.findViewById(R.id.btnSearchPost);

        btnAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Log.d("Tag", "Onclick'e basıldı");
                profileDAL.getProfile(userid, new ProfileCallback() {

                    @Override
                    public void getUser(User user) {
                        super.getUser(user);

                        Intent i = new Intent(mainActivity, UploadPostActivity.class);
                        startActivity(i);

                    }

                    @Override
                    public void nullUser() {
                        Log.d("Tag", "nullUser metodu çalıştı");
                        dialog = new Dialog(mainActivity);
                        dialog.setContentView(R.layout.popup_fill_profile);
                        dialog.show();
                        Button btnProfileDialog = dialog.findViewById(R.id.btnPopupGoToProfile);
                        btnProfileDialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mainActivity.changeFragment(new SignUpFragment());
                                dialog.dismiss();
                            }
                        });
                    }

                });


            }
        });


        // Search Sayfasına gitme
        btnSearchPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), PostActivity.class);
                startActivity(i);
            }
        });
    }
}