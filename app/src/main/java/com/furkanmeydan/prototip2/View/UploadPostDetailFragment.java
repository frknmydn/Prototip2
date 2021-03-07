package com.furkanmeydan.prototip2.View;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.furkanmeydan.prototip2.DataLayer.LocalDataManager;
import com.furkanmeydan.prototip2.DataLayer.PostDetailDataPasser;
import com.furkanmeydan.prototip2.R;
import com.google.firebase.Timestamp;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class UploadPostDetailFragment extends Fragment {

    EditText edtcarDetail,edtDestination,edtDescription,edtTime,edtDate;
    Spinner spinnerCity,spinnerPassengerCount;
    ArrayAdapter<CharSequence> spinnerAdapterCity;
    ArrayAdapter<Integer> spinnerAdapterPassanger;
    UploadPostActivity postActivity;
    String cardet;

    Integer[] items = new Integer[]{1,2,3,4,5};


    //SharedPreference classı
    LocalDataManager localDataManager;

    PostDetailDataPasser dataPasser;
    String cityString,timeString,dateCombined;
    int passengerCountString = -1;
    //Tarih ile ilgili
    private String dateString = "";
    private SimpleDateFormat dateFormat,dateTimeFormat,dateCombinedFormat;
    private Calendar calendar;

    Date dateTimeStamp;
    Timestamp timeStamp;
    //Saat ile ilgili
    TimePickerDialog timePickerDialog;



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
        return inflater.inflate(R.layout.fragment_upload_post_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        localDataManager = new LocalDataManager();



        postActivity.findViewById(R.id.btnUploadPostMap).setClickable(true);
        postActivity.findViewById(R.id.btnUploadPostDetails).setClickable(false);

        edtcarDetail = view.findViewById(R.id.UPDEDTCarDetail);
        edtDestination = view.findViewById(R.id.UPDEDTDestination);
        edtDescription = view.findViewById(R.id.UPDEDTDescription);
        edtTime = view.findViewById(R.id.UPDTime);
        edtDate = view.findViewById(R.id.UPDDate);


        //spinner
        spinnerCity =view.findViewById(R.id.UPDSpinnerCity);
        spinnerAdapterCity = ArrayAdapter.createFromResource(view.getContext(), R.array.city_array, android.R.layout.simple_spinner_item);
        spinnerAdapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setAdapter(spinnerAdapterCity);

        Log.d("Tag", "onviewCreated" + localDataManager.getSharedPreference(postActivity,"time","YOK"));
        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cityString = adapterView.getItemAtPosition(i).toString();
                Log.d("Tag","cityString: "+ cityString);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerPassengerCount =view.findViewById(R.id.UPDSpinnerPassengerCount);
        //spinnerAdapterPassanger = ArrayAdapter.createFromResource(view.getContext(), list, android.R.layout.simple_spinner_item);
        spinnerAdapterPassanger = new ArrayAdapter<Integer>(postActivity.getApplicationContext(),android.R.layout.simple_spinner_item,items);
        spinnerAdapterPassanger.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPassengerCount.setAdapter(spinnerAdapterPassanger);


        spinnerPassengerCount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                passengerCountString = (int) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Doğum tarihi seçtirmek
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
                edtTime.setVisibility(View.VISIBLE);
//

            }
        };
        edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(view.getContext(), dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });


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





        postActivity.findViewById(R.id.btnUploadPostMap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveDetails();
                postActivity.changeFragment(new UploadMapFragment());
            }
        });

        postActivity.findViewById(R.id.btnUploadPost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDetails();
                postActivity.AUPUploadPost(view);


            }
        });

    }

    public void saveDetails(){

        cardet = edtcarDetail.getText().toString();
        Log.d("Tag","cardet" +cardet);


       if(timeStamp !=null){
           localDataManager.setSharedPreferenceForLong(postActivity,"timestamp",timeStamp.getSeconds());
           if(!dateString.equals("")){
               localDataManager.setSharedPreference(postActivity,"date",dateString);
           }
           if(!timeString.equals("")){
               localDataManager.setSharedPreference(postActivity,"time",timeString);
           }
       }
       if(cityString !=null){
           localDataManager.setSharedPreference(postActivity,"city",cityString);
       }
       if(passengerCountString != -1){
           localDataManager.setSharedPreferenceForInt(postActivity,"passengercount",passengerCountString);
       }
       //if(!cardet.equals("")){
           localDataManager.setSharedPreference(postActivity,"cardetail",cardet);

       //}
       //if(!edtDescription.getText().toString().equals("")){
           localDataManager.setSharedPreference(postActivity,"description",edtDescription.getText().toString());
       //}
       //if(!edtDestination.getText().toString().equals("")){
           localDataManager.setSharedPreference(postActivity,"destination",edtDestination.getText().toString());
       //}


    }

    @Override
    public void onResume() {
        super.onResume();



        Log.d("Tag", "onResume"+localDataManager.getSharedPreference(postActivity,"cardetail","YOK"));


        // eğer data var ise edittextler içinde görünmesi için

        String time = (localDataManager.getSharedPreference(postActivity,"time",null));
        String date = (localDataManager.getSharedPreference(postActivity,"date",null));




        cityString = localDataManager.getSharedPreference(postActivity,"city",null);
        passengerCountString = localDataManager.getSharedPreferenceForInt(postActivity,"passengercount",-1);
        String carDetail = localDataManager.getSharedPreference(postActivity,"cardetail",null);
        String description = localDataManager.getSharedPreference(postActivity,"description",null);
        String destination = localDataManager.getSharedPreference(postActivity,"destination",null);

        //saat seçildiyse geri geldiğinde seçilmiş saatin timetext'inde görünmesi için
       if(date!=null){
            if(time!=null){
//                edtTime.setVisibility(View.VISIBLE);
//                edtTime.setText(time);
//                String combinedDate = date + " " + time;
//
//                try {
//                    dateTimeStamp = dateCombinedFormat.parse(combinedDate);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                calendar.setTime(dateTimeStamp);
//
                edtDate.setHint(date + " " + time);
            }
           // edtDate.setText(date);

        }

        edtDestination.setText(destination);
        edtcarDetail.setText(carDetail);
        edtDescription.setText(description);
        spinnerCity.setSelection(spinnerAdapterCity.getPosition(cityString));
        spinnerPassengerCount.setSelection(spinnerAdapterPassanger.getPosition(passengerCountString));


    }
}