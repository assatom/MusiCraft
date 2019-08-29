package com.tom.musicraft.Adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tom.musicraft.Models.Post;
import com.tom.musicraft.R;

import java.util.List;

// Reference:   https://www.youtube.com/watch?v=bSMZknDI6bg


public class PostsListAdapter extends RecyclerView.Adapter<PostsListAdapter.PostRowViewHolder>
{
    public static List<Post> mVideoList;
    OnItemClickListener mListener;
    OnEditClickListener mEditListener;
    OnDeleteClickListener mDeleteListener;

    public PostsListAdapter(List<Post> videoList) {
        mVideoList= videoList;
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
        if(mVideoList.get(i).getUser() != null)     // For debugging
            postRowViewHolder.mDisplayName.setText(mVideoList.get(i).getUser().getUsername());
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
        ImageButton mComment;
        ImageButton mEdit;
        ImageButton mDelete;
        View mView;
        WebView mWebView;


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


