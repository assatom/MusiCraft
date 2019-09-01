package com.tom.musicraft.Profile;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

//import com.bumptech.glide.Glide;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
//import com.rengwuxian.materialedittext.MaterialEditText;
//import com.theartofdev.edmodo.cropper.CropImage;
//import com.theartofdev.edmodo.cropper.CropImageView;
//import com.tom.musicraft.Adapters.CommentsListAdapter;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.tom.musicraft.Adapters.UserAdapter;
import com.tom.musicraft.Models.UserAccountSettings;
import com.tom.musicraft.R;
import com.tom.musicraft.Services.FirebaseService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class EditProfileFragment extends Fragment {

    ImageView image_profile;
    Button save;
    FloatingActionButton choosePicBtn;
    TextView name;
//    MaterialEditText fullname;//, username, bio;

    FirebaseUser firebaseUser;
    private FirebaseService mfirebaseService;
    private UserAdapter userAdapter;
    private List<UserAccountSettings> mUsers;

    private Uri mImageUri;
    private StorageTask uploadTask;
    StorageReference storageRef;
    private Context mContext;
    View view;
    UserAccountSettings user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        mContext = getContext();
        image_profile = view.findViewById(R.id.EditProfile_user_image_view);
        choosePicBtn=view.findViewById(R.id.EditProfile_add_picture_btn);
        save = view.findViewById(R.id.EditProfile_editBtn);
        name = view.findViewById(R.id.EditProfile_nameTextView);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageRef = FirebaseStorage.getInstance().getReference("uploads");
//        userAdapter = new UserAdapter(mContext, mUsers);

        user = FirebaseService.getInstance().getCurrentUserAccountSettings();
        name.setText(user.getUserName());
        Glide.with(mContext.getApplicationContext()).load(user.getProfile_photo()).into(image_profile);

        choosePicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                CropImage.ActivityResult result = CropImage.getActivityResult(getActivity().getIntent());
//                mImageUri = result.getUri();
                selectProfilePicture();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });


        return view;
    }

    private void selectProfilePicture()
    {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setRequestedSize(1024, 1024, CropImageView.RequestSizeOptions.RESIZE_INSIDE)
                .start(mContext,this);
    }

    private void backToHome() {

        BottomNavigationView nav = (BottomNavigationView)getActivity().findViewById(R.id.bottomNavViewBar);
        nav.setSelectedItemId(R.id.ic_house);
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
                        backToHome();

                    } else{
                        Log.d("EditProfileFragment","Failed.");
                       // Toast.makeText(EditProfileActivity.this,"Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("EditProfileFragment","Failed.");
                }
            });
        } else {
            Log.d("EditProfileFragment","Failed.");
            //Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }


    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            mImageUri = result.getUri();
            image_profile.setImageURI(mImageUri );

//            uploadImage();
        }
        else
            Toast.makeText(mContext, "Something gone wrong!", Toast.LENGTH_SHORT).show();
    }

}
