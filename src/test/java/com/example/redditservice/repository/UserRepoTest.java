package com.example.redditservice.repository;

import com.example.redditservice.BaseTest;
import com.example.redditservice.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@ActiveProfiles("test") // Use application-test.properties
public class UserRepoTest extends BaseTest {

    @Autowired
    private UserRepo userRepo;

    @Test
    public void shouldSavePost() {
        User expectedUserObject = new User(123L, "test user", "secret password", "user@email.com", Instant.now(), true);
        User actualUserObject = userRepo.save(expectedUserObject);
        assertThat(actualUserObject).usingRecursiveComparison()
                .ignoringFields("userId").isEqualTo(expectedUserObject);
    }

    @Test
    @Sql("classpath:test-data.sql")
    void shouldSaveUsersThroughSqlFile() {
        Optional<User> test = userRepo.findByUsername("testuser_sql");
        assertThat(test).isNotEmpty();
    }
}