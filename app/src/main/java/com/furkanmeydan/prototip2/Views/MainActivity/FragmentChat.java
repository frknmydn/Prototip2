package com.furkanmeydan.prototip2.Views.MainActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.furkanmeydan.prototip2.R;

import org.jetbrains.annotations.NotNull;


public class FragmentChat extends Fragment {

    ImageView  imageView;
    TextView txtName, txtPostHeader, txtPostDate, txtPostTime;
    RecyclerView recyclerView;
    EditText edtMessage;
    ImageButton btnSend;

    public FragmentChat() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
    }
}