package com.example.redditservice.controller;

import com.example.redditservice.dto.CommentDto;
import com.example.redditservice.model.Comment;
import com.example.redditservice.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentsController {

    private final CommentService commentService;

    @PostMapping("/create")
    public ResponseEntity<Void> createComment(
            @RequestBody CommentDto commentsDto
    ) {
        commentService.save(commentsDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/by-post/{id}")
    public ResponseEntity<List<CommentDto>> getAllCommentsForPost(
            @PathVariable("id") Long postId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(commentService.getAllCommentsForPost(postId));
    }

    @GetMapping("/by-user/{username}")
    public ResponseEntity<List<CommentDto>> getAllCommentsForUser(
            @PathVariable("username") String username
    ) {
        return  ResponseEntity.status(HttpStatus.OK)
                .body(commentService.getAllCommentsForUser(username));
    }
}
