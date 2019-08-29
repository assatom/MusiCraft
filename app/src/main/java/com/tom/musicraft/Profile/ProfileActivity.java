package com.tom.musicraft.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.tom.musicraft.Models.User;
import com.tom.musicraft.Models.UserAccountSettings;
import com.tom.musicraft.Models.UserSettings;
import com.tom.musicraft.R;
import com.tom.musicraft.Services.FirebaseService;
import com.tom.musicraft.Utils.BottomNavigationViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity
{
    private static final String TAG = "ProfileActivity";
    private Context context = ProfileActivity.this;


    private TextView mPosts, mFollowers, mFollowing, mDisplayName, mUsername, mWebsite, mDescription;
    private ProgressBar mProgressBar;
    //private CircleImageView mProfilePhoto;
    private GridView gridView;
    private Toolbar toolbar;
    private ImageView profileMenu;
    private BottomNavigationViewEx bottomNavigationView;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseService mFirebaseService;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.d(TAG, "onCreate: started.");

        init();
        setupFirebaseAuth();
        getFollowersCount();
//        getPostsCount();
        initNavigationView();
    }

    private void init(){
        Log.d(TAG, "init: inflating ");

        mDisplayName = (TextView) findViewById(R.id.display_name);
        mUsername = (TextView) findViewById(R.id.username);
        mWebsite = (TextView) findViewById(R.id.website);
        mDescription = (TextView) findViewById(R.id.description);
        //mProfilePhoto = (CircleImageView) findViewById(R.id.profile_photo);
        mPosts = (TextView) findViewById(R.id.tvPosts);
        mFollowers = (TextView) findViewById(R.id.tvFollowers);
        mFollowing = (TextView) findViewById(R.id.tvFollowing);
//        mProgressBar = (ProgressBar) findViewById(R.id.profileProgressBar);
        gridView = (GridView) findViewById(R.id.gridView);
        toolbar = (Toolbar) findViewById(R.id.profileToolBar);
        profileMenu = (ImageView) findViewById(R.id.profileMenu);
        bottomNavigationView = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);

        TextView editProfile = (TextView) findViewById(R.id.textEditProfile);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d(TAG, "onClick: navigating to " + context.getString(R.string.edit_profile_fragment));
//                Intent intent = new Intent(getActivity(), AccountSettingsActivity.class);
//                intent.putExtra(getString(R.string.calling_activity), getString(R.string.profile_activity));
//                startActivity(intent);
//                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }

    private void initNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewModel.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewModel.enableNavigation(context, this,bottomNavigationViewEx);
//        Menu menu = bottomNavigationViewEx.getMenu();
//        MenuItem menuItem = menu.getItem(0);
//        menuItem.setChecked(true);
    }

    private void setProfileWidgets(UserSettings userSettings) {
        //Log.d(TAG, "setProfileWidgets: setting widgets with data retrieving from firebase database: " + userSettings.toString());
        //Log.d(TAG, "setProfileWidgets: setting widgets with data retrieving from firebase database: " + userSettings.getSettings().getUsername());


        //User user = userSettings.getUser();
        UserAccountSettings settings = userSettings.getSettings();

        /// UniversalImageLoader.setImage(settings.getProfile_photo(), mProfilePhoto, null, "");

//        Glide.with(getActivity())
//                .load(settings.getProfile_photo())
//                .into(mProfilePhoto);

//        mUsername.setText(settings.getUsername());
        mProgressBar.setVisibility(View.GONE);
    }

    // ================================
    // Get from firebase functions
    // ================================

    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //retrieve user information from the database
                //setProfileWidgets(mFirebaseService.getUserSettings(dataSnapshot));

                //retrieve images for the user in question

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


