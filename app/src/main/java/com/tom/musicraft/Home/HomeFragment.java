package com.tom.musicraft.Home;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tom.musicraft.Adapters.PostsListAdapter;
import com.tom.musicraft.Models.Post;
import com.tom.musicraft.R;

import java.util.Vector;

public class HomeFragment extends Fragment {

    private RecyclerView mListView;
    private Vector<Post> mPostsList = new Vector<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mListView = (RecyclerView ) view.findViewById(R.id.posts_list);
        InitListView();
        return view;
    }

    private void InitListView(){
        mListView.setHasFixedSize(true);
        mListView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
//        mListView.setLayoutManager(new LinearLayoutManager(getContext()));

        /// TODO get posts from firebase !!!
        String url = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/Qopx44kAmdE\" frameborder=\"0\" allowfullscreen></iframe>";
//        url = "<html><body><iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/bSMZknDI6bg\" frameborder=\"0\" allowfullscreen></iframe></body></html>";

        mPostsList.add(new Post("Some text", url , "01/01/2019",  null));
        mPostsList.add(new Post("Some text", url , "02/01/2019",  null));

        PostsListAdapter adapter = new PostsListAdapter(mPostsList);
        mListView.setAdapter(adapter);



//        mListView.setHorizontalFadingEdgeEnabled(true);
//        mListView.setAdapter(adapter);
//        mListView.enableLoadFooter(true)
//                .getLoadFooter().setLoadAction(LoadFooter.LoadAction.RELEASE_TO_LOAD);
//        mListView.setOnUpdateListener(this)
//                .setOnLoadListener(this);
//        mListView.requestUpdate();
    }



}
