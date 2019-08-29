package com.tom.musicraft.Post;

import android.net.Uri;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.tom.musicraft.Models.Post;
import com.tom.musicraft.Room.PostRepository;
import com.tom.musicraft.Services.FirebaseService;

public class PostViewModel extends ViewModel {

    private MutableLiveData<Post> mPostLiveData = new MutableLiveData<>();

    private FirebaseService mFirebaseService;

    public PostViewModel()
    {
        mFirebaseService = FirebaseService.getInstance();
    }

    public void setPostId(String postId, LifecycleOwner lifecycleOwner, Observer<Post> observer) {
        mPostLiveData.observe(lifecycleOwner,observer);
//        Dao.instance.observePostLiveData(lifecycleOwner,postId, post -> mPostLiveData.postValue(post));
        /// TODO check what is this?
    }

    public void uploadPost(Post post){
        mFirebaseService.addPost(post);
    }

    public void updatePost(Post post){
        mFirebaseService.updatePost(post);
    }

//    public void insert(Word word) { mRepository.insert(word); }
}
