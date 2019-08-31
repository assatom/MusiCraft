package com.tom.musicraft.Profile;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.tom.musicraft.Adapters.CommentsListAdapter;
import com.tom.musicraft.Adapters.UserAdapter;
import com.tom.musicraft.Models.UserAccountSettings;
import com.tom.musicraft.R;
import com.tom.musicraft.Services.FirebaseService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EditProfileFragment extends Fragment {

    ImageView close, image_profile;
    TextView save, pp_change;
    MaterialEditText fullname;//, username, bio;

    FirebaseUser firebaseUser;
    private FirebaseService mfirebaseService;
    private UserAdapter userAdapter;
    private List<UserAccountSettings> mUsers;

    private Uri mImageUri;
    private StorageTask uploadTask;
    StorageReference storageRef;
    private Context mContext;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        mContext = getContext();
        close = view.findViewById(R.id.close);
        image_profile = view.findViewById(R.id.image_profile);
        save = view.findViewById(R.id.save);
        pp_change = view.findViewById(R.id.pp_change);
        fullname = view.findViewById(R.id.fullname);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageRef = FirebaseStorage.getInstance().getReference("uploads");
        DatabaseReference reference = FirebaseService.getInstance().getFirebaseDatabase().getReference("Users").child(firebaseUser.getUid());
        userAdapter = new UserAdapter(mContext, mUsers);

        readUsers();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserAccountSettings user = dataSnapshot.getValue(UserAccountSettings.class);
                fullname.setText(user.getUserName());
                Glide.with(mContext.getApplicationContext()).load(user.getProfile_photo()).into(image_profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToUserProfile();
            }
        });

        pp_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity().setAspectRatio(1,1)
                        .setCropShape(CropImageView.CropShape.OVAL);
                backToUserProfile();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile(fullname.getText().toString());

                CropImage.ActivityResult result = CropImage.getActivityResult(getActivity().getIntent());
                mImageUri = result.getUri();

                uploadImage();
            }
        });

        return view;
    }

    private UserAccountSettings getCurrentUser() {
        firebaseUser = mfirebaseService.getCurrentUser();
        UserAccountSettings user = null;
        for(UserAccountSettings _user : mUsers){
            if(_user.getUser_id() == mfirebaseService.getCurrentUser().getUid())
                user = _user;
        }

        return user;
    }

    private void backToUserProfile() {
        UserAccountSettings user = getCurrentUser();

        try {
            ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ProfileFragment(user)).commit();
        }
        catch (Exception ex){
            Log.d("EditProfileFragment", "Fail to get user, close failed.");
        }
    }

    private void updateProfile(String fullname) {
        DatabaseReference reference = FirebaseService.getInstance().getFirebaseDatabase().getReference("Users").child(firebaseUser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userName", fullname);

        reference.updateChildren(hashMap);
    }

    private String getFileExtention(Uri uri){
        ContentResolver contentResolver = mContext.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() {
        final ProgressDialog pd = new ProgressDialog(mContext);
        pd.setMessage("Uploading");
        pd.show();

        if(mImageUri != null){
            final StorageReference filereference = storageRef.child(System.currentTimeMillis()
                    + "." + getFileExtention(mImageUri));

            uploadTask = filereference.putFile(mImageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return filereference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String MyUrl = downloadUri.toString();

                        DatabaseReference reference = FirebaseService.getInstance().getFirebaseDatabase().getReference("Users").child(firebaseUser.getUid());
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("profile_photo", MyUrl);

                        reference.updateChildren(hashMap);
                        pd.dismiss();
                    } else{
                        Log.d("EditProfileFragment","Failed.");
                       // Toast.makeText(EditProfileActivity.this,"Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("EditProfileFragment","Failed.");
                    //Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Log.d("EditProfileFragment","Failed.");
            //Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void readUsers() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserAccountSettings user = snapshot.getValue(UserAccountSettings.class);
                    mUsers.add(user);
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
