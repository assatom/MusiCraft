package com.tom.musicraft.Services;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.Dao;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.ListenerRegistration;
import com.tom.musicraft.Interfaces.IFirebaseListener;
import com.tom.musicraft.Models.Post;
import com.tom.musicraft.Models.User;
import com.tom.musicraft.Models.UserAccountSettings;
import com.tom.musicraft.Models.UserSettings;
import com.tom.musicraft.R;
import com.tom.musicraft.Utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FirebaseService
{
    private static final String TAG = "FirebaseService";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private String userID;
    private Context context;
    CollectionReference userRef;
    CollectionReference postRef;
    CollectionReference commentRef;
    private ListenerRegistration listenerRegistration;


    public FirebaseService(Context context)
    {
        this.context = context;
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false).build();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        userRef=db.collection("Users");
        postRef=db.collection("Posts");
        commentRef=db.collection("Comments");
//        mStorageReference = FirebaseStorage.getInstance().getReference();

        if(mAuth.getCurrentUser() != null){
            userID = mAuth.getCurrentUser().getUid();
        }
    }


    public void addNewUser(String email, String username){

    UserAccountSettings user = new UserAccountSettings(
            0,
            0,
            email,
            userID,
            username
    );

    DatabaseReference usersRef = myRef.child("Users").child(userID);
    usersRef.setValue(user);
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
                                userID = mAuth.getCurrentUser().getUid();
                                addNewUser(email, username);
                                Log.d(TAG, "onComplete: Authstate changed: " + userID);
                            }

                    }
                });
    }

    /**
     * Retrieves the account settings for teh user currently logged in
     * Database: user_acount_Settings node
     * @param dataSnapshot
     * @return
     */
    public UserSettings getUserSettings(DataSnapshot dataSnapshot){
        Log.d(TAG, "getUserSettings: retrieving user account settings from firebase.");


        UserAccountSettings settings  = new UserAccountSettings();
        User user = new User();

        for(DataSnapshot ds: dataSnapshot.getChildren()){

            // user_account_settings node
            if(ds.getKey().equals(context.getString(R.string.dbname_user_account_settings))) {
                Log.d(TAG, "getUserSettings: user account settings node datasnapshot: " + ds);

                try {
                    settings.setEmail(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getEmail()
                    );
                    settings.setFollowing(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getFollowing()
                    );
                    settings.setFollowers(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getFollowers()
                    );

                    Log.d(TAG, "getUserAccountSettings: retrieved user_account_settings information: " + settings.toString());
                } catch (NullPointerException e) {
                    Log.e(TAG, "getUserAccountSettings: NullPointerException: " + e.getMessage());
                }
            }


            // users node
            Log.d(TAG, "getUserSettings: snapshot key: " + ds.getKey());
            if(ds.getKey().equals(context.getString(R.string.dbname_users))) {
                Log.d(TAG, "getUserAccountSettings: users node datasnapshot: " + ds);

                user.setUsername(
                        ds.child(userID)
                                .getValue(User.class)
                                .getUsername()
                );
                user.setEmail(
                        ds.child(userID)
                                .getValue(User.class)
                                .getEmail()
                );
                user.setPhone_number(
                        ds.child(userID)
                                .getValue(User.class)
                                .getPhone_number()
                );
                user.setUser_id(
                        ds.child(userID)
                                .getValue(User.class)
                                .getUser_id()
                );

                Log.d(TAG, "getUserAccountSettings: retrieved users information: " + user.toString());
            }
        }
        return new UserSettings(user, settings);

    }

    public void updatePostsListener(long from, IFirebaseListener listener) {
        listenerRegistration.remove();
        getAllPosts(DateTimeUtils.getTimestampFromLong(from), listener);
    }

    public void getAllPosts(long updateFrom,final IFirebaseListener listener) {
        Timestamp timeStamp;
        if(updateFrom==0) {
            timeStamp = DateTimeUtils.getTimeStamp(2019, 1, 1);
            Log.d("Tag", "getAllPosts if: timeStamp = " + timeStamp);
        }
        else{
            timeStamp= DateTimeUtils.getTimestampFromLong(updateFrom);
            Log.d("Tag", "getAllPosts else: timeStamp = " + timeStamp);
        }

        getAllPosts(timeStamp,listener);
    }

    private  void getAllPosts(Timestamp from, IFirebaseListener listener)
    {
        /*listenerRegistration = postRef.whereGreaterThan("lastUpdate", from).addSnapshotListener((snapshot, e) -> {
            if (e != null) {
                return;
            }
            if (snapshot != null && !snapshot.isEmpty()) {
                List<Post> posts= new ArrayList<>();
                snapshot.getDocumentChanges().get(0).getDocument().toObject(Post.class);
                for (DocumentChange docChange : snapshot.getDocumentChanges()) {
                    posts.add(docChange.getDocument().toObject(Post.class));
                }
                Log.d("Tag", "posts size in observer = " + posts.size());
                posts= snapshot.toObjects(Post.class);
                listener.updatePosts(posts);
            }
        });*/
    }

    public void updatePost(final Post post){//, final Dao.UpdatePostListener listener){
      /*  postRef.document(post.getId()).set(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.onComplete(post);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Tag", "onFailure: updatePost " + post.getId() + " failed");
                listener.onComplete(null);
            }
        });*/
    }

    public void addPost(final Post post){//, final Dao.AddPostListener listener) {

        String id = postRef.document().getId();
        post.setId(id);
        //postRef.document(id).set(post);

        DatabaseReference usersRef = myRef.child("Posts").child(id);
        usersRef.setValue(post);
    }

    public void deletePost(String postId){//, final Dao.DeletePostListener listener) {
        try {
            postRef.document(postId).delete();
            Log.d(TAG, "onComplete: deletePost: " + postId);
        }
        catch (Exception ex) {
            Log.d(TAG, "Exception: delete post: " + postId);
        }
    }


    public FirebaseUser getCurrentUser(){
        return mAuth.getCurrentUser();

    }

}
