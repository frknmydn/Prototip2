package com.furkanmeydan.prototip2.Views.MainActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.furkanmeydan.prototip2.Adapters.MessageAdapter;
import com.furkanmeydan.prototip2.DataLayer.NotificationManager;
import com.furkanmeydan.prototip2.Models.Message;
import com.furkanmeydan.prototip2.Models.Request;
import com.furkanmeydan.prototip2.R;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.installations.Utils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


public class FragmentChat extends Fragment {

    ImageView imageView;
    TextView txtName, txtPostHeader, txtPostDate, txtPostTime;
    RecyclerView recyclerView;
    EditText edtMessage;
    ImageButton btnSend;
    Request request;
    String currentuserID;
    MessageAdapter messageAdapter;
    ArrayList<Message> messagesList;
    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://prototip2-69173-default-rtdb.europe-west1.firebasedatabase.app/").getReference();

    public FragmentChat() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            request = (Request) getArguments().getSerializable("request");
        }
        messagesList = new ArrayList<>();
        currentuserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //getDataInitial();
        messageAdapter = new MessageAdapter(messagesList, getActivity());
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
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(messageAdapter);

        setData();
        getDataFeed();

        btnSend.setOnClickListener(listener -> sendData());

    }

    public void setData() {
        if (request.getSenderID().equals(currentuserID)) {
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
        } else {
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

    public void sendData() {
        String messageString = edtMessage.getText().toString();
        Long timestamp = Timestamp.now().getSeconds();
        String senderID = currentuserID;
        String receiverID;
        String receiverName;
        if (request.getSenderID().equals(currentuserID)) {
            receiverID = request.getPostOwnerID();
            receiverName = request.getSenderName();
        } else {
            receiverID = request.getSenderID();
            receiverName = request.getPostOwnerName();

        }
        Message message = new Message(senderID, receiverID, timestamp, messageString);
        String uuid = UUID.randomUUID().toString();
        databaseReference.child("messages").child(request.getPostID()).child(uuid).setValue(message);
        messageAdapter.notifyDataSetChanged();
        edtMessage.setText("");


        Thread t1 = new Thread(){
            @Override
            public void run() {
                super.run();

                    NotificationManager notificationManager = new NotificationManager(receiverID,message.getMessage(),"Mesaj: " + receiverName);
                    try {
                        notificationManager.NotificationForNow();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }



            }
        };
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void getDataFeed() {


        databaseReference.child("messages").child(request.getPostID()).orderByChild("timestamp").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message message = snapshot.getValue(Message.class);
                if(!messagesList.contains(message)){
                    messagesList.add(message);
                    recyclerView.scrollToPosition(messagesList.size() - 1);
                    messageAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



}