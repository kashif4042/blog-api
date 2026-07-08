package com.Kashif.blog_api.controller;

import com.Kashif.blog_api.dto.PostRequest;
import com.Kashif.blog_api.dto.PostResponse;
import com.Kashif.blog_api.dto.UserResponse;
import com.Kashif.blog_api.entity.Post;
import com.Kashif.blog_api.entity.User;
import com.Kashif.blog_api.service.PostService;
import com.Kashif.blog_api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @PostMapping("/user/{userId}")
    public ResponseEntity<PostResponse>
    createPost(@PathVariable Long userId, @Valid @RequestBody PostRequest request) {
        User author = userService.getUserById(userId);

        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setAuthor(author);

        Post savedPost = postService.createPost(post);
        UserResponse authorResponse = new UserResponse(savedPost.getAuthor().getId(), savedPost.getAuthor().getUsername(), savedPost.getAuthor().getEmail());
        PostResponse response = new PostResponse(savedPost.getId(), savedPost.getTitle(), savedPost.getContent(), savedPost.getCreatedAt(), authorResponse);

        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long id) {
        Post post = postService.getPostById(id);

        UserResponse authorResponse = new UserResponse(post.getAuthor().getId(), post.getAuthor().getUsername(), post.getAuthor().getEmail());

        PostResponse response = new PostResponse(post.getId(), post.getTitle(), post.getContent(), post.getCreatedAt(), authorResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();

        List<PostResponse> responseList = posts.stream()
                .map(post -> {
                    UserResponse authorResponse = new UserResponse(post.getAuthor().getId(), post.getAuthor().getUsername(), post.getAuthor().getEmail());
                    return new PostResponse(post.getId(), post.getTitle(), post.getContent(), post.getCreatedAt(), authorResponse);
                })
                .collect(Collectors.toList());

        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    @GetMapping("/author/{userId}")
    public ResponseEntity<List<PostResponse>> getPostsByAuthor(@PathVariable Long userId){
        User author = userService.getUserById(userId);
        List<Post> posts = postService.getPostsByAuthor(author);

        List<PostResponse> responseList = posts.stream()
                .map(post -> {
                    UserResponse authorResponse = new UserResponse(post.getAuthor().getId(), post.getAuthor().getUsername(), post.getAuthor().getEmail());
                    return new PostResponse(post.getId(), post.getTitle(), post.getContent(), post.getCreatedAt(), authorResponse);
                })
                .collect(Collectors.toList());

        return new ResponseEntity<>(responseList, HttpStatus.OK);

    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long id, @Valid @RequestBody PostRequest request) {
        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());

        Post updatedPost = postService.updatePost(id, post);

        UserResponse authorResponse = new UserResponse(updatedPost.getAuthor().getId(), updatedPost.getAuthor().getUsername(), updatedPost.getAuthor().getEmail());
        PostResponse response = new PostResponse(updatedPost.getId(), updatedPost.getTitle(), updatedPost.getContent(), updatedPost.getCreatedAt(), authorResponse);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }


}



