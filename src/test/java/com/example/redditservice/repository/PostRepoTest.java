package com.example.redditservice.repository;

import com.example.redditservice.BaseTest;
import com.example.redditservice.dto.PostResponse;
import com.example.redditservice.model.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PostRepositoryTest extends BaseTest {

    @Autowired
    private PostRepo postRepo;

    @Test
    public void shouldSavePost() {
//        Post expectedPostObject = new Post(null, "First Post", "http://url.site", "Test",
//                0, null, Instant.now(), null);
//        Post actualPostObject = postRepo.save(expectedPostObject);
//        assertThat(actualPostObject).usingRecursiveComparison()
//                .ignoringFields("postId").isEqualTo(expectedPostObject);
    }

}