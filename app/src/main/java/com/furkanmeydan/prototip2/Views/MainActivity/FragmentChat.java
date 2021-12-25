package com.furkanmeydan.prototip2.Views.MainActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.furkanmeydan.prototip2.Models.Request;
import com.furkanmeydan.prototip2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.installations.Utils;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class FragmentChat extends Fragment {

    ImageView  imageView;
    TextView txtName, txtPostHeader, txtPostDate, txtPostTime;
    RecyclerView recyclerView;
    EditText edtMessage;
    ImageButton btnSend;
    Request request;
    String currentuserID;

    public FragmentChat() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments()!=null){
            request = (Request) getArguments().getSerializable("request");
        }
        currentuserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageView = view.findViewById(R.id.chatFragmentImage);
        txtName = view.findViewById(R.id.chatFragmentName);
        txtPostHeader = view.findViewById(R.id.chatFragmentPostHeader);
        txtPostDate = view.findViewById(R.id.chatFragmentPostDate);
        txtPostTime = view.findViewById(R.id.chatFragmentPostTime);
        edtMessage = view.findViewById(R.id.chatFragmentMessage);
        btnSend = view.findViewById(R.id.chatFragmentBtnSend);
        recyclerView = view.findViewById(R.id.chatFragmentRCL);
        setData();
    }

    public void setData(){
        if(request.getSenderID().equals(currentuserID)){
            txtName.setText(request.getPostOwnerName());
            Glide.with(getActivity()).load(request.getPostOwnerProfilePicture()).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            }).into(imageView);
        }
        else{
            txtName.setText(request.getSenderName());
            Glide.with(getActivity()).load(request.getSenderImage()).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            }).into(imageView);
        }
        txtPostHeader.setText(request.getPostHeader());

        SimpleDateFormat dateCombinedFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        Date date = new Date(TimeUnit.SECONDS.toMillis(request.getPostTimestamp()));

        String dateTime = dateCombinedFormat.format(date);
        String[] dateStringArray = dateTime.split(" ");
        String dateDate = dateStringArray[0];
        String dateTimeTime = dateStringArray[1];
        txtPostTime.setText(dateTimeTime);
        txtPostDate.setText(dateDate);

    }
}