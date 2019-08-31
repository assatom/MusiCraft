package com.tom.musicraft.Models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;

@Entity(tableName = "Comments")
public class Comment {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "comment_ID")
    private String Id;
    private String UserId;
    private String Text; // actual comment text
    private String CreationDate;
    private String lastUpdate;
    private String PostId;
    private String UserName;


    public Comment(){
        // Need empty ctor for deserialization from DB
    }

    public Comment(String text, String creationDate, String userId, String userName, String postId){
        this.Id = java.util.UUID.randomUUID().toString();
        this.Text = text;
        this.CreationDate = creationDate;
        this.UserId = userId;
        this.UserName = userName;
        this.PostId = postId;
    }

    @NonNull
    public String getId() {
        return Id;
    }

    public void setId(@NonNull String id) {
        Id = id;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public String getCreationDate() {
        return CreationDate;
    }

    public void setCreationDate(String creationDate) {
        CreationDate = creationDate;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getPostId() {
        return PostId;
    }

    public void setPostId(String postId) {
        PostId = postId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }
}
