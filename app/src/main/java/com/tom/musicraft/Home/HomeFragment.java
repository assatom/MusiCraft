package com.tom.musicraft.Home;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tom.musicraft.Adapters.CommentsListAdapter;
import com.tom.musicraft.Adapters.PostsListAdapter;
import com.tom.musicraft.Login.RegisterActivity;
import com.tom.musicraft.Models.Comment;
import com.tom.musicraft.Models.Post;
import com.tom.musicraft.Post.PostViewModel;
import com.tom.musicraft.Post.PostsListViewModel;
import com.tom.musicraft.R;
import com.tom.musicraft.Services.FirebaseService;

import java.util.Date;
import java.util.List;
import java.util.Vector;

public class HomeFragment extends Fragment {

    private RecyclerView mListView;
    private Vector<Post> mPostsList = new Vector<>();
    private Context mContext;
    private FirebaseService firebaseService;
    private PostsListViewModel mViewModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        firebaseService = new FirebaseService(mContext);
        mListView = (RecyclerView ) view.findViewById(R.id.posts_list);
        mViewModel = ViewModelProviders.of(this).get(PostsListViewModel.class);

        InitListView();
        return view;
    }

    private void InitListView(){
        mListView.setHasFixedSize(true);
        mListView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        PostsListAdapter adapter = new PostsListAdapter(mPostsList, this.getActivity());
        mListView.setAdapter(adapter);

        mViewModel.getAllPosts().observe(this, new Observer<List<Post>>() {
            @Override
            public void onChanged(@Nullable final List<Post> posts) {
                // Update the cached copy of the words in the adapter.
                adapter.setPosts(posts);
            }
        });

    }
    private ImageButton mSendBtn;
    private RecyclerView mCommentsListView;




}
