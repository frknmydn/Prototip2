package com.furkanmeydan.prototip2.Views.MainActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment {

    User user;
    ProfileDAL profileDAL;
    private EditText edtNameSurname, edtEmail;
    private TextView gender, txtBirthday;
    private String userid;
    private Button btnUpdate;
    private ImageView imageView;
    private ConstraintLayout CLMain, CLProgress;
    private ProgressBar progressBar;
    private Uri imageData;
    private Bitmap selectedImage;


    MainActivity mainActivity;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        profileDAL = new ProfileDAL();



    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainActivity = (MainActivity) getActivity();

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CLMain = view.findViewById(R.id.CLFPMain);
        CLProgress = view.findViewById(R.id.CLFPProgress);
        progressBar = view.findViewById(R.id.progressBarPF);

        gender = view.findViewById(R.id.GenderFragment);
        txtBirthday = view.findViewById(R.id.BirthdayFragment);
        edtEmail = view.findViewById(R.id.EDTEmailFragment);
        edtNameSurname = view.findViewById(R.id.EDTNameSurnameFragment);
        imageView = view.findViewById(R.id.ImageViewFragment);
        btnUpdate = view.findViewById(R.id.btnUpdateFragment);

        if(getArguments() != null){
            Bundle bundle = getArguments();
            user = (User) bundle.getSerializable("user");
            getUserProfile(user);
            btnUpdate.setVisibility(View.GONE);
            imageView.setClickable(false);
            imageView.setFocusable(false);

        }else{
                getData();

            imageView.setOnClickListener(view1 -> updatePicture());

            btnUpdate.setOnClickListener(view12 -> updatePictureData());


        }



    }

    public void getData() {

        userid = Objects.requireNonNull(mainActivity.firebaseAuth.getCurrentUser()).getUid();

        profileDAL.getProfile(userid, new ProfileCallback() {
            @Override
            public void getUser(User user) {
                super.getUser(user);
                Log.d("Tag", "getUser çalıştı");
                Log.d("Tag","getUser "+ mainActivity.getOneSignalID());
                getUserProfile(user);
                /*
                txtBirthday.setText(user.getBirthDate());
                edtEmail.setText(user.getEmail());
                edtNameSurname.setText(user.getNameSurname());
                gender.setText(user.getGender());
                Glide.with(mainActivity.getApplicationContext()).load(user.getProfilePicture()).apply(RequestOptions.skipMemoryCacheOf(true))
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).into(imageView);

                CLProgress.setVisibility(View.INVISIBLE);
                CLMain.setVisibility(View.VISIBLE);
                *
                 */

            }
        });


    }

    private void getUserProfile(User user) {
        txtBirthday.setText(user.getBirthDate());
        edtEmail.setText(user.getEmail());
        edtNameSurname.setText(user.getNameSurname());
        gender.setText(user.getGender());
        Glide.with(mainActivity.getApplicationContext()).load(user.getProfilePicture()).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                CLProgress.setVisibility(View.INVISIBLE);
                CLMain.setVisibility(View.VISIBLE);
                return false;
            }
        }).apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).into(imageView);


    }


    public void updatePicture() {
        Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intentToGallery, 2);
    }

    public void updatePictureData() {
        final String imageName = "images/" + Objects.requireNonNull(mainActivity.firebaseAuth.getCurrentUser()).getUid() + "jpg";
        mainActivity.storageReference.child(imageName).putFile(imageData).addOnSuccessListener(taskSnapshot -> {
            StorageReference imgURLref = FirebaseStorage.getInstance().getReference(imageName);
            imgURLref.getDownloadUrl().addOnSuccessListener(uri -> Toast.makeText(mainActivity.getApplicationContext(), "Başarılı bir şekilde güncellendi", Toast.LENGTH_LONG).show());
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {


            imageData = data.getData();
            try {

                if (Build.VERSION.SDK_INT >= 28) {

                    ImageDecoder.Source source = ImageDecoder.createSource(mainActivity.getContentResolver(), imageData);
                    selectedImage = ImageDecoder.decodeBitmap(source);
                } else {

                    selectedImage = MediaStore.Images.Media.getBitmap(mainActivity.getContentResolver(), imageData);

                }
                imageView.setImageBitmap(selectedImage);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}