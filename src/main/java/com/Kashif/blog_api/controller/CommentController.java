package com.Kashif.blog_api.controller;

import com.Kashif.blog_api.dto.CommentRequest;
import com.Kashif.blog_api.dto.CommentResponse;
import com.Kashif.blog_api.dto.UserResponse;
import com.Kashif.blog_api.entity.Comment;
import com.Kashif.blog_api.entity.Post;
import com.Kashif.blog_api.entity.User;
import com.Kashif.blog_api.service.CommentService;
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
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    // POST /api/comments/post/{postId}/user/{userId}
    // Creates a new comment on a specific post, written by a specific user
    @PostMapping("/post/{postId}/user/{userId}")
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable Long postId,
            @PathVariable Long userId,
            @Valid @RequestBody CommentRequest request) {

        // Build a bare Comment entity - only "content" comes from the client
        Comment comment = new Comment();
        comment.setContent(request.getContent());

        // Service handles fetching + validating both Post and User internally,
        // then attaches them and saves - we just pass the IDs through
        Comment savedComment = commentService.createComment(postId, userId, comment);

        // Convert the saved entity into a safe response DTO
        CommentResponse response = mapToResponse(savedComment);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // GET /api/comments/{id}
    // Fetches a single comment by its own id
    @GetMapping("/{id}")
    public ResponseEntity<CommentResponse> getCommentById(@PathVariable Long id) {
        Comment comment = commentService.getCommentById(id);
        CommentResponse response = mapToResponse(comment);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // GET /api/comments/post/{postId}
    // Fetches all comments belonging to a specific post
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentResponse>> getCommentsByPost(@PathVariable Long postId) {
        // Need the actual Post object first, since the service method expects one
        Post post = postService.getPostById(postId);
        List<Comment> comments = commentService.getCommentsByPost(post);

        // Convert every Comment in the list to a CommentResponse
        List<CommentResponse> responseList = comments.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    // GET /api/comments/user/{userId}
    // Fetches all comments written by a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CommentResponse>> getCommentsByUser(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        List<Comment> comments = commentService.getCommentsByUser(user);

        List<CommentResponse> responseList = comments.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    // DELETE /api/comments/{id}
    // Deletes a comment by id, no response body needed
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

    // Helper method - converts a Comment entity into a safe CommentResponse DTO.
    // Pulled out into its own method since this exact conversion is needed
    // in FOUR different places above (createComment, getCommentById,
    // getCommentsByPost, getCommentsByUser) - avoids repeating the same
    // 5 lines of code four times.
    private CommentResponse mapToResponse(Comment comment) {
        UserResponse userResponse = new UserResponse(
                comment.getUser().getId(),
                comment.getUser().getUsername(),
                comment.getUser().getEmail()
        );

        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getPost().getId(),
                userResponse
        );
    }
}