package com.Kashif.blog_api.service;

import com.Kashif.blog_api.entity.Comment;
import com.Kashif.blog_api.entity.Post;
import com.Kashif.blog_api.entity.User;

import java.util.List;

public interface CommentService {
    Comment createComment(Long postId,Long userId, Comment comment);

    Comment getCommentById(Long id);

    List<Comment> getCommentsByPost(Post post);

    List<Comment> getCommentsByUser(User user);

    void deleteComment(Long id);
}
