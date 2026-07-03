package com.Kashif.blog_api.service;

import com.Kashif.blog_api.entity.Post;
import com.Kashif.blog_api.entity.User;

import java.util.List;

public interface PostService {
    Post createPost(Post post);

    Post getPostById(Long id);

    List<Post> getAllPosts();

    List<Post> getPostsByAuthor(User author);

    Post updatePost(Long id, Post updatedPost);

    void deletePost(Long id);

}
