package com.furkanmeydan.prototip2.Views.MainActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

//import com.furkanmeydan.prototip2.HomeFragmentDirections;
import com.furkanmeydan.prototip2.DataLayer.LocalDataManager;
import com.furkanmeydan.prototip2.DataLayer.ProfileCallback;
import com.furkanmeydan.prototip2.DataLayer.ProfileDAL;
import com.furkanmeydan.prototip2.Models.CollectionHelper;
import com.furkanmeydan.prototip2.Models.User;
import com.furkanmeydan.prototip2.R;
import com.furkanmeydan.prototip2.Views.LoginRegisterActivity.LoginRegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private FrameLayout mainFrameLayout;
    private ConstraintLayout constraintLayout;
    private Fragment fragment;

    private String genderString, nameSurnameString, eMailString,profilePic,birthDate;

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

        // sharedPrefte kullanıcı bilgileri var ise çekmek için
        nameSurnameString = localDataManagerUser.getSharedPreference(this,"sharedNameSurname",null);
        eMailString = localDataManagerUser.getSharedPreference(this,"sharedEmail",null);
        birthDate = localDataManagerUser.getSharedPreference(this,"sharedBirthdate",null);
        genderString = localDataManagerUser.getSharedPreference(this,"sharedGender",null);
        profilePic = localDataManagerUser.getSharedPreference(this,"sharedImageURL",null);




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

                }
            });

        }


        //constraintLayout=findViewById(R.id.consLayout);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        if(item.getItemId()==R.id.homePage){
            changeFragment(new HomeFragment());
        }
        else if(item.getItemId()==R.id.profile){
            firebaseFirestore.collection(CollectionHelper.USER_COLLECTION).document(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    User user = Objects.requireNonNull(task.getResult()).toObject(User.class);
                    if(user !=null){
                        changeFragment(new ProfileFragment());
                    }else{
                        changeFragment(new SignUpFragment());
                    }
                }
            });


        }
        else if(item.getItemId()==R.id.signOut){
            firebaseAuth.signOut();
            Intent i = new Intent(MainActivity.this, LoginRegisterActivity.class);
            startActivity(i);
            this.finish();
        }
        else if(item.getItemId()==R.id.questionsToMe){
            changeFragment(new QuestionsToMeFragment());
        }

        else if(item.getItemId()== R.id.myPosts){
            changeFragment(new MyPostFragment());
        }

        else if(item.getItemId() == R.id.requestsToMyPosts){
            changeFragment(new FragmentRequestsToMyPosts());
        }

        else if(item.getItemId() == R.id.RequestsISent){
            changeFragment(new FragmentMyRequests());
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
        fragment.setArguments(args);
        fragmentTransaction.replace(R.id.consLayout,fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        changeFragment(new HomeFragment());
    }

}