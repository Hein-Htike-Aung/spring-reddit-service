package com.example.redditservice.service;

import com.example.redditservice.dto.CommentDto;
import com.example.redditservice.dto.NotificationEmail;
import com.example.redditservice.exception.SpringRedditException;
import com.example.redditservice.mapper.CommentMapper;
import com.example.redditservice.model.Post;
import com.example.redditservice.model.User;
import com.example.redditservice.repository.CommentRepo;
import com.example.redditservice.repository.PostRepo;
import com.example.redditservice.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class CommentService {
    private static final String POST_URL = "";
    private final CommentRepo commentRepo;
    private final PostRepo postRepo;
    private final UserRepo userRepo;
    private final AuthService authService;
    private final CommentMapper commentMapper;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    public void save(CommentDto commentsDto) {
        Post post = postRepo.findById(commentsDto.getPostId())
                .orElseThrow(() -> new SpringRedditException("No Post found with id - " + commentsDto.getPostId()));

        commentRepo.save(commentMapper.mapToComment(commentsDto, post, authService.getCurrentUser()));

        // Send Mail To Post's Owner
        // post.getUser() = Post's Owner
        String message = mailContentBuilder.build(commentsDto.getUserName() + " commented on your post" + POST_URL);

        mailService.sendMail(new NotificationEmail(
                commentsDto.getUserName() + " commented on your post",
                post.getUser().getEmail(),
                message
        ));
    }

    public List<CommentDto> getAllCommentsForPost(Long postId) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new SpringRedditException("No post found with id - " + postId));

        return commentRepo.findByPost(post).stream()
                .map(commentMapper::mapToCommentDto)
                .collect(Collectors.toList());
    }

    public List<CommentDto> getAllCommentsForUser(String username) {

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new SpringRedditException("No user found with username - " + username));

        return commentRepo.findByUser(user).stream()
                .map(commentMapper::mapToCommentDto)
                .collect(Collectors.toList());
    }

    public boolean containSwearWords(String comment) {
        if (comment.contains("shit")) {
            throw new SpringRedditException("Comment contains unacceptable language");
        }
        return false;
    }
}
