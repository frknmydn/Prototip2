package com.furkanmeydan.prototip2.View.PostSearchResultDetailActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.furkanmeydan.prototip2.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class FragmentPostSearchResultDetail extends Fragment {
    private TextView postHeader, postCity, postPassangerCount, postTime, postDescription, postCarDetail;
    private PostSearchResultDetailActivity activity;

    public FragmentPostSearchResultDetail() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (PostSearchResultDetailActivity) getActivity();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_search_result_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        postHeader = view.findViewById(R.id.txtSearchResultDetailHeader);
        postCity = view.findViewById(R.id.txtSearchResultDetailCity);
        postPassangerCount = view.findViewById(R.id.txtSearchResultDetailPasangerCount);
        postTime = view.findViewById(R.id.txtSearchResultDetailDateTime);
        postDescription = view.findViewById(R.id.txtSearchResultDetailDescription);
        postCarDetail = view.findViewById(R.id.txtSearchResultDetailCarDet);

        setData();

    }

    private void setData() {
        postHeader.setText(activity.post.getDestination());
        postCity.setText(activity.post.getCity());
        postPassangerCount.setText(String.valueOf(activity.post.getPassengerCount()));
        postDescription.setText(activity.post.getDescription());
        postCarDetail.setText(activity.post.getCarDetail());

        //zaman işlemleri
        long timeStampp = activity.post.getTimestamp();
        Log.d("Post Tag timestamp", String.valueOf(timeStampp));
        SimpleDateFormat dateCombinedFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        Date date = new Date(TimeUnit.SECONDS.toMillis(timeStampp));

        String dateTime = dateCombinedFormat.format(date);
        //
        postTime.setText(dateTime);
    }
}