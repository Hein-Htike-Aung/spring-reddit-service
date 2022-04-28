package com.example.redditservice.service;

import com.example.redditservice.dto.PostRequest;
import com.example.redditservice.dto.PostResponse;
import com.example.redditservice.mapper.PostMapper;
import com.example.redditservice.model.Post;
import com.example.redditservice.model.SubReddit;
import com.example.redditservice.model.User;
import com.example.redditservice.repository.PostRepo;
import com.example.redditservice.repository.SubRedditRepo;
import com.example.redditservice.repository.UserRepo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    /*
     * Mocking Repos & Services
     * */
//    private PostRepo postRepo = Mockito.mock(PostRepo.class);
//    private SubRedditRepo subRedditRepo = Mockito.mock(SubRedditRepo.class);
//    private AuthService authService = Mockito.mock(AuthService.class);
//    private UserRepo userRepo = Mockito.mock(UserRepo.class);
//    private PostMapper postMapper = Mockito.mock(PostMapper.class);

    @Mock private PostRepo postRepo;
    @Mock private SubRedditRepo subRedditRepo;
    @Mock private AuthService authService;
    @Mock private UserRepo userRepo;
    @Mock private PostMapper postMapper;
    private PostService postService;
    @Captor private ArgumentCaptor<Post> postArgumentCaptor;

    // Before Each Test
    @BeforeEach
    public void setup() {
        postService = new PostService(postRepo, subRedditRepo, authService, userRepo, postMapper);
    }

    @Test
    @DisplayName("Should find post by id")
    void shouldFindPostById() {
        // execute via mocks
        PostService postService = new PostService(postRepo, subRedditRepo, authService, userRepo, postMapper);

        Post post = new Post(123L, "First Post", "http://url.site", "Some Desc-ription", 0, Instant.now(), null, null);
        PostResponse expectedPostResponse = new PostResponse(123L, "First Post", "http://url.site", "Test",
                "Test User", "Test Subreddit", 0, 0, "1 Hour ago", false, false);

        // Test Repo
        Mockito.when(postRepo.findById(123L)).thenReturn(Optional.of(post));
        // Test Mapper
        Mockito.when(postMapper.mapToDto(Mockito.any(Post.class))).thenReturn(expectedPostResponse);

        PostResponse actualPostResponse = postService.getPost(123L);

        // Test Service
        Assertions.assertThat(actualPostResponse.getId()).isEqualTo(expectedPostResponse.getId());
        Assertions.assertThat(actualPostResponse.getPostName()).isEqualTo(expectedPostResponse.getPostName());
    }

    @Test
    @DisplayName("Should Save Posts")
    public void shouldSavePosts() {

        User currentUser = new User(123L, "test user", "secret password", "user@email.com", Instant.now(), true);
        SubReddit subreddit = new SubReddit(123L, "First Subreddit", "Subreddit Description", Collections.emptyList(), Instant.now(), currentUser);
        Post post = new Post(123L, "First Post", "http://url.site", "Test", 0, Instant.now(), null, null);

        PostRequest postRequest = new PostRequest(123L, "First Post", "http://url.site", "Post Reuquest Description", "First Subreddit");

        Mockito.when(subRedditRepo.findByName("First Subreddit"))
                .thenReturn(Optional.of(subreddit));
        Mockito.when(postMapper.mapToPost(postRequest, subreddit, currentUser))
                .thenReturn(post);
        Mockito.when(authService.getCurrentUser())
                .thenReturn(currentUser);

        postService.save(postRequest);
//        Mockito.verify(postRepo, Mockito.times(1)).save(ArgumentMatchers.any(Post.class));
        // Capture the Saved Return Object Value
        Mockito.verify(postRepo, Mockito.times(1)).save(postArgumentCaptor.capture());

        Assertions.assertThat(postArgumentCaptor.getValue().getId()).isEqualTo(123L);
        Assertions.assertThat(postArgumentCaptor.getValue().getPostName()).isEqualTo("First Post");
    }
}