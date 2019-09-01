package com.tom.musicraft.Models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Users")
public class UserAccountSettings
{
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "user_ID")
    private String user_id;

    private long followers;
    private long following;
    private String profile_photo;
    private String email;
    private String userName;



    public UserAccountSettings(long followers, long following, String email , String user_id, String userName)
    {
        this.followers = followers;
        this.following = following;
        this.email= email;
        this.user_id = user_id;
        this.userName = userName;
        this.profile_photo ="";
//        this.posts = posts;
//        this.profile_photo = profile_photo;
    }

    public UserAccountSettings() {}


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @NonNull
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(@NonNull String user_id) {
        this.user_id = user_id;
    }

    public long getFollowers() {
        return followers;
    }

    public void setFollowers(long followers) {
        this.followers = followers;
    }

    public long getFollowing() {
        return following;
    }

    public void setFollowing(long following) {
        this.following = following;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setProfile_photo(String profile_photo) {
        this.profile_photo = profile_photo;
    }

    public String getProfile_photo() {
        return profile_photo;
    }


    @Override
    public String toString() {
        return "UserAccountSettings{" +
                ", followers=" + followers +
                ", following=" + following +
                ", email='" + email + '\'' +
                '}';
    }


}
