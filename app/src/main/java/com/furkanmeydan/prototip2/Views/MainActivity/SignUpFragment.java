package com.furkanmeydan.prototip2.Views.MainActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.furkanmeydan.prototip2.DataLayer.Callbacks.ProfileCallback;
import com.furkanmeydan.prototip2.DataLayer.LocalDataManager;
import com.furkanmeydan.prototip2.DataLayer.ProfileDAL;
import com.furkanmeydan.prototip2.Models.ConnectionChecker;
import com.furkanmeydan.prototip2.Models.User;
import com.furkanmeydan.prototip2.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.onesignal.OneSignal;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;


public class SignUpFragment extends Fragment {

    LocalDataManager localDataManager;
    LocalDataManager localDataManagerUser;

    ProfileDAL profileDAL;

    public MainActivity mainActivity;

    private EditText edtNameSurname, edtEmail, edtBirthday;
    private Spinner genderSpinner;
    private String genderString, nameSurnameString, eMailString;
    private Button btnRegister;
    private ImageView imageView;

    //Doğum tarihi ile ilgili
    private String dateString = "";
    private SimpleDateFormat dateFormat;
    private Calendar calendar;

    ArrayAdapter<CharSequence> spinnerAdapter;


    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;



    Uri imageData;
    Bitmap selectedImage;
    ConnectionChecker conChecker;


    public SignUpFragment() {
        // Required empty public constructor
    }


    @SuppressLint("SimpleDateFormat")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        localDataManager = new LocalDataManager();
        localDataManagerUser = new LocalDataManager();
        profileDAL = new ProfileDAL();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();



        //Doğum tarihi seçmek için telefonun takvim uygulamasını açmak için
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        calendar = Calendar.getInstance();
        conChecker = new ConnectionChecker();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_sign_up, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mainActivity = (MainActivity) getActivity();


        edtNameSurname = view.findViewById(R.id.signUpEDTNameSurname);
        edtEmail = view.findViewById(R.id.signUpEDTEmail);
        edtBirthday = view.findViewById(R.id.signUpBirthday);
        btnRegister = view.findViewById(R.id.btncreateProfile);
        imageView = view.findViewById(R.id.signUpImageView);


        //Doğum tarihi seçtirmek
        final DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, i, i1, i2) -> {

            calendar.set(Calendar.YEAR, i);
            calendar.set(Calendar.MONTH, i1 + 1);
            calendar.set(Calendar.DAY_OF_MONTH, i2);

            dateString = String.format("%02d/%02d/%04d", i2, i1 + 1, i);
            edtBirthday.setText(dateString);
//

        };
        edtBirthday.setOnClickListener(view1 -> new DatePickerDialog(view1.getContext(), dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show());


        //spinner
        genderSpinner = view.findViewById(R.id.signUpGender);
        spinnerAdapter = ArrayAdapter.createFromResource(view.getContext(), R.array.genders_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(spinnerAdapter);




        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                genderString = adapterView.getItemAtPosition(i).toString();
                
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }


        });


        //Galeriden fotoğraf seçme işlemleri
        imageView.setOnClickListener(view12 -> {
            if (ContextCompat.checkSelfPermission(view12.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                //Üstteki kod çalışmıyor
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            } else {
                Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentToGallery, 2);

            }

        });


        btnRegister.setOnClickListener(view13 -> {
            nameSurnameString = edtNameSurname.getText().toString();
            eMailString = edtEmail.getText().toString();


            if (imageData != null) {
                if(!genderString.equals("Cinsiyet")){
                    try {
                        uploadImage();
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(getActivity(), "Lütfen Cinsiyetinizi Belirtiniz.", Toast.LENGTH_LONG).show();
                }

            } else
                Toast.makeText(getActivity(), "Lütfen Profil Fotoğrafı Seçin", Toast.LENGTH_LONG).show();

        });


    }


    private void uploadImage() throws IOException, InterruptedException {
        if(conChecker.isConnected()){
            final String imageName = "images/" + Objects.requireNonNull(mainActivity.firebaseAuth.getCurrentUser()).getUid() + "jpg";
            storageReference.child(imageName).putFile(imageData).addOnSuccessListener(taskSnapshot -> {
                StorageReference imgURLref = FirebaseStorage.getInstance().getReference(imageName);
                imgURLref.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageURL = uri.toString();
                    uploadData(imageURL);
                });
            });
        }
        else{
            conChecker.showWindow(mainActivity);
        }

    }


    private void uploadData(final String imageURL) {

        try {
            if(conChecker.isConnected()){
                profileDAL.uploadProfile(nameSurnameString, eMailString, dateString, genderString, imageURL, getActivity(), new ProfileCallback() {

                    @Override
                    public void uploadProfile(User user) {
                        super.uploadProfile(user);

                        localDataManagerUser.setSharedPreference(mainActivity, "sharedNameSurname", user.getNameSurname());
                        localDataManagerUser.setSharedPreference(mainActivity, "sharedEmail", user.getEmail());
                        localDataManagerUser.setSharedPreference(mainActivity, "sharedBirthdate", user.getBirthDate());
                        localDataManagerUser.setSharedPreference(mainActivity, "sharedGender", user.getGender());
                        localDataManagerUser.setSharedPreference(mainActivity, "sharedImageURL", user.getProfilePicture());
                        localDataManagerUser.setSharedPreference(mainActivity, "sharedOneSignalID", user.getOneSignalID());



                        Intent i = new Intent(mainActivity, MainActivity.class);
                        startActivity(i);
                        mainActivity.finish();
                    }
                });
            }
            else{
                conChecker.showWindow(mainActivity);
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }


    }
    // İlk defa galeriyi açınca izin isteme muhabbeti
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                System.out.println("onRequestPermissionsResult çalışıyor");
                Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentToGallery, 2);
            }
        } else {
            System.out.println("onRequestPermissionsResult çalışıyor ama requestcode 1 değil");
        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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

                    selectedImage = resize(selectedImage,1920,1080);

                } else {

                    selectedImage = MediaStore.Images.Media.getBitmap(mainActivity.getContentResolver(), imageData);

                    selectedImage = resize(selectedImage,1920,1080);

                }
                imageView.setImageBitmap(selectedImage);


            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    private  Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > ratioBitmap) {
                finalWidth = (int) ((float)maxHeight * ratioBitmap);
            }
            else {
                finalHeight = (int) ((float)maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }

}