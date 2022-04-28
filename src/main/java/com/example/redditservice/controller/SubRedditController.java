package com.example.redditservice.controller;

import com.example.redditservice.dto.SubRedditDto;
import com.example.redditservice.service.SubRedditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/subreddit")
public class SubRedditController {

    private final SubRedditService subRedditService;

    @PostMapping("/create")
    private ResponseEntity<SubRedditDto> createSubReddit(
            @RequestBody SubRedditDto subRedditDto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(subRedditService.save(subRedditDto));
    }

    @GetMapping
    public ResponseEntity<List<SubRedditDto>> getAllSubReddits() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(subRedditService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubRedditDto> getSubReddit(
            @PathVariable("id") Long id
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(subRedditService.getSubReddit(id));
    }
}
