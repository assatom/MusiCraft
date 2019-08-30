package com.tom.musicraft.Room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.tom.musicraft.Models.Post;

import java.util.List;

@Dao
public interface PostDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Post post);

    @Query("DELETE FROM Posts")
    void deleteAll();

///   @Query("SELECT * from post_table INNER JOIN user_table ON mAuthorId = user_id")
    @Query("SELECT * from Posts")
    LiveData<List<Post>> getAllPosts();

    @Query("SELECT * from Posts WHERE post_ID=:postId")
    LiveData<Post> getPostById(String postId);

    @Query("SELECT * from Posts Where userID = :i_userID")
    LiveData<List<Post>> getAllPostsByUserId(String i_userID);

    @Query("DELETE FROM Posts WHERE post_ID=:postId")
    void delete(String postId);
}
