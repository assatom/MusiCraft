package com.tom.musicraft.Room;


import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.tom.musicraft.Models.Comment;
import com.tom.musicraft.Models.Post;

import java.util.List;

public class PostRepository {

    private PostDao mPostDao;
//    private UserDao mUserDao;
    private CommentDao mCommentDao;
    private LiveData<List<Post>> mAllPosts;
//    private LiveData<List<User>> mAllUsers;

    public PostRepository(Application application) {
        PostRoomDatabase db = PostRoomDatabase.getDatabase(application);
        mPostDao= db.postDao();
        mCommentDao = db.commentsDao();
//        mUserDao= db.userDao();
//        mAllUsers = mUserDao.getAllUsers();
        mAllPosts = mPostDao.getAllPosts();

    }

    public LiveData<List<Post>> getAllPosts() {
        return mAllPosts;
    }
//    public LiveData<List<User>> getAllUsers() {
//        return mAllUsers;
//    }

    public LiveData<List<Post>> getAllPostsByUserId(String userId){
        return mPostDao.getAllPostsByUserId(userId);
    }

    public LiveData<Post> getPostById(String  postId){
        return mPostDao.getPostById(postId);
    }

//    public LiveData<CommentAndUser> getCommentById(String commentId){
//        return mCommentDao.getCommentById(commentId);
//    }
//
//    public LiveData<List<CommentAndUser>> getAllComments(String postId){
//        return mCommentDao.getAllCommentByPostId(postId);
//    }
//    public LiveData<User> getUserById(String userId)
//    {
//        return  mUserDao.getUserById(userId);
//    }

    public void insert(Post post) {
        new insertPostAsyncTask(mPostDao).execute(post);

    }
//    public void insert(User user) {
//        new insertUserAsyncTask(mUserDao).execute(user);
//    }
    public void insert(Comment comment) {
        new insertCommandAsyncTask(mCommentDao).execute(comment);
    }

    public void deletePost(String postId) {
        new deletePostAsyncTask(mPostDao).execute(postId);
    }

    public void deleteComment(String commentId){
        new deleteCommentAsyncTask(mCommentDao).execute(commentId);
    }

    public LiveData<List<Comment>> getAllCommentsbyPostID(String postId) {
        return mCommentDao.getAllCommentsByPostId(postId);

    }

    private static class insertPostAsyncTask extends AsyncTask<Post, Void, Void> {

        private PostDao mAsyncTaskDao;

        public insertPostAsyncTask(PostDao  dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Post... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deletePostAsyncTask extends AsyncTask<String, Void, Void> {

        private PostDao mAsyncTaskDao;

        public deletePostAsyncTask(PostDao  dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final String... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }


    private static class insertCommandAsyncTask extends AsyncTask<Comment, Void, Void> {

        private CommentDao mAsyncTaskDao;

        insertCommandAsyncTask (CommentDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Comment... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteCommentAsyncTask extends AsyncTask<String, Void, Void> {

        private CommentDao mAsyncTaskDao;

        public deleteCommentAsyncTask(CommentDao  dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final String... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }


//    private static class insertUserAsyncTask extends AsyncTask<User, Void, Void> {
//
//        private UserDao mAsyncTaskDao;
//
//        public insertUserAsyncTask (UserDao  dao) {
//            mAsyncTaskDao = dao;
//        }
//
//        @Override
//        protected Void doInBackground(final User... params) {
//            mAsyncTaskDao.insertUser(params[0]);
//            return null;
//        }
//
//    }

}
