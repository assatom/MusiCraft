package com.tom.musicraft.Models;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Posts")
public class Post
{
    @PrimaryKey     //(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "post_ID")
    private String postID;

    private String text;
    private String videoUrl;
    private String date;
    private String userID;
    private String userName;


    public Post(){
        // Need empty ctor for deserialization from DB
    }

    public Post(String text, String videoUrl, String date, String userID, String userName) {
        this.text = text;
        this.date = date;
        this.videoUrl = videoUrl;
        this.userID = userID;
        this.userName = userName;
        this.postID = java.util.UUID.randomUUID().toString();
    }

    @NonNull
    public String getPostID() {
        return postID;
    }

    public void setPostID(@NonNull String postID) {
        this.postID = postID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    //    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
