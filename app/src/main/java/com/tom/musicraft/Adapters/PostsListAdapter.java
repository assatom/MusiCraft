package com.tom.musicraft.Adapters;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tom.musicraft.Home.HomeFragment;
import com.tom.musicraft.Models.Comment;
import com.tom.musicraft.Models.Post;
import com.tom.musicraft.Post.CommentFragment;
import com.tom.musicraft.R;
import com.tom.musicraft.Services.FirebaseService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

// Reference:   https://www.youtube.com/watch?v=bSMZknDI6bg


public class PostsListAdapter extends RecyclerView.Adapter<PostsListAdapter.PostRowViewHolder>
{
    public static List<Post> mVideoList;
    private FragmentActivity fragmentActivity;
    OnItemClickListener mListener;
    OnEditClickListener mEditListener;
    OnDeleteClickListener mDeleteListener;

    public PostsListAdapter(List<Post> videoList) {
        mVideoList= videoList;
    }
    public PostsListAdapter(List<Post> videoList,FragmentActivity fragmentActivity) {
        mVideoList= videoList;
        this.fragmentActivity = fragmentActivity;
    }

    public interface OnItemClickListener{
        void onClick(int index);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public interface OnEditClickListener{
        void onClick(int index);
    }
    public void setOnEditClickListener(OnEditClickListener listener){
        mEditListener = listener;
    }

    public interface OnDeleteClickListener{
        void onClick(int index);
    }
    public void setOnDeleteClickListener(OnDeleteClickListener listener){
        mDeleteListener = listener;
    }

    @NonNull
    @Override
    public PostRowViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fragment_post, viewGroup,false);

        PostRowViewHolder viewHolder = new PostRowViewHolder(view, mListener, mEditListener, mDeleteListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostRowViewHolder postRowViewHolder, int i) {

        postRowViewHolder.mWebView.loadData(mVideoList.get(i).getVideoUrl(), "text/html", "UTF-8");
        postRowViewHolder.mDate.setText(mVideoList.get(i).getDate());
        postRowViewHolder.mText.setText(mVideoList.get(i).getText());
        postRowViewHolder.mDisplayName.setText(mVideoList.get(i).getUserName());
        postRowViewHolder.mPostId = mVideoList.get(i).getPostID();
        if(FirebaseService.getInstance().getCurrentUserAccountSettings() != null) {
            postRowViewHolder.mUserId = FirebaseService.getInstance().getCurrentUserAccountSettings().getUser_id();
            postRowViewHolder.mUserName = FirebaseService.getInstance().getCurrentUserAccountSettings().getUserName();
        }
        if(fragmentActivity != null)
            postRowViewHolder.mFragmentActivity = fragmentActivity;
    }

    @Override
    public int getItemCount() {
        return mVideoList.size();
    }

    public void setPosts(List<Post> posts){
        mVideoList = posts;
        notifyDataSetChanged();
    }


    // View holder
    class PostRowViewHolder extends RecyclerView.ViewHolder {
        ImageView mUserImage;
        TextView mDisplayName;
        TextView mDate;
        TextView mText;
        CheckBox mLikeCb;
        ImageButton mEdit;
        ImageButton mDelete;
        View mView;
        WebView mWebView;
        String mPostId;
        String mUserId;
        String mUserName;

        // Comment
        private ImageButton mSendBtn;
        private RecyclerView mCommentsListView;
        private FragmentActivity mFragmentActivity;
        private TextView mCommentText;

        void Refresh()
        {
            BottomNavigationView nav =  mFragmentActivity.findViewById(R.id.bottomNavViewBar);
            nav.setSelectedItemId(R.id.ic_house);
        }


        public PostRowViewHolder(@NonNull final View itemView,
                                 final OnItemClickListener listener,
                                 final OnEditClickListener editListener,
                                 final OnDeleteClickListener deleteListener) {
            super(itemView);

            //   mUserImage = itemView.findViewById(R.id.post_user_img);
            mDate = itemView.findViewById(R.id.post_date);
            mText = itemView.findViewById(R.id.post_text);
            mDisplayName = itemView.findViewById(R.id.post_display_name);
            mWebView = itemView.findViewById(R.id.post_youtube);

            mWebView.getSettings().setDomStorageEnabled(true);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.getSettings().setAppCacheEnabled(true);
            mWebView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
            mWebView.setWebChromeClient(new WebChromeClient());
            mView = itemView;

            // Comments
            mCommentText = itemView.findViewById((R.id.comments_edit_text));
            mSendBtn = itemView.findViewById(R.id.comments_send_btn);
            mCommentsListView = itemView.findViewById(R.id.comments_list_rv);
            mCommentsListView.setLayoutManager(new LinearLayoutManager(mFragmentActivity));

            List<Comment> commentList = new ArrayList<>(FirebaseService.getInstance().getAllCommentsByPostID(mPostId));

            CommentsListAdapter adapter = new CommentsListAdapter(commentList);
//            mCommentsListView.setAdapter(adapter);
//            adapter.notifyDataSetChanged();

            Query query = FirebaseDatabase.getInstance().getReference("Comments");
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Comment comment = snapshot.getValue(Comment.class);
                        if(comment.getPostId().equals(mPostId))
                            commentList.add(comment);
                    }
                    adapter.notifyDataSetChanged();
                    mCommentsListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            mSendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Tag", "add comment clicked!");

                    if(mCommentText.getText().toString().isEmpty())
                    {
                        Toast.makeText(mFragmentActivity.getBaseContext(),"You must fill out all the fields", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String pattern = "MM/dd/yyyy HH:mm:ss";
                    DateFormat df = new SimpleDateFormat(pattern);
                    Date today = Calendar.getInstance().getTime();
                    String todayAsString = df.format(today);


                    Comment comment = new Comment(
                            mCommentText.getText().toString(),
                            todayAsString,
                            mUserId,
                            mUserName,
                            mPostId);

                    commentList.add(comment);
                    adapter.notifyDataSetChanged();

                    Refresh();

                    FirebaseService.getInstance().addComment(comment);
                }});

//            FirebaseService.getInstance().getAllCommentsbyPostID(mPostId).observe(itemView, new Observer<List<Comment>>() {
//                @Override
//                public void onChanged(@Nullable final List<Comment> comments) {
//                    // Update the cached copy of the words in the adapter.
//                    adapter.setPosts(comments);
//                }
//            });


//            mLikeCb = itemView.findViewById(R.id.post_row_like_cb);
//            mComment = itemView.findViewById(R.id.post_row_comment_bt);
//            mEdit = itemView.findViewById(R.id.post_row_edit_bt);
//            mDelete = itemView.findViewById(R.id.post_row_delete_bt);

//            mDelete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int index = getAdapterPosition();
//                    if (deleteListener != null) {
//                        if (index != RecyclerView.NO_POSITION) {
//                            deleteListener.onClick(index);
//                        }
//                    }
//                }
//            });
//
//            mEdit.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int index = getAdapterPosition();
//                    if (editListener != null) {
//                        if (index != RecyclerView.NO_POSITION) {
//                            editListener.onClick(index);
//                        }
//                    }
//                }
//            });
//
//            mComment.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int index = getAdapterPosition();
//                    if (listener != null) {
//                        if (index != RecyclerView.NO_POSITION) {
//                            listener.onClick(index);
//                        }
//                    }
//                }
//            });
        }
    }

}


