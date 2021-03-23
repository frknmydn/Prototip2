package com.furkanmeydan.prototip2.View.PostSearchResultDetailActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.furkanmeydan.prototip2.DataLayer.ProfileCallback;
import com.furkanmeydan.prototip2.DataLayer.ProfileDAL;
import com.furkanmeydan.prototip2.Model.User;
import com.furkanmeydan.prototip2.R;


public class FragmentPostSearchPostOwner extends Fragment {

    ProfileDAL profileDAL;
    PostSearchResultDetailActivity activity;
    Bundle bundle;
    private TextView txtNameSurname, txtGender, txtBirthday;
    private ImageView imageView;
    String ownerId;
    User userProfile;


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
                Glide.with(activity.getApplicationContext()).load(userProfile.getProfilePicture()).apply(RequestOptions.skipMemoryCacheOf(true))
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).into(imageView);


            }
        });
    }
}