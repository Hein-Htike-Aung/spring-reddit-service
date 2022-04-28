package com.example.redditservice.mapper;

import com.example.redditservice.dto.SubRedditDto;
import com.example.redditservice.model.SubReddit;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class SubRedditMapper {

    public SubRedditDto mapSubRedditToDto(SubReddit subReddit) {
        if (subReddit == null) {
            return null;
        }

        return SubRedditDto.builder()
                .id(subReddit.getId())
                .subRedditName(subReddit.getName())
                .description(subReddit.getDescription())
                .numberOfPosts(subReddit.getPosts().size())
                .build();
    }

    public SubReddit mapDtoToSubReddit(SubRedditDto subRedditDto) {
        if (subRedditDto == null) {
            return null;
        }

        return SubReddit.builder()
                .id(subRedditDto.getId())
                .name(subRedditDto.getSubRedditName())
                .description(subRedditDto.getDescription())
                .createdDate(Instant.now())
                .build();
    }
}
