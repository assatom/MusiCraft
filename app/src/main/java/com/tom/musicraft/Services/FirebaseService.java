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
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.auth.User;
import com.tom.musicraft.Models.Comment;
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
        loadDataFromFirebase();
    }

    List<Comment> mComments = new ArrayList<>();
    List<UserAccountSettings> mUsers = new ArrayList<>();
    private void loadDataFromFirebase() {
        DatabaseReference postsReference = this.mFirebaseDatabase.getReference("Posts");
        postsReference .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    mPostRepository.insert(post);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        DatabaseReference commentsReference = this.mFirebaseDatabase.getReference("Comments");
        commentsReference .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Comment comment= snapshot.getValue(Comment.class);
                    mComments.add(comment);
                   // mPostRepository.insert(comment);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        DatabaseReference usersReference = this.mFirebaseDatabase.getReference("Users");
        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserAccountSettings user = snapshot.getValue(UserAccountSettings.class);
                    mUsers.add(user);
                    // mPostRepository.insert(user);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });


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


    public LiveData<List<Comment>> getAllCommentsbyPostID(String postID) {
        return mPostRepository.getAllCommentsbyPostID(postID);
    }


    public List<Comment> getAllCommentsByPostID(String postID)
    {
        List<Comment> results = new ArrayList<>();
        for (Comment comment: mComments)
        {
            if(comment.getPostId().equals(postID))
                results.add(comment);
        }

        return results;
    }


    public LiveData<List<Post>> getAllPostsByUserId(String id) {
       return mPostRepository.getAllPostsByUserId(id);
    }


    public void updatePost(final Post post){    /// TODO implement

    }

    public void updateComment(final Comment comment){    /// TODO implement

    }

    public void addPost(final Post post){

        mPostRepository.insert(post);       // Local DB

        DatabaseReference usersRef = myRef.child("Posts").child(post.getPostID());
        usersRef.setValue(post);
    }


    public void addComment(final Comment comment){

//        mPostRepository.insert(comment);       // Local DB

        DatabaseReference commentRef = myRef.child("Comments").child(comment.getId());
        commentRef.setValue(comment);
    }

    public void deletePost(String postId){      // TODO implement

    }

    public FirebaseUser getCurrentUser(){      // TODO with room - shuold provide UserAccountSettings object
        return mAuth.getCurrentUser();

    }

    public UserAccountSettings getCurrentUserAccountSettings()
    {
        String currentUserID = getCurrentUser().getUid();
        for (UserAccountSettings user: mUsers)
        {
            if(user.getUser_id().equals(currentUserID))
                return  user;
        }

        return null;
    }

    public void logout()
    {
        mAuth.signOut();
    }

}
