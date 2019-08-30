package com.tom.musicraft.Comment;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.tom.musicraft.Models.Comment;
import com.tom.musicraft.Models.Post;
import com.tom.musicraft.Services.FirebaseService;

public class CommentViewModel extends ViewModel {

    private MutableLiveData<Comment> mCommentLiveData = new MutableLiveData<>();

    private FirebaseService mFirebaseService;

    public CommentViewModel()
    {
        mFirebaseService = FirebaseService.getInstance();
    }

    public void setCommentId(String commentId, LifecycleOwner lifecycleOwner, Observer<Comment> observer) {
        mCommentLiveData.observe(lifecycleOwner,observer);
//        Dao.instance.observePostLiveData(lifecycleOwner,postId, post -> mPostLiveData.postValue(post));
        /// TODO check what is this?
    }

    public void uploadComment(Comment comment){
        mFirebaseService.addComment(comment);
    }

    public void updateComment(Comment comment){
        mFirebaseService.updateComment(comment);
    }
}
