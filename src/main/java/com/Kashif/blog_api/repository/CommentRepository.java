package com.Kashif.blog_api.repository;

import com.Kashif.blog_api.entity.Comment;
import com.Kashif.blog_api.entity.Post;
import com.Kashif.blog_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByPost(Post post);
    List<Comment> findByUser(User user);
}
