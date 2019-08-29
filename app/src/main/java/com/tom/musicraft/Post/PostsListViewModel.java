package com.tom.musicraft.Post;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.tom.musicraft.Models.Post;
import com.tom.musicraft.Room.PostRepository;
import com.tom.musicraft.Services.FirebaseService;

import java.util.List;

public class PostsListViewModel extends ViewModel
{
    private FirebaseService mFirebaseService;

    public PostsListViewModel() {
        mFirebaseService = FirebaseService.getInstance();
    }

    public LiveData<List<Post>> getAllPosts()
    {
        return mFirebaseService.getAllPosts();
    }
}
