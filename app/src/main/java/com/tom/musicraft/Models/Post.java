package com.tom.musicraft.Models;

public class Post
{
    private String text;
    private String videoUrl;
    private String date;
    private User user;
    private String postID;

    public Post(String text, String videoUrl, String date, User user) {
        this.text = text;
        this.date = date;
        this.videoUrl = videoUrl;
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public void setUrl(String url) {
        this.videoUrl = url;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setId(String id){
        this.postID = id;
    }
    public String getId(){
        return this.postID;
    }
}
