package com.tom.musicraft.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.tom.musicraft.Adapters.PostsListAdapter;
import com.tom.musicraft.Login.LoginActivity;
import com.tom.musicraft.Models.Post;
import com.tom.musicraft.Models.UserAccountSettings;
import com.tom.musicraft.Post.PostsListViewModel;
import com.tom.musicraft.R;
import com.tom.musicraft.Services.FirebaseService;

import java.util.List;
import java.util.Vector;

public class ProfileFragment extends Fragment
{
    private static final String TAG = "ProfileFragment";
    private TextView mPosts, mFollowers, mFollowing, mDisplayName, mUsername;
    private ProgressBar mProgressBar;
    //private CircleImageView mProfilePhoto;
    private GridView gridView;
    private Toolbar toolbar;
    private ImageView profileMenu;
    private BottomNavigationViewEx bottomNavigationView;
    private RecyclerView mListView;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseService mFirebaseService;
    private FirebaseUser firebaseUser;

    private boolean currentUserFlag = true;
    private PostsListViewModel mViewModel;
    private Vector<Post> mPostsList = new Vector<>();
    UserAccountSettings user;

    public ProfileFragment(UserAccountSettings user)
    {
        this.user = user;
    }
    public ProfileFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        init(view);
        setUserDetalies();

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void initPosts()
    {
        mViewModel = ViewModelProviders.of(this).get(PostsListViewModel.class);
        mListView.setHasFixedSize(true);
        mListView.setLayoutManager(new LinearLayoutManager(this.getActivity()));


        PostsListAdapter adapter = new PostsListAdapter(mPostsList);
        mListView.setAdapter(adapter);

        mViewModel.getAllPostsByUser(user.getUser_id()).observe(this, new Observer<List<Post>>() {
            @Override
            public void onChanged(@Nullable final List<Post> posts) {
                // Update the cached copy of the words in the adapter.
                adapter.setPosts(posts);
            }
        });


    }

    private void init(View view){

        mDisplayName = (TextView) view.findViewById(R.id.display_name);
        mUsername = (TextView) view.findViewById(R.id.username);
//        mDescription = (TextView) view.findViewById(R.id.description);
        //mProfilePhoto = (CircleImageView) findViewById(R.id.profile_photo);
        mFollowers = (TextView) view.findViewById(R.id.tvFollowers);
        mFollowing = (TextView) view.findViewById(R.id.tvFollowing);
//        mProgressBar = (ProgressBar) findViewById(R.id.profileProgressBar);
        gridView = (GridView) view.findViewById(R.id.gridView);
        toolbar = (Toolbar) view.findViewById(R.id.profileToolBar);
        profileMenu = (ImageView) view.findViewById(R.id.profileMenu);
        bottomNavigationView = (BottomNavigationViewEx) view.findViewById(R.id.bottomNavViewBar);
        mListView = (RecyclerView) view.findViewById(R.id.profile_postsList);

        TextView editProfile = (TextView) view.findViewById(R.id.textEditProfile);


        profileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                FirebaseService.getInstance().logout();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

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


    private void setUserDetalies()
    {
        if(user!= null)
        {
            insertUserDetailes();
            return;
        }

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userID = firebaseUser.getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        myRef = mFirebaseDatabase.getReference();
        DatabaseReference usersRef = myRef.child("Users").child(userID);
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(getContext() == null)
                    return;

                user = dataSnapshot.getValue(UserAccountSettings.class);
                if(user!=null) {
                    insertUserDetailes();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void insertUserDetailes()
    {
        mFollowers.setText(String.valueOf(user.getFollowers()));
        mFollowing.setText(String.valueOf(user.getFollowing()));
        mDisplayName.setText(String.valueOf(user.getUserName()));
        mUsername.setText(String.valueOf(user.getUserName()));
        initPosts();
        // Glide.with(getContext()).load(user.getImageurl()).into(image_profile);
        //                mPosts= user.getPosts();
    }
}
