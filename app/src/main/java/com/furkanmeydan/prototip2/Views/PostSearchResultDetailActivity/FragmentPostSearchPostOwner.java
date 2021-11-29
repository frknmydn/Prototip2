package com.furkanmeydan.prototip2.Views.PostSearchResultDetailActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.furkanmeydan.prototip2.DataLayer.Callbacks.ProfileCallback;
import com.furkanmeydan.prototip2.DataLayer.ProfileDAL;
import com.furkanmeydan.prototip2.Models.User;
import com.furkanmeydan.prototip2.R;


public class FragmentPostSearchPostOwner extends Fragment {

    ProfileDAL profileDAL;
    PostSearchResultDetailActivity activity;
    Bundle bundle;
    private TextView txtNameSurname, txtGender, txtBirthday;
    private ImageView imageView;
    ConstraintLayout layoutDetail, layoutProgress;
    private ProgressBar progressBar;
    String ownerId;
    User userProfile;
    Button nonShow;


    public FragmentPostSearchPostOwner() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (PostSearchResultDetailActivity) getActivity();
        //ownerId = activity.post.get
        ownerId= activity.post.getOwnerID();
        profileDAL = new ProfileDAL();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_search_post_owner, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtNameSurname = view.findViewById(R.id.postSearchPostOwnerName);
        txtGender = view.findViewById(R.id.postSearchPostOwnerGender);
        txtBirthday = view.findViewById(R.id.postSearchPostOwnerBD);
        imageView = view.findViewById(R.id.postSearchPostOwnerImageView);
        layoutDetail = view.findViewById(R.id.searchProfileConsLayoutContent);
        layoutProgress = view.findViewById(R.id.searchProfileConsLayout);
        progressBar = view.findViewById(R.id.searchResultProgress);
        nonShow = view.findViewById(R.id.btnBlockUserProfile);
        nonShow.setVisibility(View.INVISIBLE);
        Log.d("Tag","PostSearchactivity owner id "+ ownerId);
        Log.d("Tag","PostSearchactivity getprofiledata öncesi ");
        getProfileData();



    }

    public void getProfileData(){
        Log.d("Tag","PostSearchactivity getProfiledata içerisi");

        profileDAL.getProfile(ownerId, new ProfileCallback() {
            @Override
            public void getUser(User user) {
                super.getUser(user);
                Log.d("Tag","PostSearchactivity getUser içerisi");
                userProfile = user;
                txtNameSurname.setText(userProfile.getNameSurname());
                txtBirthday.setText(userProfile.getBirthDate());
                txtGender.setText(userProfile.getGender());
                Glide.with(activity.getApplicationContext()).load(userProfile.getProfilePicture()).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        layoutProgress.setVisibility(View.GONE);
                        layoutDetail.setVisibility(View.VISIBLE);
                        return false;
                    }
                }).apply(RequestOptions.skipMemoryCacheOf(true))
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).into(imageView);



            }
        });
    }
}