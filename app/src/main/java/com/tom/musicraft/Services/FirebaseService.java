package com.tom.musicraft.Services;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
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
import com.tom.musicraft.Models.Post;
import com.tom.musicraft.Models.UserAccountSettings;
import com.tom.musicraft.R;
import com.tom.musicraft.Room.PostRepository;
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




    private PostRepository mPostRepository;
    private static FirebaseService Instance;

    public static FirebaseService getInstance()
    {
        if(Instance == null)
            Instance = new FirebaseService(null);

        return Instance;
    }

    public void init(Application application)
    {
        mPostRepository = new PostRepository(application);
    }


    // TODO 1) remove context (consider where we need it)
    // TODO 2) use FirebaseService.getInstance in whole application
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


    public LiveData<List<Post>> getAllPosts() {
        return mPostRepository.getAllPosts();
    }

    public LiveData<List<Post>> getAllPostsByUserId(String id) {
       return mPostRepository.getAllPostsByUserId(id);
    }

    private  void getAllPosts(Timestamp from) // TODO implement
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

    public void updatePost(final Post post){    /// TODO implement

    }

    public void addPost(final Post post){

        mPostRepository.insert(post);       // Local DB

        DatabaseReference usersRef = myRef.child("Posts").child(post.getPostID());
        usersRef.setValue(post);
    }

    public void deletePost(String postId){      // TODO implement

    }

    public FirebaseUser getCurrentUser(){
        return mAuth.getCurrentUser();

    }

    public void logout()
    {
        mAuth.signOut();
    }

}
