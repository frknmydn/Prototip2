package com.furkanmeydan.prototip2.Views.MainActivity;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.AsyncTask;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class FragmentMyCars extends Fragment {
    AutoCompleteTextView actvBrand, actvModel, actvYear, actvType;
    EditText edtColour, edtOptionalInfo;
    Button btnSave;
    ImageView imgViewCarPhoto;
    private Uri imageData;
    private StorageReference storageReference;
    MainActivity mainActivity;
    int cnt;
    int intLastYear;

    ArrayAdapter<CharSequence> adapterVehicleType, adapterBrand, adapterModel, adapterYear;


    String typeString, brandString, modelString, yearString, colourString, optionalInfoString;
    UUID uuid;

    CarDAL carDAL;

    AppDatabase db;

    File file;

    public FragmentMyCars() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        uuid = UUID.randomUUID();
        carDAL = new CarDAL();
        db = Room.databaseBuilder(mainActivity,
                AppDatabase.class, "carsDB").build();

        //çalışıyor, artık kullanıcıların db'de kayıt ettiği arabayı da tutabiliyoruz.
        AsyncTask.execute(() -> {
            List<Car> cars = db.carDao().getAllCars();
            Log.d("TAGGOY", "run: cars" + cars.size());
        });
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


        actvType.setOnItemClickListener((parent, view1, position, id) -> {
            typeString = (String) parent.getItemAtPosition(position);
            Log.d("Tag","type string: "+ typeString);

            if(typeString!=null && typeString.equals("Araba")){
                Log.d("TAG", "araba olarak seçili: ");
                adapterBrand = ArrayAdapter.createFromResource(view1.getContext(),R.array.brand_array_car, android.R.layout.simple_spinner_item);
                adapterBrand.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                actvBrand.setAdapter(adapterBrand);

                actvBrand.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view1, int position, long id) {
                        brandString = (String) parent.getItemAtPosition(position);
                        Log.d("Tag","brand string asdasd: "+ brandString);


                        if(brandString!=null && brandString.equals("BMW")){
                            adapterModel = ArrayAdapter.createFromResource(view1.getContext(),R.array.BMW_Models, android.R.layout.simple_spinner_item);
                            adapterModel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            actvModel.setAdapter(adapterModel);
                        }
                        else if(brandString!=null && brandString.equals("MERCEDES")){
                            adapterModel = ArrayAdapter.createFromResource(view1.getContext(),R.array.MERCEDES_Models, android.R.layout.simple_spinner_item);
                            adapterModel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            actvModel.setAdapter(adapterModel);
                        }
                        else if(brandString!=null && brandString.equals("HONDA")){
                            adapterModel = ArrayAdapter.createFromResource(view1.getContext(),R.array.HONDA_Models, android.R.layout.simple_spinner_item);
                            adapterModel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            actvModel.setAdapter(adapterModel);
                        }
                        else if(brandString!=null && brandString.equals("AUDI")){
                            adapterModel = ArrayAdapter.createFromResource(view1.getContext(),R.array.AUDI_Models, android.R.layout.simple_spinner_item);
                            adapterModel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            actvModel.setAdapter(adapterModel);
                        }
                        actvModel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view1, int position, long id) {
                                modelString = (String) parent.getItemAtPosition(position);
                                Log.d("TAG", "onItemClick: " + modelString);
                            }
                        });

                    }
                });
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
                cnt =1;

            }

        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    uploadImage();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });



    }


    private void uploadImage() throws IOException {
        if(cnt==1) {

            final String imageName = "images/" + Objects.requireNonNull(mainActivity.firebaseAuth.getCurrentUser()).getUid() + uuid + "jpg";

            storageReference.child(imageName).putFile(imageData).addOnSuccessListener(taskSnapshot -> {
                StorageReference imgURLref = FirebaseStorage.getInstance().getReference(imageName);
                imgURLref.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageURL = uri.toString();
                    uploadData(imageURL);

                });
            });



        }
    }

    private void uploadData(final String imageURL){
        colourString = edtColour.getText().toString();
        optionalInfoString = edtOptionalInfo.getText().toString();
        if(yearString!=null){
            intLastYear = Integer.parseInt(yearString);
        }
        else
            Toast.makeText(mainActivity,"Lütfen model yılı seçiniz",Toast.LENGTH_LONG).show();


        carDAL.uploadCar(brandString, modelString, colourString, typeString
                , optionalInfoString, intLastYear, imageURL,mainActivity ,new CarCallback() {
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
            Double originalWidth, originalHeight, ratio;

            try {

                Bitmap selectedImage;

                if (Build.VERSION.SDK_INT >= 28) {

                    ImageDecoder.Source source = ImageDecoder.createSource(mainActivity.getContentResolver(), imageData);
                    selectedImage = ImageDecoder.decodeBitmap(source);

                    selectedImage = resize(selectedImage,1024,720);

                    imageData = getImageUri(mainActivity,selectedImage);



                } else {

                    selectedImage = MediaStore.Images.Media.getBitmap(mainActivity.getContentResolver(), imageData);

                    selectedImage = Bitmap.createScaledBitmap(selectedImage,300,300,false);
                    imageData = getImageUri(mainActivity,selectedImage);

                }
                imgViewCarPhoto.setImageBitmap(selectedImage);
                cnt=1;


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
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