//    private void setupGridView(){
//        Log.d(TAG, "setupGridView: Setting up image grid.");
//
//        final ArrayList<Photo> photos = new ArrayList<>();
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//        Query query = reference
//                .child(getString(R.string.dbname_user_photos))
//                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for ( DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
//
//                    Photo photo = new Photo();
//                    Map<String, Object> objectMap = (HashMap<String, Object>) singleSnapshot.getValue();
//
//                    try {
//                        photo.setCaption(objectMap.get(getString(R.string.field_caption)).toString());
//                        photo.setTags(objectMap.get(getString(R.string.field_tags)).toString());
//                        photo.setPhoto_id(objectMap.get(getString(R.string.field_photo_id)).toString());
//                        photo.setUser_id(objectMap.get(getString(R.string.field_user_id)).toString());
//                        photo.setDate_created(objectMap.get(getString(R.string.field_date_created)).toString());
//                        photo.setImage_path(objectMap.get(getString(R.string.field_image_path)).toString());
//
//                        ArrayList<Comment> comments = new ArrayList<Comment>();
//                        for (DataSnapshot dSnapshot : singleSnapshot
//                                .child(getString(R.string.field_comments)).getChildren()) {
//                            Comment comment = new Comment();
//                            comment.setUser_id(dSnapshot.getValue(Comment.class).getUser_id());
//                            comment.setComment(dSnapshot.getValue(Comment.class).getComment());
//                            comment.setDate_created(dSnapshot.getValue(Comment.class).getDate_created());
//                            comments.add(comment);
//                        }
//
//                        photo.setComments(comments);
//
//                        List<Like> likesList = new ArrayList<Like>();
//                        for (DataSnapshot dSnapshot : singleSnapshot
//                                .child(getString(R.string.field_likes)).getChildren()) {
//                            Like like = new Like();
//                            like.setUser_id(dSnapshot.getValue(Like.class).getUser_id());
//                            likesList.add(like);
//                        }
//                        photo.setLikes(likesList);
//                        photos.add(photo);
//                    }catch(NullPointerException e){
//                        Log.e(TAG, "onDataChange: NullPointerException: " + e.getMessage() );
//                    }
//                }
//
//                //setup our image grid
//                int gridWidth = getResources().getDisplayMetrics().widthPixels;
//                int imageWidth = gridWidth/NUM_GRID_COLUMNS;
//                gridView.setColumnWidth(imageWidth);
//
//                ArrayList<String> imgUrls = new ArrayList<String>();
//                for(int i = 0; i < photos.size(); i++){
//                    imgUrls.add(photos.get(i).getImage_path());
//                }
//                GridImageAdapter adapter = new GridImageAdapter(getActivity(),R.layout.layout_grid_imageview,
//                        "", imgUrls);
//                gridView.setAdapter(adapter);
//
//                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        mOnGridImageSelectedListener.onGridImageSelected(photos.get(position), ACTIVITY_NUM);
//                    }
//                });
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.d(TAG, "onCancelled: query cancelled.");
//            }
//        });
//    }
//
    int mFollowersCount;
    private void getFollowersCount(){
        mFollowersCount = 0;

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child(getString(R.string.dbname_followers))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: found follower:" + singleSnapshot.getValue());
                    mFollowersCount++;
                }
                mFollowers.setText(String.valueOf(mFollowersCount));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
//
//    private void getFollowingCount(){
//        mFollowingCount = 0;
//
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//        Query query = reference.child(getString(R.string.dbname_following))
//                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for(DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
//                    Log.d(TAG, "onDataChange: found following user:" + singleSnapshot.getValue());
//                    mFollowingCount++;
//                }
//                mFollowing.setText(String.valueOf(mFollowingCount));
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
//
//    private void getPostsCount(){
//        mPostsCount = 0;
//
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//        Query query = reference.child(getString(R.string.dbname_user_photos))
//                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for(DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
//                    Log.d(TAG, "onDataChange: found post:" + singleSnapshot.getValue());
//                    mPostsCount++;
//                }
//                mPosts.setText(String.valueOf(mPostsCount));
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
//
//    private void setProfileWidgets(UserSettings userSettings) {
//
//        //User user = userSettings.getUser();
//        UserAccountSettings settings = userSettings.getSettings();
//
//        UniversalImageLoader.setImage(settings.getProfile_photo(), mProfilePhoto, null, "");
//
//        mDisplayName.setText(settings.getDisplay_name());
//        mUsername.setText(settings.getUsername());
//        mWebsite.setText(settings.getWebsite());
//        mDescription.setText(settings.getDescription());
//        mProgressBar.setVisibility(View.GONE);
//    }




}
