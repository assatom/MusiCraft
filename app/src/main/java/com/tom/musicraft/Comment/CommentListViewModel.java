package com.tom.musicraft.Comment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.tom.musicraft.Models.Comment;
import com.tom.musicraft.Services.FirebaseService;

import java.util.List;

public class CommentListViewModel extends ViewModel
{
    private FirebaseService mFirebaseService;

    public CommentListViewModel() {
        mFirebaseService = FirebaseService.getInstance();
    }

    public LiveData<List<Comment>> getAllCommentsByPostID(String postID)
    {
        return mFirebaseService.getAllCommentsbyPostID(postID);
    }
}
