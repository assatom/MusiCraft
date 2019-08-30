package com.tom.musicraft.Adapters;


import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.tom.musicraft.Models.UserAccountSettings;
import com.tom.musicraft.Profile.ProfileFragment;
import com.tom.musicraft.R;
import com.tom.musicraft.Services.FirebaseService;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.viewHolder>{

    private Context mContext;
    private List<UserAccountSettings> mUsers;
    private FirebaseUser firebaseUser;
    private FirebaseService mfirebaseService;

    public UserAdapter(Context mContext, List<UserAccountSettings> mUsers) {
        this.mContext = mContext;
        this.mUsers = mUsers;
        mfirebaseService = new FirebaseService(mContext);
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_user, parent, false);
        return new UserAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewHolder holder, int position) {
        firebaseUser = mfirebaseService.getCurrentUser();
        final UserAccountSettings user = mUsers.get(position);

        holder.username.setText(user.getUserName());
        /// TODO profile image
//        Glide.with(mContext).load(user.getImageurl()).into(holder.image_profile);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
//                editor.putString("profileid", user.getUser_id());
//                editor.apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment(user)).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public CircleImageView image_profile;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            image_profile = itemView.findViewById(R.id.image_profile);      /// TODO
        }
    }
}
