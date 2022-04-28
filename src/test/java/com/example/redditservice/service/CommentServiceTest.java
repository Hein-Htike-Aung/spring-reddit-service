package com.example.redditservice.service;

import com.example.redditservice.exception.SpringRedditException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

public class CommentServiceTest {

    @Test
    @DisplayName("Test should pass when comment do not contains swear words")
    void shouldNotContainsSwearWordsInsideComment() {
        CommentService commentService = new CommentService(null, null, null, null, null, null, null);
        assertThat(commentService.containSwearWords("Clean Comment")).isFalse();
    }

    @Test
    @DisplayName("Should throw Exception when comments contains swear words")
    void shouldFailWhenCommentContainsSwearWords() {
        CommentService commentService = new CommentService(null, null, null, null, null, null, null);

        assertThatThrownBy(() -> {
            commentService.containSwearWords("shit!!!!");
        }).isInstanceOf(SpringRedditException.class)
                .hasMessage("Comment contains unacceptable language");
    }
}