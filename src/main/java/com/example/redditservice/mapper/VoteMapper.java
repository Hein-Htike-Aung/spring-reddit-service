package com.example.redditservice.mapper;

import com.example.redditservice.dto.VoteDto;
import com.example.redditservice.model.Post;
import com.example.redditservice.model.User;
import com.example.redditservice.model.Vote;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class VoteMapper {

    public Vote mapToVote(VoteDto voteDto, Post post, User currentUser) {
        if(voteDto == null || post == null || currentUser == null) {
            return null;
        }

        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .user(currentUser)
                .post(post)
                .build();
    }
}
