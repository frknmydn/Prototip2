package com.furkanmeydan.prototip2.Views.MainActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.furkanmeydan.prototip2.Models.Car;
import com.furkanmeydan.prototip2.Models.Request;
import com.furkanmeydan.prototip2.R;

import org.jetbrains.annotations.NotNull;


public class FragmentSingleCar extends Fragment {

    Car car;
    TextView carType, carBrand, carModel, carYear, carColour, optionalInfo;
    ImageView carImage;
    Button btnDeleteCar;
    MainActivity activity;

    public FragmentSingleCar() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (MainActivity) getActivity();
        if(getArguments()!=null){
            car = (Car) getArguments().getSerializable("car");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_single_car, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        carType = view.findViewById(R.id.actwMyCarType1);
        carBrand = view.findViewById(R.id.actwMyCarBrand1);;
        carModel = view.findViewById(R.id.actwMyCarModel1); ;
        carColour = view.findViewById(R.id.actwMyCarColour1);
        carYear = view.findViewById(R.id.actwMyCarYear1);
        optionalInfo = view.findViewById(R.id.actwMyCarOptionalInfo1);;
        carImage = view.findViewById(R.id.imageView41);
        btnDeleteCar = view.findViewById(R.id.buttondeleteCar);

        Glide.with(activity.getApplicationContext()).load(car.getPicURL()).apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).into(carImage);
        carType.setText(car.getType());
        carBrand.setText(car.getBrand());
        carModel.setText(car.getModel());
        String carYearString = String.valueOf(car.getYear());
        carYear.setText(carYearString);
        carColour.setText(car.getColor());
        if(car.getOptionalInfo()!=null && !car.getOptionalInfo().equals("")){
            optionalInfo.setText(car.getOptionalInfo());
        }


    }
}