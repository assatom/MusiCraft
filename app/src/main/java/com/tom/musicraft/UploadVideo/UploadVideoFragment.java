package com.tom.musicraft.UploadVideo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.tom.musicraft.Home.HomeActivity;
import com.tom.musicraft.Login.LoginActivity;
import com.tom.musicraft.Login.RegisterActivity;
import com.tom.musicraft.Main.MainActivity;
import com.tom.musicraft.Models.Post;
import com.tom.musicraft.R;
import com.tom.musicraft.Services.FirebaseService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import butterknife.Unbinder;

public class UploadVideoFragment extends Fragment {

    private static final String TAG = "UploadVideoFragment";
    @Nullable
    @BindView(R.id.videodescription)
    EditText videodesciption;
    @Nullable
    @BindView(R.id.videourl)
    EditText videodeurl;
    @Nullable
    @BindView(R.id.sharevideo)
    Button shareBtn;

    private Unbinder unbinder;
    private Context mContext;
    private View view;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseService mFirebaseService;
    FirebaseUser firebaseUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_uploadvideo, container, false);
        ButterKnife.bind(this, view);
        unbinder = ButterKnife.bind(this, view);
        //init(view);
        return view;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext=context;
        mFirebaseService = new FirebaseService(mContext);
        Log.d(TAG, "onCreate: started.");
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_register);
    }

    private void init(View view){

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Optional
            @Override
            public void onClick(View v) {
                Log.d(TAG, "share button onClick: navigating to home.");
                Post p = new Post(videodesciption.getText().toString(), videodeurl.getText().toString() , "01/01/2019",  null);
                mFirebaseService.addPost(p);
                Intent intent1 = new Intent(mContext, HomeActivity.class);//ACTIVITY_NUM = 0
                mContext.startActivity(intent1);
            }
        });
    }

    @Optional @OnClick(R.id.sharevideo) void onMaybeMissingClicked() {
        Log.d(TAG, "share button onClick: navigating to home.");
        Post p = new Post(videodesciption.getText().toString(), videodeurl.getText().toString() , "01/01/2019",  null);
        mFirebaseService.addPost(p);
       // Intent intent1 = new Intent(mContext, HomeActivity.class);//ACTIVITY_NUM = 0
       // mContext.startActivity(intent1);
    }
}
