package com.example.redditservice.repository;

import com.example.redditservice.model.Post;
import com.example.redditservice.model.SubReddit;
import com.example.redditservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepo extends JpaRepository<Post, Long> {

    List<Post> findBySubreddit(SubReddit subReddit);

    List<Post> findByUser(User user);
}
