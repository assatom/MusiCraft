package com.tom.musicraft.Home;

import androidx.fragment.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.auth.FirebaseAuth;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.tom.musicraft.Models.Post;
import com.tom.musicraft.R;
import com.tom.musicraft.Services.FirebaseService;
import com.tom.musicraft.Utils.BottomNavigationViewModel;

public class HomeActivity extends AppCompatActivity
{
    private static final String TAG = "HomeActivity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Context context = HomeActivity.this;
    private FirebaseService firebaseService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG, "onCreate: starting.");
//        mViewPager = (ViewPager) findViewById(R.id.viewpager_container);
//        mFrameLayout = (FrameLayout) findViewById(R.id.container);
//        mRelativeLayout = (RelativeLayout) findViewById(R.id.relLayoutParent);
//
//        setupFirebaseAuth();
//
//        initImageLoader();
        initNavigationView();
        String url = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/Qopx44kAmdE\" frameborder=\"0\" allowfullscreen></iframe>";
      //  Post p = new Post("Some text", url , "01/01/2019",  null);
       // firebaseService.addPost(p);

//        setupViewPager();

    }

    private void initNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewModel.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewModel.enableNavigation(context, this,bottomNavigationViewEx);

        Fragment mainFragment = getSupportFragmentManager().findFragmentById(R.id.mainFragment);

        BottomNavigationViewModel.Initialize(mainFragment);
        BottomNavigationViewModel.NavigateTo(bottomNavigationViewEx);
//        Menu menu = bottomNavigationViewEx.getMenu();
//        MenuItem menuItem = menu.getItem(0);
//        menuItem.setChecked(true);
    }





}
