package com.example.redditservice.service;

import com.example.redditservice.dto.PostRequest;
import com.example.redditservice.dto.PostResponse;
import com.example.redditservice.exception.SpringRedditException;
import com.example.redditservice.mapper.PostMapper;
import com.example.redditservice.model.Post;
import com.example.redditservice.model.SubReddit;
import com.example.redditservice.model.User;
import com.example.redditservice.repository.PostRepo;
import com.example.redditservice.repository.SubRedditRepo;
import com.example.redditservice.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepo postRepo;
    private final SubRedditRepo subRedditRepo;
    private final AuthService authService;
    private final UserRepo userRepo;
    private final PostMapper postMapper;

    public void save(PostRequest postRequest) {

        SubReddit subReddit = subRedditRepo.findByName(postRequest.getSubredditName())
                .orElseThrow(() -> new SpringRedditException(postRequest.getSubredditName()));

        postRepo.save(postMapper.mapToPost(postRequest, subReddit, this.authService.getCurrentUser()));

    }

    public List<PostResponse> getAllPosts() {
        return postRepo.findAll()
                .stream()
                .map(postMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public PostResponse getPost(Long id) {
        Post post = postRepo.findById(id).orElseThrow(() -> new SpringRedditException("Post Not found with id - " + id));
        return postMapper.mapToDto(post);
    }

    public List<PostResponse> getPostsBySubreddit(Long subRedditId) {
        SubReddit subReddit = subRedditRepo
                .findById(subRedditId)
                .orElseThrow(() -> new SpringRedditException("SubReddit not found with id - " + subRedditId));

        List<Post> postListBySubReddit = postRepo.findBySubreddit(subReddit);

        return postListBySubReddit.stream()
                .map(postMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public List<PostResponse> getPostsByUsername(String username) {
        User user = userRepo
                .findByUsername(username)
                .orElseThrow(() -> new SpringRedditException("No User wih username - " + username));

        return postRepo.findByUser(user)
                .stream()
                .map(postMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
