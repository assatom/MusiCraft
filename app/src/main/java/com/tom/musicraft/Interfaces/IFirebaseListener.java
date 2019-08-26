package com.tom.musicraft.Interfaces;

import com.tom.musicraft.Models.Comment;
import com.tom.musicraft.Models.Post;
import com.tom.musicraft.Models.User;

import java.util.List;

public interface IFirebaseListener {

    void updatePosts(List<Post> posts);

    void updatedCommentsForPosts(List<Comment> commentList);

    void updateUsers(List<User> users);
}
