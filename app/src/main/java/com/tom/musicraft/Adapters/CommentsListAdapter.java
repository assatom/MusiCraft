package com.tom.musicraft.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.auth.User;
import com.tom.musicraft.Models.Comment;
import com.tom.musicraft.Models.UserAccountSettings;
import com.tom.musicraft.R;
import com.tom.musicraft.Services.FirebaseService;

import java.util.List;

public class CommentsListAdapter extends RecyclerView.Adapter<CommentsListAdapter.CommentRowViewHolder>{
    public static List<Comment> mCommentsList;
    OnItemClickListener mListener;
    OnEditClickListener mEditListener;
    OnDeleteClickListener mDeleteListener;

    public CommentsListAdapter(List<Comment> data) {
        mCommentsList = data;
    }
    public CommentsListAdapter(){}
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
    public CommentRowViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.comment_row, viewGroup,false);
        CommentRowViewHolder viewHolder = new CommentRowViewHolder(view, mListener, mEditListener, mDeleteListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentRowViewHolder commentRowViewHolder, int i) {

        Comment comment = mCommentsList.get(i);
        // TODO : set name - search in db the userName by the userID from comment
        commentRowViewHolder.mName.setText(comment.getUserName());
        // TODO : Check if we need the creation date or the last update
        commentRowViewHolder.mDate.setText(comment.getCreationDate());
        commentRowViewHolder.mText.setText(comment.getText());
        // TODO : Set image to user
        //commentRowViewHolder.mUserImage.setImageBitmap(comment.getmText());


        //commentRowViewHolder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return mCommentsList.size();
    }

    public void setPosts(List<Comment> comments){
        mCommentsList = comments;
        notifyDataSetChanged();
    }

    class CommentRowViewHolder extends RecyclerView.ViewHolder {
        ImageView mUserImage;
        TextView mName;
        TextView mDate;
        TextView mText;
        ImageButton mEdit;
        ImageButton mDelete;
        ImageButton mSend;
        View mView;

        public CommentRowViewHolder(@NonNull final View itemView,
                                    final OnItemClickListener listener,
                                    final OnEditClickListener editListener,
                                    final OnDeleteClickListener deleteListener) {
            super(itemView);
            mUserImage = itemView.findViewById(R.id.comment_user_img);
            mName = itemView.findViewById(R.id.comment_row_user_name_tv);
            mDate = itemView.findViewById(R.id.comment_row_date_tv);
            mText = itemView.findViewById(R.id.comment_row_text_tv);
            mEdit = itemView.findViewById(R.id.comment_row_edit_bt);
            mDelete = itemView.findViewById(R.id.comment_row_delete_bt);
            mSend = itemView.findViewById(R.id.comments_send_btn);

            mView = itemView;

//            mSend.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Log.d("Adasdadad","asas");
//                }
//            });


//            mDelete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int index = getAdapterPosition();
//                    if (deleteListener != null){
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
//                    if (editListener != null){
//                        if (index != RecyclerView.NO_POSITION) {
//                            editListener.onClick(index);
//                        }
//                    }
//                }
//            });
        }

      //  public void bind(Comment _comment){
       //    Log.d("TAG", "bind: mText = " + mText);

       //    Comment comment = _comment;//commentAndUser.getComment();

       //    // TODO : Search user by comment.getUserID();
       //    //User user =

       //    UserAccountSettings userAccountSettings = null;
       //    //User user = commentAndUser.getUser();

       //    if (userAccountSettings.getUser_id().equals(Dao.instance.getCurrentUserId())){
       //        // visible remove and edit buttons
       //        mEdit.setVisibility(View.VISIBLE);
       //        mDelete.setVisibility(View.VISIBLE);
       //    }
       //    else {
       //        mEdit.setVisibility(View.GONE);
       //        mDelete.setVisibility(View.GONE);
       //    }

       //    mText.setText(comment.getText());
       //    mDate.setText(comment.getCreationDate());
       //    mName.setText(user.getName());
       //    if (user.getImageUri() != null)
       //        Picasso.with(itemView.getContext()).load(user.getImageUri()).fit().into(mUserImage);
       //    else
       //        mUserImage.setImageResource(R.drawable.user_default_image);
      //  }
    }
}
