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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
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
import com.tom.musicraft.Models.UserAccountSettings;
import com.tom.musicraft.Post.PostViewModel;
import com.tom.musicraft.R;
import com.tom.musicraft.Services.FirebaseService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import butterknife.Unbinder;

public class AddPostFragment extends Fragment {

    public static String pattern = "MM/dd/yyyy HH:mm:ss";
    private static final String TAG = "AddPostFragment";
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
    PostViewModel mViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_uploadvideo, container, false);
        ButterKnife.bind(this, view);
        unbinder = ButterKnife.bind(this, view);
        mViewModel = new PostViewModel();
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

    @Optional @OnClick(R.id.sharevideo) void onMaybeMissingClicked() {
        Log.d(TAG, "share button onClick: navigating to home.");

        if(videodeurl.getText().toString().isEmpty())
        {
            Toast.makeText(getContext(),"You must fill out all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String userID = FirebaseService.getInstance().getCurrentUser().getUid();
        UserAccountSettings u = FirebaseService.getInstance().getCurrentUserAccountSettings();
        String userName = FirebaseService.getInstance().getCurrentUserAccountSettings().getUserName();

        DateFormat df = new SimpleDateFormat(pattern);

        // Get the today date using Calendar object.
        Date today = Calendar.getInstance().getTime();
        // Using DateFormat format method we can create a string
        // representation of a date with the defined format.
        String todayAsString = df.format(today);
        Post p = new Post(videodesciption.getText().toString(),
                generateVideoURL(videodeurl.getText().toString()),
                todayAsString,
                userID, userName);

        // Replaced:  mFirebaseService.addPost(p);
        mViewModel.uploadPost(p);

        Toast.makeText(getContext(),"Your post added successfully", Toast.LENGTH_SHORT).show();

        BottomNavigationView nav = (BottomNavigationView)getActivity().findViewById(R.id.bottomNavViewBar);
        nav.setSelectedItemId(R.id.ic_house);
    }


    private String generateVideoURL(String youtubeUrl)
    {
        String[] arr = youtubeUrl.split("=");
        String suffix = arr[arr.length-1];
        String url = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/"+suffix+"\" frameborder=\"0\" allowfullscreen></iframe>";
        return url;
    }

}
