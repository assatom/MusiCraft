package com.tom.musicraft.Room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.tom.musicraft.Models.Comment;

import java.util.List;

@Dao
public interface CommentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Comment comment);

    @Query("DELETE FROM Comments")
    void deleteAll();

    @Query("SELECT * from Comments " +
            " INNER JOIN Users ON mUserId=user_ID WHERE mPostId=:postId")
    LiveData<List<Comment>> getAllCommentByPostId(String postId);

    @Query("DELETE FROM Comments WHERE comment_ID=:commentId")
    void delete(String commentId);


    @Query("SELECT * from Comments" +
            " INNER JOIN Users ON mUserId=user_ID WHERE comment_ID=:commentId")
    LiveData<Comment> getCommentById(String commentId);
}
