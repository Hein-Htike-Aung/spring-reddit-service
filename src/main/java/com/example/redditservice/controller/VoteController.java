package com.example.redditservice.controller;

import com.example.redditservice.dto.VoteDto;
import com.example.redditservice.service.VoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/votes")
public class VoteController {

    private final VoteService voteService;

    @PostMapping("/create")
    public ResponseEntity<Void> vote(
            @RequestBody VoteDto voteDto
    ) {
        voteService.save(voteDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
