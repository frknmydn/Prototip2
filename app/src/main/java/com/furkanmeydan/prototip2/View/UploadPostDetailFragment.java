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

import com.furkanmeydan.prototip2.DataLayer.PostDetailDataPasser;
import com.furkanmeydan.prototip2.R;
import com.google.firebase.Timestamp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UploadPostDetailFragment extends Fragment {

    EditText edtcarDetail,edtDestination,edtDescription,edtTime,edtDate;
    Spinner spinnerCity,spinnerPassengerCount;
    ArrayAdapter<CharSequence> spinnerAdapterCity,spinnerAdapterPassanger;
    UploadPostActivity postActivity;

    PostDetailDataPasser dataPasser;
    String cityString,passengerCountString,timeString,dateCombined;

    //Tarih ile ilgili
    private String dateString = "";
    private SimpleDateFormat dateFormat;
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


        postActivity.findViewById(R.id.btnUploadPostMap).setClickable(true);
        postActivity.findViewById(R.id.btnUploadPostDetails).setClickable(false);

        edtcarDetail = view.findViewById(R.id.UPDEDTCarDetail);
        edtDestination = view.findViewById(R.id.UPDEDTDestination);
        edtDescription = view.findViewById(R.id.UPDEDTDescription);
        edtTime = view.findViewById(R.id.UPDTime);
        edtDate = view.findViewById(R.id.UPDDate);


        //spinner
        spinnerCity = (Spinner) view.findViewById(R.id.UPDSpinnerCity);
        spinnerAdapterCity = ArrayAdapter.createFromResource(view.getContext(), R.array.city_array, android.R.layout.simple_spinner_item);
        spinnerAdapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setAdapter(spinnerAdapterCity);


        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cityString = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerPassengerCount = (Spinner) view.findViewById(R.id.UPDSpinnerPassengerCount);
        spinnerAdapterPassanger = ArrayAdapter.createFromResource(view.getContext(), R.array.passanger_count_array, android.R.layout.simple_spinner_item);
        spinnerAdapterPassanger.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPassengerCount.setAdapter(spinnerAdapterPassanger);


        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                passengerCountString = adapterView.getItemAtPosition(i).toString();
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
                calendar.set(Calendar.MONTH, i1 + 1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);

                dateString = String.format("%02d/%02d/%04d", i2, i1 + 1, i);
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
                        timePicker.getHour();
                        timeString = String.format("%02d:%02d", i , i1);


                        edtTime.setText(timeString);

                        dateCombined = dateCombined + " " + timeString;


                        //timestamp oluşturmak için


                        try {
                            dateTimeStamp = dateFormat.parse(dateCombined);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        timeStamp = new Timestamp(dateTimeStamp);
                        Log.d("Tag",timeStamp.toString());

                    }
                },hour,minutes,true);

                timePickerDialog.show();
            }

        });






    }

    @Override
    public void onResume() {
        super.onResume();

        //saat seçildiyse geri geldiğinde seçilmiş saatin timetext'inde görünmesi için
        if(!edtTime.getText().toString().equals("")){
            edtTime.setVisibility(View.VISIBLE);
        }
    }
}