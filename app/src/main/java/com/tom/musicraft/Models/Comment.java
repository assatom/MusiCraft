package com.tom.musicraft.Models;

import com.google.firebase.Timestamp;

public class Comment {

    private String mId;
    private String mUserId;
    private User mAuthor; // will contain data of user created comment
    private String mText; // actual comment text
    private String mCreationDate;
    private Timestamp lastUpdate;
    private String mPostId;

    public String getPostId() {
        return mPostId;
    }

    public void setPostId(String mPostId) {
        this.mPostId = mPostId;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        this.mUserId = userId;
    }

    public Comment(){
        // Need empty ctor for deserialization from DB
    }

    public void setId(String id) {
        this.mId = id;
    }

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

    public User getAuthor() {
        return this.mAuthor;
    }
    public void setAuthor(User author) {
        this.mAuthor = author;
    }

    public String getText() {
        return this.mText;
    }
    public void setText(String text) {
        this.mText = text;
    }

    public String getCreationDate() {
        return this.mCreationDate;
    }

    public void setCreationDate(String creationDate) {
        this.mCreationDate = creationDate;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
