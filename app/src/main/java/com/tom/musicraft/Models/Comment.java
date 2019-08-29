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

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmUserId() {
        return mUserId;
    }

    public void setmUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public User getmAuthor() {
        return mAuthor;
    }

    public void setmAuthor(User mAuthor) {
        this.mAuthor = mAuthor;
    }

    public String getmText() {
        return mText;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }

    public String getmCreationDate() {
        return mCreationDate;
    }

    public void setmCreationDate(String mCreationDate) {
        this.mCreationDate = mCreationDate;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getmPostId() {
        return mPostId;
    }

    public void setmPostId(String mPostId) {
        this.mPostId = mPostId;
    }
}
