package com.furkanmeydan.prototip2.Views.PostActivity;

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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.furkanmeydan.prototip2.DataLayer.PostDAL;
import com.furkanmeydan.prototip2.R;
import com.google.firebase.Timestamp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class PostSearchFragment extends Fragment {

    EditText edtSearchDate, edtSearchTime1, edtSearchTime2;
    Spinner spnSearchCity, spnSearchGender;
    Button btnSearch;

    String cityString, genderString;
    String dateString;
    String timeString1,timeString2, dateCombined1, dateCombined2;


    private SimpleDateFormat simpleDateFormat, dateTimeFormat, dateCombinedFormat;

    Calendar calendar;
    Date dateDate1, dateDate2;
    Timestamp timestamp1 , timestamp2;
    TimePickerDialog timePickerDialog1, timePickerDialog2;

    ArrayAdapter<CharSequence> searchCitySpinnerAdapter;
    ArrayAdapter<CharSequence> searchGenderSpinnerAdapter;


    PostActivity postActivity;
    PostDAL postDAL;



    public PostSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        postActivity = (PostActivity) getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        simpleDateFormat =new SimpleDateFormat("dd/MM/yyyy");
        dateTimeFormat = new SimpleDateFormat("HH:mm");
        dateCombinedFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        calendar = Calendar.getInstance();
        postDAL = new PostDAL();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_search, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edtSearchDate = view.findViewById(R.id.edtSearchDateFS);
        edtSearchTime1 = view.findViewById(R.id.edtSearchTime1FS);
        edtSearchTime2 = view.findViewById(R.id.edtSearchTime2FS);

        spnSearchCity = view.findViewById(R.id.spinnerSearchCityFS);
        spnSearchGender = view.findViewById(R.id.spinnerSearchGenderFS);

        btnSearch = view.findViewById(R.id.btnSearchPost);


        //city spinner
        searchCitySpinnerAdapter = ArrayAdapter.createFromResource(view.getContext(),R.array.city_array, android.R.layout.simple_spinner_item);
        searchCitySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSearchCity.setAdapter(searchCitySpinnerAdapter);

        spnSearchCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cityString = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getActivity(),"Lütfen Şehir Seçiniz",Toast.LENGTH_LONG).show();
            }
        });

        // gender spinner
        searchGenderSpinnerAdapter = ArrayAdapter.createFromResource(view.getContext(),R.array.search_genders_array,android.R.layout.simple_spinner_item);
        searchGenderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSearchGender.setAdapter(searchGenderSpinnerAdapter);
        spnSearchGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(adapterView.getItemAtPosition(i).toString().equals("Cinsiyet")){
                    Toast.makeText(postActivity,"Cinsiyet içi ",Toast.LENGTH_LONG).show();
                }
                else{
                    genderString= adapterView.getItemAtPosition(i).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        // tarih seçtirme
        final DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, i, i1, i2) -> {
            calendar.set(Calendar.YEAR,i);
            calendar.set(Calendar.MONTH,i1);
            calendar.set(Calendar.DAY_OF_MONTH,i2);


            //seçilen zamanı STring'e çevirme
            dateString = simpleDateFormat.format(calendar.getTime());
            dateCombined1 = dateString;
            dateCombined2 = dateString;
            edtSearchDate.setText(dateString);
            edtSearchTime1.setVisibility(View.VISIBLE);
            edtSearchTime2.setVisibility(View.VISIBLE);

        };





        edtSearchDate.setOnClickListener(view1 ->
                new DatePickerDialog(view1.getContext(),dateSetListener,calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show());



        edtSearchTime1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minutes = calendar.get(Calendar.MINUTE);

                timePickerDialog1 = new TimePickerDialog(getActivity(), (timePicker, i, i1) -> {
                    calendar.set(Calendar.HOUR_OF_DAY,i);
                    calendar.set(Calendar.MINUTE,i1);

                    timeString1 = dateTimeFormat.format(calendar.getTime());
                    edtSearchTime1.setText(timeString1);

                    dateCombined1 = dateString;
                    dateCombined1 = dateCombined1 + " "+ timeString1;

                    try{
                        dateDate1 = dateCombinedFormat.parse(dateCombined1);
                    }
                    catch (ParseException e){

                    }
                    assert dateDate1 != null;
                    timestamp1 = new Timestamp(dateDate1);
                    //Log.d("TAG timestamp1",timestamp1.toString());


                },hour,minutes,true);
                timePickerDialog1.show();

            }
        });



        edtSearchTime2.setOnClickListener(view12 -> {
            calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);

            timePickerDialog2 = new TimePickerDialog(getActivity(), (timePicker, i, i1) -> {
                calendar.set(Calendar.HOUR_OF_DAY,i);
                calendar.set(Calendar.MINUTE,i1);

                timeString2 = dateTimeFormat.format(calendar.getTime());
                edtSearchTime2.setText(timeString2);


                dateCombined2 = dateString;
                dateCombined2 = dateCombined2 + " " + timeString2;

                try{
                    dateDate2 = dateCombinedFormat.parse(dateCombined2);
                }
                catch (ParseException e){

                }
                assert dateDate2 != null;
                timestamp2 = new Timestamp(dateDate2);
                //Log.d("TAG datecombined2",dateCombined2);
                //Log.d("TAG timestamp2",timestamp2.toString());
            },hour,minutes,true);
            timePickerDialog2.show();

        });


        btnSearch.setOnClickListener(view13 -> {


            Bundle args = postDAL.checkArgs(timestamp1,timestamp2,genderString,cityString,postActivity);
            if(args !=null){
                postActivity.changeFragmentArgs(new PostSearchUserLocationMapFragment(),args);
            }

        });

    }
}