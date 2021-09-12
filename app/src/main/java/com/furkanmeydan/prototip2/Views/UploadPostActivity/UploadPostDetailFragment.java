package com.furkanmeydan.prototip2.Views.UploadPostActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.furkanmeydan.prototip2.Adapters.MyCarsAdapter;
import com.furkanmeydan.prototip2.DataLayer.Callbacks.AppDatabase;
import com.furkanmeydan.prototip2.DataLayer.CarDAL;
import com.furkanmeydan.prototip2.DataLayer.LocalDataManager;
import com.furkanmeydan.prototip2.DataLayer.PostDetailDataPasser;
import com.furkanmeydan.prototip2.Models.Car;
import com.furkanmeydan.prototip2.R;
import com.furkanmeydan.prototip2.Views.MainActivity.MainActivity;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class UploadPostDetailFragment extends Fragment {

    EditText edtDestination,edtDescription,edtTime,edtDate;

    AutoCompleteTextView spinnerCity, spinnerPassengerCount;


    TextInputLayout timeLayout;
    ArrayAdapter<CharSequence> spinnerAdapterCity;
    ArrayAdapter<Integer> spinnerAdapterPassanger;
    UploadPostActivity postActivity;
    Drawable drawable;

    DatePickerDialog datePickerDialog;
    long timeNow = Timestamp.now().getSeconds() * 1000;
    long timeMaxDifference = (14 * 24 * 60 * 60 * 1000);

    Integer[] items = new Integer[]{1,2,3,4,5};


    //SharedPreference classı
    LocalDataManager localDataManager;

    PostDetailDataPasser dataPasser;
    String cityString,timeString,dateCombined;
    Integer passengerCountString = -1;
    //Tarih ile ilgili
    private String dateString = "";
    private SimpleDateFormat dateFormat,dateTimeFormat,dateCombinedFormat;
    private Calendar calendar;

    Date dateTimeStamp;
    Timestamp timeStamp;
    //Saat ile ilgili
    TimePickerDialog timePickerDialog;

    TextInputLayout txtinputlay;



    public UploadPostDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        postActivity = (UploadPostActivity) getActivity();


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Doğum tarihi seçmek için telefonun takvim uygulamasını açmak için
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeFormat = new SimpleDateFormat("HH:mm");
        dateCombinedFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        calendar = Calendar.getInstance();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upload_post_detail_new, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        localDataManager = new LocalDataManager();

        postActivity.layoutCar = view.findViewById(R.id.layoutCar);

        edtDestination = view.findViewById(R.id.UPDEDTDestination1);
        edtDescription = view.findViewById(R.id.UPDEDTDescription);
        timeLayout = view.findViewById(R.id.UPDTimeLayout);
        edtTime = view.findViewById(R.id.UPDTime);
        edtDate = view.findViewById(R.id.UPDDate);
        postActivity.layoutCar = view.findViewById(R.id.layoutCar);
        postActivity.txtCarInfo = view.findViewById(R.id.txtCarInfo);

        txtinputlay = view.findViewById(R.id.UPDEDTDestinationLayout);

        postActivity.layoutCar.setOnClickListener(v -> {


            //Popup içi adapter için
            ArrayList<Car> carList = new ArrayList<>();
            CarDAL carDAL = new CarDAL();
            AppDatabase appDatabase = Room.databaseBuilder(postActivity,
                    AppDatabase.class, "carsDB").build();
            String userid = postActivity.firebaseAuth.getUid();
            RecyclerView rclMyCars = postActivity.popupView.findViewById(R.id.rclSelectCar);
            rclMyCars.setLayoutManager(new LinearLayoutManager(getContext()));
            MyCarsAdapter adapter = new MyCarsAdapter(carList,postActivity);

            rclMyCars.setAdapter(adapter);


            Thread t1 = new Thread() {
                @Override
                public void run() {
                    if(carList.size()<=0){
                        carList.addAll(appDatabase.carDao().loadAllCarsByUserID(userid));
                        adapter.notifyDataSetChanged();
                    }
                }
            };
            t1.start();
            try {
                t1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            postActivity.carPopup.showAtLocation(v,Gravity.CENTER, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            Log.d("TAG", "CarListSize: "+ String.valueOf(carList.size()));

        });

        edtDate.setHint("Tarih Seçiniz");
        edtTime.setHint("Saat Seçiniz");
        timeLayout.setVisibility(View.INVISIBLE);




        //***********UPDATE*****************

        spinnerAdapterCity = ArrayAdapter.createFromResource(view.getContext(), R.array.city_array, android.R.layout.simple_spinner_item);
        spinnerAdapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity = (AutoCompleteTextView) view.findViewById(R.id.atxtCity);
        spinnerCity.setAdapter(spinnerAdapterCity);

        spinnerCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cityString = (String) parent.getItemAtPosition(position);
                Log.d("Tag","cityString: "+ cityString);
            }
        });

        //***********/UPDATE****************


        //spinner passenger count. bu classın içinde adapterı yazılı
        spinnerPassengerCount =view.findViewById(R.id.atxtPassengerCount);
        spinnerAdapterPassanger = new ArrayAdapter<Integer>(postActivity.getApplicationContext(),android.R.layout.simple_spinner_item,items);
        spinnerAdapterPassanger.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPassengerCount.setAdapter(spinnerAdapterPassanger);

        spinnerPassengerCount.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                passengerCountString = (Integer) parent.getItemAtPosition(position);
            }
        });




        //İlan tarihi seçtirmek
        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);


                //dateString = String.format("%02d/%02d/%04d", i2, i1 + 1, i);
                dateString = dateFormat.format(calendar.getTime());
                dateCombined = dateString;
                edtDate.setText(dateString);
                timeLayout.setVisibility(View.VISIBLE);
                edtTime.setVisibility(View.VISIBLE);
                edtTime.setHint("");



            }
        };
        datePickerDialog = new DatePickerDialog(postActivity,dateSetListener,calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(timeNow);
        datePickerDialog.getDatePicker().setMaxDate(timeNow + timeMaxDifference);


        edtDate.setOnClickListener(view1 -> datePickerDialog.show());

        /*
        edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(view.getContext(), dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                datePickerDialog.getDatePicker().setMinDate(timeNow);
                datePickerDialog.getDatePicker().setMaxDate(timeNow + timeMaxDifference);

            }
        });
        */


        //Saat seçmek için
        edtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                int hour =  calendar.get(Calendar.HOUR_OF_DAY);
                int minutes = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        //String pickerString = String.format("%02d:%02d", i , i1);

                        calendar.set(Calendar.HOUR_OF_DAY,i);
                        calendar.set(Calendar.MINUTE,i1);

                        timeString = dateTimeFormat.format(calendar.getTime());


                        edtTime.setText(timeString);
                        Log.d("Tag",timeString);

                        dateCombined = dateString;
                        dateCombined = dateCombined + " " + timeString;
                        Log.d("Tag",dateCombined);


                        //timestamp oluşturmak için


                        try {
                            dateTimeStamp = dateCombinedFormat.parse(dateCombined);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        calendar.setTime(dateTimeStamp);
                        timeStamp = new Timestamp(dateTimeStamp);
                        Date date = timeStamp.toDate();
                        dateCombinedFormat.format(date);
                        Log.d("Tag",timeStamp.toString());
                        Log.d("Tag",dateTimeStamp.toString());
                        Log.d("Tag",dateCombinedFormat.format(date));

                    }
                },hour,minutes,true);

                timePickerDialog.show();
            }

        });




    }

    public void saveDetails(){

       if(timeStamp !=null){
           localDataManager.setSharedPreferenceForLong(postActivity,"timestamp",timeStamp.getSeconds());
           /////
           postActivity.postToPush.setTimestamp(timeStamp.getSeconds());
           if(!dateString.equals("")){
               localDataManager.setSharedPreference(postActivity,"date",dateString);
           }
           if(!timeString.equals("")){
               localDataManager.setSharedPreference(postActivity,"time",timeString);
           }
       }
       if(cityString !=null){
           ////
           postActivity.postToPush.setCity(cityString);
       }
       if(passengerCountString != -1){
           ////
           postActivity.postToPush.setPassengerCount(passengerCountString);
       }
        if(!edtDescription.getText().toString().equals("")){
            ////
            postActivity.postToPush.setDescription(edtDescription.getText().toString());
        }


       if(!edtDestination.getText().toString().equals("")){
           ////
           postActivity.postToPush.setDestination(edtDestination.getText().toString());
       }


    }

    @Override
    public void onResume() {
        super.onResume();

        // eğer data var ise edittextler içinde görünmesi için

        String time = (localDataManager.getSharedPreference(postActivity,"time",null));
        String date = (localDataManager.getSharedPreference(postActivity,"date",null));

        cityString = postActivity.postToPush.getCity();
        Log.d("TAG", "onResume: "+ cityString);

        passengerCountString = postActivity.postToPush.getPassengerCount();
        Log.d("TAG", "onResume: " + passengerCountString);
        String description = postActivity.postToPush.getDescription();
        String destination = postActivity.postToPush.getDestination();

        //saat seçildiyse geri geldiğinde seçilmiş saatin timetext'inde görünmesi için
       if(date!=null){
            if(time!=null){


                String dateTime = date + " " + time;
                edtTime.setVisibility(View.INVISIBLE);
                timeLayout.setVisibility(View.INVISIBLE);
                edtDate.setText(dateTime);
                Log.d("TAG", "onResume: xd "+ date + time);
            }


        }

        edtDestination.setText(destination);
        edtDescription.setText(description);
        if(cityString!=null && passengerCountString!=null){
            spinnerCity.setText(cityString,false);
            spinnerAdapterCity.getFilter().filter(null);
            spinnerPassengerCount.setText(String.valueOf(passengerCountString),false);
            spinnerAdapterPassanger.getFilter().filter(null);
        }
        if(postActivity.car != null){
            postActivity.txtCarInfo.setText(postActivity.car.getBrand() + " " + postActivity.car.getModel());

            drawable = AppCompatResources.getDrawable(postActivity, R.drawable.car_selected);
            postActivity.layoutCar.setBackground(drawable);


        }


    }
}