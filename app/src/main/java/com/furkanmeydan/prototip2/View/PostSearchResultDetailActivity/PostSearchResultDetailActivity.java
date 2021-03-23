package com.furkanmeydan.prototip2.View.PostSearchResultDetailActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.furkanmeydan.prototip2.DataLayer.ProfileDAL;
import com.furkanmeydan.prototip2.MAPROUTER.TaskLoadedCallback;
import com.furkanmeydan.prototip2.Model.CollectionHelper;
import com.furkanmeydan.prototip2.Model.Post;
import com.furkanmeydan.prototip2.Model.User;
import com.furkanmeydan.prototip2.R;
import com.furkanmeydan.prototip2.View.LoginRegisterActivity.LoginRegisterActivity;
import com.furkanmeydan.prototip2.View.MainActivity.HomeFragment;
import com.furkanmeydan.prototip2.View.MainActivity.MainActivity;
import com.furkanmeydan.prototip2.View.MainActivity.MyPostFragment;
import com.furkanmeydan.prototip2.View.MainActivity.ProfileFragment;
import com.furkanmeydan.prototip2.View.MainActivity.SignUpFragment;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class PostSearchResultDetailActivity extends AppCompatActivity implements TaskLoadedCallback {
    ProfileDAL profileDAL;
    Post post;
    private BottomNavigationView navigationView1;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_search_result_detail);

        init();
        navigationViewSettings();

        changeFragment(new FragmentPostSearchResultDetail());
    }

    private void navigationViewSettings() {

        navigationView1.setOnNavigationItemSelectedListener(navListener);


    }


    public void init(){

        firebaseAuth=FirebaseAuth.getInstance();
        Intent i = getIntent();
        Bundle bundle = i.getBundleExtra("bundle");

        if(bundle !=null){
            post = (Post) bundle.getSerializable("post");
        }
        profileDAL = new ProfileDAL();
        navigationView1 = findViewById(R.id.postSearchResultDetailBottomNavigation);
        navigationView1.bringToFront();
    }


    public void changeFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack("PostCallback");
        fragmentTransaction.replace(R.id.PostSearchResultContainer,fragment);
        fragmentTransaction.commit();
    }

    public void changeFragmentArgs(Fragment fragment,Bundle args){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragment.setArguments(args);
        fragmentTransaction.replace(R.id.PostSearchResultContainer,fragment);
        fragmentTransaction.commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if(item.getItemId()==R.id.Details){

                Toast.makeText(getApplicationContext(),"DET",Toast.LENGTH_LONG).show();
                changeFragment(new FragmentPostSearchResultDetail());

            }
            else if(item.getItemId()==R.id.Map){
                Toast.makeText(getApplicationContext(),"MAP",Toast.LENGTH_LONG).show();
                changeFragment(new FragmentPostSearchResultMap());
            }
            else if(item.getItemId()==R.id.showProfile){
                Toast.makeText(getApplicationContext(),"SHOW PROFILE",Toast.LENGTH_LONG).show();
                changeFragment(new FragmentPostSearchPostOwner());
            }
            else if(item.getItemId()==R.id.Questions){
                Toast.makeText(getApplicationContext(),"QUESTIONS",Toast.LENGTH_LONG).show();
                changeFragment(new FragmentPostSearchResultQuestions());
            }

            else if(item.getItemId()== R.id.Requests){
                Toast.makeText(getApplicationContext(),"REQUESTS",Toast.LENGTH_LONG).show();
            }
            return true;
        }
    };

    @Override
    public void onTaskDone(Object... values) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentPostSearchResultMap fr = (FragmentPostSearchResultMap) fm.findFragmentById(R.id.PostSearchResultContainer);
        if(fr.getCurrentPolyline() !=null){
            fr.getCurrentPolyline().remove();
        }
        fr.getmMap().addPolyline((PolylineOptions) values[0]);

    }

    /*
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.Details){
            Toast.makeText(this,"Detail",Toast.LENGTH_LONG).show();
        }
        else if(item.getItemId()==R.id.Map){
            Toast.makeText(this,"MAP",Toast.LENGTH_LONG).show();
        }
        else if(item.getItemId()==R.id.showProfile){
            Toast.makeText(this,"SHOW PROFILE",Toast.LENGTH_LONG).show();
        }
        else if(item.getItemId()==R.id.Questions){
            Toast.makeText(this,"QUESTIONS",Toast.LENGTH_LONG).show();
        }

        else if(item.getItemId()== R.id.Requests){
            Toast.makeText(this,"REQUESTS",Toast.LENGTH_LONG).show();
        }
        return true;
    }

     */



}