package com.example.redditservice.repository;

import com.example.redditservice.model.Post;
import com.example.redditservice.model.SubReddit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubRedditRepo extends JpaRepository<SubReddit, Long> {
    Optional<SubReddit> findByName(String subredditName);
}
