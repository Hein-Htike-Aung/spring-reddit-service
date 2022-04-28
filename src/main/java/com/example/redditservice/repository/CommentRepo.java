package com.example.redditservice.repository;

import com.example.redditservice.model.Comment;
import com.example.redditservice.model.Post;
import com.example.redditservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);

    List<Comment> findByUser(User user);
}
