package com.furkanmeydan.prototip2.Views.MainActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.furkanmeydan.prototip2.DataLayer.Callbacks.AppDatabase;
import com.furkanmeydan.prototip2.DataLayer.Callbacks.CarCallback;
import com.furkanmeydan.prototip2.DataLayer.CarDAL;
import com.furkanmeydan.prototip2.Models.Car;
import com.furkanmeydan.prototip2.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Objects;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class FragmentMyCars extends Fragment {
    AutoCompleteTextView actvBrand, actvModel, actvYear, actvType;
    EditText edtColour, edtOptionalInfo;
    Button btnSave;
    ImageView imgViewCarPhoto;
    private Uri imageData;
    private Bitmap selectedImage;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    MainActivity mainActivity;

    ArrayAdapter<CharSequence> adapterVehicleType, adapterBrand, adapterModel, adapterYear;


    String typeString, brandString, modelString, yearString, colourString, optionalInfoString;
    UUID uuid;

    CarDAL carDAL;

    AppDatabase db;

    public FragmentMyCars() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        uuid = UUID.randomUUID();
        carDAL = new CarDAL();
        db = Room.databaseBuilder(mainActivity,
                AppDatabase.class, "carsDB").build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_cars, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //actwMyCarColour actwMyCarYear  actwMyCarBrand actwMyCarModel  actwMyCarOptionalInfo btnSaveCar

        actvBrand = view.findViewById(R.id.actwMyCarBrand);
        actvModel = view.findViewById(R.id.actwMyCarModel);
        actvYear = view.findViewById(R.id.actwMyCarYear);

        imgViewCarPhoto = view.findViewById(R.id.imageView4);
        edtColour = view.findViewById(R.id.actwMyCarColour);
        edtOptionalInfo = view.findViewById(R.id.actwMyCarOptionalInfo);
        btnSave = view.findViewById(R.id.btnSaveCar);



        adapterVehicleType = ArrayAdapter.createFromResource(view.getContext(), R.array.car_type, android.R.layout.simple_spinner_item);
        adapterVehicleType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        actvType = (AutoCompleteTextView) view.findViewById(R.id.actwMyCarType);
        actvType.setAdapter(adapterVehicleType);


        actvType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                typeString = (String) parent.getItemAtPosition(position);
                Log.d("Tag","type string: "+ typeString);

                if(typeString!=null && typeString.equals("Araba")){
                    Log.d("TAG", "araba olarak seçili: ");
                    adapterBrand = ArrayAdapter.createFromResource(view.getContext(),R.array.brand_array_car, android.R.layout.simple_spinner_item);
                    adapterBrand.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    actvBrand.setAdapter(adapterBrand);

                    actvBrand.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            brandString = (String) parent.getItemAtPosition(position);
                            Log.d("Tag","brand string asdasd: "+ brandString);


                            if(brandString!=null && brandString.equals("BMW")){
                                adapterModel = ArrayAdapter.createFromResource(view.getContext(),R.array.BMW_Models, android.R.layout.simple_spinner_item);
                                adapterModel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                actvModel.setAdapter(adapterModel);
                            }
                            else if(brandString!=null && brandString.equals("MERCEDES")){
                                adapterModel = ArrayAdapter.createFromResource(view.getContext(),R.array.MERCEDES_Models, android.R.layout.simple_spinner_item);
                                adapterModel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                actvModel.setAdapter(adapterModel);
                            }
                            else if(brandString!=null && brandString.equals("HONDA")){
                                adapterModel = ArrayAdapter.createFromResource(view.getContext(),R.array.HONDA_Models, android.R.layout.simple_spinner_item);
                                adapterModel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                actvModel.setAdapter(adapterModel);
                            }
                            else if(brandString!=null && brandString.equals("AUDI")){
                                adapterModel = ArrayAdapter.createFromResource(view.getContext(),R.array.AUDI_Models, android.R.layout.simple_spinner_item);
                                adapterModel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                actvModel.setAdapter(adapterModel);
                            }
                            actvModel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    modelString = (String) parent.getItemAtPosition(position);
                                    Log.d("TAG", "onItemClick: " + modelString);
                                }
                            });

                        }
                    });
                }
            }
        });


        adapterYear = ArrayAdapter.createFromResource(view.getContext(), R.array.model_year, android.R.layout.simple_spinner_item);
        adapterVehicleType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        actvYear.setAdapter(adapterYear);
        actvYear.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                yearString = (String) parent.getItemAtPosition(position);
            }
        });


        imgViewCarPhoto.setOnClickListener(view18 -> {
            if (ContextCompat.checkSelfPermission(view18.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                //Üstteki kod çalışmıyor
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            } else {
                Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentToGallery, 2);

            }

        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();

            }
        });



    }


    private void uploadImage() {
        final String imageName = "images/" + Objects.requireNonNull(mainActivity.firebaseAuth.getCurrentUser()).getUid() + uuid + "jpg";
        storageReference.child(imageName).putFile(imageData).addOnSuccessListener(taskSnapshot -> {
            StorageReference imgURLref = FirebaseStorage.getInstance().getReference(imageName);
            imgURLref.getDownloadUrl().addOnSuccessListener(uri -> {
                String imageURL = uri.toString();
                uploadData(imageURL);

            });
        });
    }

    private void uploadData(final String imageURL){
        colourString = edtColour.getText().toString();
        optionalInfoString = edtOptionalInfo.getText().toString();
        int intLastYear = Integer.parseInt(yearString);

        carDAL.uploadCar(1, brandString, modelString, colourString, typeString, optionalInfoString, intLastYear, imageURL,mainActivity ,new CarCallback() {
            @Override
            public void uploadCar(Car car) {
                super.uploadCar(car);
                Toast.makeText(mainActivity, "Araç Eklendi",Toast.LENGTH_LONG).show();
                Intent i = new Intent(mainActivity, MainActivity.class);
                startActivity(i);
                mainActivity.finish();

            }
        });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
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
                imgViewCarPhoto.setImageBitmap(selectedImage);


            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }
}