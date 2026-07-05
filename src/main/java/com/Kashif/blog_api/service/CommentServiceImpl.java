package com.Kashif.blog_api.service;

import com.Kashif.blog_api.entity.Comment;
import com.Kashif.blog_api.entity.Post;
import com.Kashif.blog_api.entity.User;
import com.Kashif.blog_api.repository.CommentRepository;
import com.Kashif.blog_api.repository.PostRepository;
import com.Kashif.blog_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService{

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Comment createComment(Long postId ,Long userId, Comment comment){
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new RuntimeException("Post not found with id:" + postId));

        User user = userRepository.findById(userId).
                orElseThrow(()-> new RuntimeException("User not found with id:" + userId));

        comment.setPost(post);
        comment.setUser(user);
        comment.setCreatedAt(LocalDateTime.now());

        return commentRepository.save(comment);

    }

    @Override
    public Comment getCommentById(Long id){
        return commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with id:" + id));
    }

    @Override
    public List<Comment> getCommentsByPost(Post post) {
        return commentRepository.findByPost(post);
    }

    @Override
    public List<Comment> getCommentsByUser(User user){
        return commentRepository.findByUser(user);
    }

    @Override
    public void deleteComment(Long id){
        if (!commentRepository.existsById(id)) {
            throw new RuntimeException("Comment not found with id: " + id);
        }
        commentRepository.deleteById(id);

    }
}
