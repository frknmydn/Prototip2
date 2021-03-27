package com.furkanmeydan.prototip2.Views.PostSearchResultDetailActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.furkanmeydan.prototip2.DataLayer.ProfileDAL;
import com.furkanmeydan.prototip2.MapRouter.TaskLoadedCallback;
import com.furkanmeydan.prototip2.Models.Post;
import com.furkanmeydan.prototip2.R;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class PostSearchResultDetailActivity extends AppCompatActivity implements TaskLoadedCallback {
    ProfileDAL profileDAL;
    Post post;
    BottomNavigationView navigationView1;
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
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
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

        //Haritada directions geldikten sonra navigation itemlerinin geri açılması için
        for(int i = 0; i < navigationView1.getMenu().size();i++){
            navigationView1.getMenu().getItem(i).setEnabled(true);
        }
        //navigationView1.setClickable(true);
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

    public BottomNavigationView getNavigationView1() {
        return navigationView1;
    }
}