package com.tom.musicraft.Models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;

@Entity(tableName = "Comments")
public class Comment {

    @PrimaryKey     //(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "comment_ID")
    private String mId;
    private String mUserId;
//    private User mAuthor; // will contain data of user created comment
    private String mText; // actual comment text
    private String mCreationDate;
    private String lastUpdate;
    private String mPostId;

    public Comment(){
        // Need empty ctor for deserialization from DB
    }

    public void setId(@NonNull String id) {
        this.mId = id;
    }

    @NonNull
    public String getId()
    {
        return this.mId;
    }

    public Comment(String text, String creationDate, String userId){
        this.mId = java.util.UUID.randomUUID().toString();
        this.mText = text;
        this.mCreationDate = creationDate;
        this.mUserId = userId;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String mUserId) {
        this.mUserId = mUserId;
    }

//    public User getmAuthor() {
//        return mAuthor;
//    }
//
//    public void setmAuthor(User mAuthor) {
//        this.mAuthor = mAuthor;
//    }

    public String getText() {
        return mText;
    }

    public void setText(String mText) {
        this.mText = mText;
    }

    public String getCreationDate() {
        return mCreationDate;
    }

    public void setCreationDate(String mCreationDate) {
        this.mCreationDate = mCreationDate;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getPostId() {
        return mPostId;
    }

    public void setPostId(String mPostId) {
        this.mPostId = mPostId;
    }
}
