package com.tom.musicraft.Post;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tom.musicraft.Adapters.CommentsListAdapter;
import com.tom.musicraft.Comment.CommentListViewModel;
import com.tom.musicraft.Models.Comment;
import com.tom.musicraft.R;
import com.tom.musicraft.Services.FirebaseService;

import java.util.Date;
import java.util.List;
import java.util.Vector;

public class CommentFragment extends Fragment {

    private CommentsListAdapter mAdapter;
    private List<Comment> mComments = new Vector<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private String mPostId;

   private ImageButton mSendBtn;
   private EditText mCommentText;
   private CommentListViewModel viewModel;
   private ProgressDialog mProgressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_comments, container, false);

        mProgressDialog = new ProgressDialog(getActivity());
        viewModel= ViewModelProviders.of(this).get(CommentListViewModel.class);
      //  mPostId =  CommentsFragmentArgs.fromBundle(getArguments()).getPostId();       // TODO

        mRecyclerView = view.findViewById(R.id.comments_list_rv);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new CommentsListAdapter(mComments);
        mRecyclerView.setAdapter(mAdapter);

        mSendBtn = view.findViewById(R.id.comments_send_btn);
        mCommentText = view.findViewById(R.id.comments_edit_text);


        // TODO load comments by post id => this.mComments
        mAdapter = new CommentsListAdapter( this.mComments);
        addButtonsClickListeners(view);

//        viewModel.SetPostId(mPostId,this.getViewLifecycleOwner(),post -> {
//            viewModel.observeCommentsList(getViewLifecycleOwner(), comments ->{
//                if (comments != null) {
//                    this.mComments.clear();
//                    this.mComments.addAll(comments);
//                    if (mAdapter == null) {
//                        mAdapter = new CommentsListAdapter( this.mComments);
//                        mRecyclerView.setAdapter(mAdapter);
//                    } else {
//                        mRecyclerView.setAdapter(mAdapter);
//                        mAdapter.notifyDataSetChanged();
//                    }
//                    addButtonsClickListeners(view);
//                }
//            });
//            viewModel.init(getViewLifecycleOwner());
//        });

        return view;
    }

    private void addButtonsClickListeners(View view) {
        mAdapter.setOnDeleteClickListener(new CommentsListAdapter.OnDeleteClickListener() {
            @Override
            public void onClick(int index) {
                mProgressDialog.setTitle("Deleting comment");
                mProgressDialog.show();
                Log.d("TAG","item click: " + index);
//                final Comment comment = CommentsListAdapter.mData.get(index);
//                viewModel.deleteComment(comment.getComment(), new Dao.DeleteCommentListener() {
//                    @Override
//                    public void onComplete(Void avoid) {
//                        mProgressDialog.hide();
//                        Log.d("TAG","deleted comment id: " + comment.getComment().getId());
//                        mComments.remove(comment);
//                        mAdapter.notifyDataSetChanged();
//                        Toast.makeText(getActivity(), "Comment deleted successfully!", Toast.LENGTH_SHORT).show();
//                    }
//                });
            }
        });

//        mAdapter.setOnEditClickListener(new CommentsListAdapter.OnEditClickListener() {
//            @Override
//            public void onClick(int index) {
//                Log.d("TAG","item click: " + index);
//                //Navigation.findNavController(view).navigate(R.id.action_cardsListFragment_to_cardDetailsFragment);
//                Comment comment = CommentsListAdapter.mCommentsList.get(index);
//
//
//                CommentsFragmentDirections.ActionCommentsFragmentToEditCommentFragment action =
//                        CommentsFragmentDirections.actionCommentsFragmentToEditCommentFragment(comment.getComment().getId(), mPostId);
//                Navigation.findNavController(view).navigate(action);
//            }
//        });

        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog.setTitle("Adding comment..");
                mProgressDialog.show();
                Log.d("Tag", "add comment clicked!");
                Date date = new Date();


                Comment comment = new Comment("text", "1/1/2019", "UserID", "UserName", mPostId);

                FirebaseService.getInstance().addComment(comment);
//                comment.setLastUpdate(DateTimeUtils.getTimestampFromLong(date.getTime()));

//                viewModel.addComment(comment, new Dao.AddCommentListener() {
//                    @Override
//                    public void onComplete(Comment comment) {
//                        mProgressDialog.hide();
//                        Common.hideKeyboard(CommentsFragment.this);
//                        mCommentText.setText("");
//                        Common.scrollToBottom(mRecyclerView);
//                    }
//                });
            }
        });
    }

}
