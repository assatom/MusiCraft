package com.tom.musicraft.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.tom.musicraft.Home.HomeActivity;
import com.tom.musicraft.Home.HomeFragment;
import com.tom.musicraft.Profile.ProfileActivity;
import com.tom.musicraft.R;

public class BottomNavigationViewModel
{
    private static final String TAG = "BottomNavigationViewHel";

    public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx){
        Log.d(TAG, "setupBottomNavigationView: Setting up BottomNavigationView");
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(false);
    }

    public static void enableNavigation(final Context context, final Activity callingActivity, BottomNavigationViewEx view){

        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.ic_house:
                       Intent intent1 = new Intent(context, HomeActivity.class);//ACTIVITY_NUM = 0
                       context.startActivity(intent1);
                      // callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                       break;

//                    case R.id.ic_search:
//                        Intent intent2  = new Intent(context, SearchActivity.class);//ACTIVITY_NUM = 1
//                        context.startActivity(intent2);
//                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//                        break;

//                    case R.id.ic_circle:
//                        Intent intent3 = new Intent(context, ShareActivity.class);//ACTIVITY_NUM = 2
//                        context.startActivity(intent3);
//                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//                        break;
//
//                    case R.id.ic_alert:
//                        Intent intent4 = new Intent(context, LikesActivity.class);//ACTIVITY_NUM = 3
//                        context.startActivity(intent4);
//                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//                        break;

                    case R.id.ic_android:
                        Intent intent5 = new Intent(context, ProfileActivity.class);//ACTIVITY_NUM = 4
                        context.startActivity(intent5);
//                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;
                }


                return false;
            }
        });



    }

    static Fragment fragment = null;
    static HomeFragment home = null;
    public static void Initialize(Fragment fragment2)
    {
        fragment = fragment2;
        home = new HomeFragment();
    }


    public static void NavigateTo( BottomNavigationViewEx view) // final Context context, final Fragment fragment,
    {
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.ic_house:
//                        Intent intent1 = new Intent(context, HomeActivity.class);//ACTIVITY_NUM = 0
                        fragment.setTargetFragment(home, 1);
                        break;
                }
                return false;
            }
        });
    }






}
