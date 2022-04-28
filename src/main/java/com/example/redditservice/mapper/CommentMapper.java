package com.example.redditservice.mapper;

import com.example.redditservice.dto.CommentDto;
import com.example.redditservice.model.Comment;
import com.example.redditservice.model.Post;
import com.example.redditservice.model.User;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    public Comment mapToComment(CommentDto commentsDto, Post post, User user) {
        if (commentsDto == null || post == null || user == null) {
            return null;
        }

        return Comment.builder()
                .id(commentsDto.getId())
                .text(commentsDto.getText())
                .post(post)
                .createdDate(Instant.now())
                .user(user)
                .build();
    }

    public CommentDto mapToCommentDto(Comment comment) {
        if (comment == null) {
            return null;
        }

        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .userName(comment.getUser().getUsername())
                .postId(comment.getPost().getId())
                .duration(getDuration(comment))
                .build();
    }

    private String getDuration(Comment comment) {
        return TimeAgo.using(comment.getCreatedDate().toEpochMilli());
    }
}
