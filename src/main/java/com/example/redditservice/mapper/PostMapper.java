package com.example.redditservice.mapper;

import com.example.redditservice.dto.PostRequest;
import com.example.redditservice.dto.PostResponse;
import com.example.redditservice.ennumeration.VoteType;
import com.example.redditservice.model.Post;
import com.example.redditservice.model.SubReddit;
import com.example.redditservice.model.User;
import com.example.redditservice.model.Vote;
import com.example.redditservice.repository.CommentRepo;
import com.example.redditservice.repository.VoteRepo;
import com.example.redditservice.service.AuthService;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PostMapper {

    private final CommentRepo commentRepo;
    private final AuthService authService;
    private final VoteRepo voteRepo;

    public Post mapToPost(PostRequest postRequest, SubReddit subReddit, User currentUser) {
        if (postRequest == null || subReddit == null || currentUser == null) {
            return null;
        }

        return Post.builder()
                .id(postRequest.getPostId())
                .postName(postRequest.getPostName())
                .url(postRequest.getUrl())
                .description(postRequest.getDescription())
                .createdDate(Instant.now())
                .subreddit(subReddit)
                .user(currentUser)
                .voteCount(0)
                .build();
    }

    public PostResponse mapToDto(Post post) {
        if (post == null) {
            return null;
        }

        return PostResponse.builder()
                .id(post.getId())
                .postName(post.getPostName())
                .subredditName(post.getSubreddit().getName())
                .description(post.getDescription())
                .url(post.getUrl())
                .userName(post.getUser().getUsername())
                .voteCount(post.getVoteCount())

                .duration(getDuration(post))
                .commentCount(getCommentCount(post))
                .upVote(isPostUpVoted(post))
                .downVote(isPostDownVoted(post))
                .build();
    }

    private String getDuration(Post post) {
        return TimeAgo.using(post.getCreatedDate().toEpochMilli());
    }

    private Integer getCommentCount(Post post) {
        return commentRepo.findByPost(post).size();
    }

    private boolean isPostUpVoted(Post post) {
        return checkVoteUpOrDown(post, VoteType.UPVOTE);
    }

    private boolean isPostDownVoted(Post post) {
        return checkVoteUpOrDown(post, VoteType.DOWNVOTE);
    }

    private boolean checkVoteUpOrDown(Post post, VoteType voteType) {
        if (authService.getCurrentUser() != null) {
            Optional<Vote> topByPostAndUserOrderByVoteIdDesc = voteRepo
                    .findTopByPostAndUserOrderByIdDesc(post, authService.getCurrentUser());

            return topByPostAndUserOrderByVoteIdDesc.filter(vote -> vote.getVoteType().equals(voteType))
                    .isPresent();
        }
        return false;
    }
}
