package com.tom.musicraft.Services;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tom.musicraft.Models.User;
import com.tom.musicraft.R;

public class FirebaseService
{
    private static final String TAG = "FirebaseService";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
//    private StorageReference mStorageReference;
    private String userID;
    private Context context;


    public FirebaseService(Context context)
    {
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
//        mStorageReference = FirebaseStorage.getInstance().getReference();

        if(mAuth.getCurrentUser() != null){
            userID = mAuth.getCurrentUser().getUid();
        }
    }


    public void addNewUser(String email, String username, String description, String website, String profile_photo){

        User user = new User( userID,  1,  email,  username );

//        myRef.child(context.getString(R.string.dbname_users))
//                .child(userID)
//                .setValue(user);


//        UserAccountSettings settings = new UserAccountSettings(
//                description,
//                username,
//                0,
//                0,
//                0,
//                profile_photo,
//                StringManipulation.condenseUsername(username),
//                website,
//                userID
//        );

//        myRef.child(context.getString(R.string.dbname_user_account_settings))
//                .child(userID)
//                .setValue(settings);

    }


    public void registerNewEmail(final String email, String password, final String username)
    {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Toast.makeText(context, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();

                        }
                        else
                            if(task.isSuccessful())
                            {
                                //sendVerificationEmail();      //send verificaton email
                                userID = mAuth.getCurrentUser().getUid();
                                Log.d(TAG, "onComplete: Authstate changed: " + userID);
                            }

                    }
                });
    }

}
