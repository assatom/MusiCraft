package com.tom.musicraft.Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tom.musicraft.Home.HomeFragment;
import com.tom.musicraft.Profile.ProfileFragment;
import com.tom.musicraft.R;

public class MainActivity extends AppCompatActivity {


    BottomNavigationView bottomNavigationView;
    Fragment selectedFragment = null;

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.ic_house:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.ic_search:
                            selectedFragment = null;
//                            selectedFragment = new SearchFragment();
                            break;
                        case R.id.ic_android:
                            selectedFragment = new ProfileFragment();
//                            SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
//                            editor.putString("profileid", db.getCurrentUser().getUid());
//                            editor.apply();
//                            selectedFragment = new ProfileFragment();
                            break;
                        case R.id.ic_circle:
                            selectedFragment = null;
//                            startActivity(new Intent(MainActivity.this, PostActivity.class));
                            break;
                    }

                    if(selectedFragment != null) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    }
                    return true;
                }
            };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        db = new FirebaseDB(this);
        bottomNavigationView = findViewById(R.id.bottomNavViewBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        Bundle intent = getIntent().getExtras();
//        if (intent != null && intent.getString("publisherid") != null){
//            String publisher = intent.getString("publisherid");
//
//            SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
//            editor.putString("profielid", publisher);
//            editor.apply();
//
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                    new ProfileFragment()).commit();
//        }
//        else{
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
//        }

    }
}
