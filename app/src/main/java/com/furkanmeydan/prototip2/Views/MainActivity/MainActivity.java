package com.furkanmeydan.prototip2.Views.MainActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

//import com.furkanmeydan.prototip2.HomeFragmentDirections;
import com.furkanmeydan.prototip2.DataLayer.Callbacks.AppDatabase;
import com.furkanmeydan.prototip2.DataLayer.LocalDataManager;
import com.furkanmeydan.prototip2.DataLayer.Callbacks.ProfileCallback;
import com.furkanmeydan.prototip2.DataLayer.ProfileDAL;
import com.furkanmeydan.prototip2.Models.Car;
import com.furkanmeydan.prototip2.Models.CollectionHelper;
import com.furkanmeydan.prototip2.Models.ConnectionChecker;
import com.furkanmeydan.prototip2.Models.User;
import com.furkanmeydan.prototip2.R;
import com.furkanmeydan.prototip2.Views.LoginRegisterActivity.LoginRegisterActivity;
import com.furkanmeydan.prototip2.Views.PostSearchResultDetailActivity.LocationService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.onesignal.OSDeviceState;
import com.onesignal.OneSignal;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private FrameLayout mainFrameLayout;
    private ConstraintLayout constraintLayout;
    private Fragment fragment;
    ImageButton btnOpenDrawable;
    TextView txtHeader;


    //Internet kontrol
    ConnectionChecker connectionChecker;
    //Deneme yapıcam
    AppDatabase db;

    private String genderString, nameSurnameString, eMailString,profilePic,birthDate,oneSignalID;

        //For profile fragment
     FirebaseAuth firebaseAuth;
     FirebaseFirestore firebaseFirestore;
     StorageReference storageReference;
     FirebaseStorage firebaseStorage;
     LocalDataManager localDataManager;
     LocalDataManager localDataManagerUser;
     ProfileDAL profileDAL;

     String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initializing();
        navigationViewSettings();
        changeFragment(new HomeFragment());
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "carsDB").build();


    }

    private void navigationViewSettings() {
        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.Open,R.string.Close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item);
    }

    private void initializing(){
        localDataManager = new LocalDataManager();
        localDataManagerUser = new LocalDataManager();
        profileDAL = new ProfileDAL();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        btnOpenDrawable = findViewById(R.id.btnOpenDrawable);
        txtHeader = findViewById(R.id.textView42);

        // sharedPrefte kullanıcı bilgileri var ise çekmek için
        nameSurnameString = localDataManagerUser.getSharedPreference(this,"sharedNameSurname",null);
        eMailString = localDataManagerUser.getSharedPreference(this,"sharedEmail",null);
        birthDate = localDataManagerUser.getSharedPreference(this,"sharedBirthdate",null);
        genderString = localDataManagerUser.getSharedPreference(this,"sharedGender",null);
        profilePic = localDataManagerUser.getSharedPreference(this,"sharedImageURL",null);
        oneSignalID = localDataManager.getSharedPreference(this,"sharedOneSignalID",null);

        OSDeviceState device = OneSignal.getDeviceState();
        String oneSignalIDOriginal = device.getUserId();
        Log.d("Tag","OneSignalID: "+oneSignalID);
        Log.d("Tag","OneSignalIDOriginal: "+oneSignalIDOriginal);


        connectionChecker = new ConnectionChecker();

        userId = firebaseAuth.getCurrentUser().getUid();

        storageReference = firebaseStorage.getReference();
        navigationView = findViewById(R.id.NavigationView);
        navigationView.bringToFront();
        drawerLayout = findViewById(R.id.DrawerLayoutMain);
        mainFrameLayout=findViewById(R.id.consLayout);


        if(genderString==null){
            profileDAL.getProfile(userId, new ProfileCallback() {
                @Override
                public void getUser(User user) {
                    super.getUser(user);

                    localDataManagerUser.setSharedPreference(getApplicationContext(),"sharedNameSurname",user.getNameSurname());
                    localDataManagerUser.setSharedPreference(getApplicationContext(),"sharedEmail",user.getEmail());
                    localDataManagerUser.setSharedPreference(getApplicationContext(),"sharedBirthdate",user.getBirthDate());
                    localDataManagerUser.setSharedPreference(getApplicationContext(),"sharedGender",user.getGender());
                    localDataManagerUser.setSharedPreference(getApplicationContext(),"sharedImageURL",user.getProfilePicture());
                    localDataManagerUser.setSharedPreference(getApplicationContext(),"sharedOneSignalID",user.getOneSignalID());

                }
            });

        }

        btnOpenDrawable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });


        //constraintLayout=findViewById(R.id.consLayout);
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        if(item.getItemId()==R.id.homePage){
            changeFragment(new HomeFragment());
        }
        else if(item.getItemId()==R.id.profile){
            try {
                //internet bağlantısı kontrolü sağlanıyor
                if(!connectionChecker.isConnected()){
                    connectionChecker.showWindow(this);
                }
                else{
                    firebaseFirestore.collection(CollectionHelper.USER_COLLECTION).
                            document(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())
                            .get().addOnCompleteListener(task -> {
                        User user = Objects.requireNonNull(task.getResult()).toObject(User.class);
                        if(user !=null){
                            changeFragment(new ProfileFragment());
                        }else{
                            changeFragment(new SignUpFragment());
                        }
                    });
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }




        }
        else if(item.getItemId()==R.id.signOut){

            try {
                if(connectionChecker.isConnected()){
                    firebaseAuth.signOut();
                    String serviceRunning = localDataManager.getSharedPreference(MainActivity.this, "isServiceEnable","0");
                    if(!serviceRunning.equals("0")) {


                        Intent serviceIntent = new Intent(MainActivity.this, LocationService.class);
                        serviceIntent.putExtra("action", "0");
                        ContextCompat.startForegroundService(MainActivity.this, serviceIntent);
                        localDataManager.setSharedPreference(MainActivity.this, "isServiceEnable", "0");
                    }
                    localDataManager.clearSharedPreference(MainActivity.this);
                    Intent i = new Intent(MainActivity.this, LoginRegisterActivity.class);
                    startActivity(i);
                    this.finish();
                }
                else{
                    connectionChecker.showWindow(this);
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }


        }
        else if(item.getItemId()==R.id.questionsToMe){
            try {
                if (connectionChecker.isConnected()) {
                    changeFragment(new QuestionsToMeFragment());
                }
                else{
                    connectionChecker.showWindow(this);
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }

        }

        else if(item.getItemId()== R.id.myPosts){
            try {
                if(connectionChecker.isConnected()){
                    changeFragment(new MyPostFragment());

                }
                else{
                    connectionChecker.showWindow(this);
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }


        }

        else if(item.getItemId() == R.id.requestsToMyPosts){
            try {
                if(connectionChecker.isConnected()){
                    changeFragment(new FragmentRequestsToMyPosts());

                }
                else {
                    connectionChecker.showWindow(this);
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }

        else if(item.getItemId() == R.id.RequestsISent){
            try {
                if(connectionChecker.isConnected()){
                    changeFragment(new FragmentMyRequests2());

                }
                else {
                    connectionChecker.showWindow(this);
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
        else if(item.getItemId() == R.id.wish_list){
            try {
                if(connectionChecker.isConnected()){
                    changeFragment(new FragmentWishList());
                }
                else {
                    connectionChecker.showWindow(this);
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }

        }
        else if(item.getItemId() == R.id.blockedUsers){
            try {
                if(connectionChecker.isConnected()){
                    changeFragment(new FragmentBlockList());
                }
                else {
                    connectionChecker.showWindow(this);
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
        else if(item.getItemId() == R.id.my_cars){
            try {
                if(connectionChecker.isConnected()){
                    changeFragment(new FragmentMyAllCars());
                }
                else {
                    connectionChecker.showWindow(this);
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }

        }
        return true;
    }

    public void changeFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack("ProfileCallback");
        fragmentTransaction.replace(R.id.consLayout,fragment);
        fragmentTransaction.commit();
    }

    public void changeFragmentArgs(Fragment fragment,Bundle args){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack("ProfileCallback");
        fragment.setArguments(args);
        fragmentTransaction.replace(R.id.consLayout,fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //changeFragment(new HomeFragment());
    }

    public String getOneSignalID() {
        return oneSignalID;
    }
}