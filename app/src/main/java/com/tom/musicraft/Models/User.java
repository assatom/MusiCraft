package com.tom.musicraft.Models;

import java.util.List;
import java.util.Vector;

public class User
{
    private String user_id;
    private long phone_number;
    private String email;
    private String userName;
    private Vector<Post> userPosts;

    public User(String user_id, long phone_number, String email, String username) {
        this.user_id = user_id;
        this.phone_number = phone_number;
        this.email = email;
        this.userName = username;
        this.userPosts = new Vector<Post>();
    }

    public User(Vector<Post> userPosts){
        userPosts=new Vector<Post>();
    }

    public User() {}


//    protected User(Parcel in) {
//        user_id = in.readString();
//        phone_number = in.readLong();
//        email = in.readString();
//        username = in.readString();
//    }

//    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
//        @Override
//        public User createFromParcel(Parcel in) {
//            return new User(in);
//        }
//
//        @Override
//        public User[] newArray(int size) {
//            return new User[size];
//        }
//    };

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public long getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(long phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return userName;
    }

    public void setUsername(String username) {
        this.userName = username;
    }

    public void AddPost(Post post)
    {
        this.userPosts.add(post);
    }


    @Override
    public String toString() {
        return "User{" +
                "user_id='" + user_id + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", email='" + email + '\'' +
                ", username='" + userName + '\'' +
                '}';
    }
}
