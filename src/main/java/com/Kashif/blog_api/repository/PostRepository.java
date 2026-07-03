package com.Kashif.blog_api.repository;

import com.Kashif.blog_api.entity.Post;
import com.Kashif.blog_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findByAuthor(User author);
    List<Post> findAllByOrderByCreatedAtDesc();
}
