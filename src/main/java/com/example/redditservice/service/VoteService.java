package com.example.redditservice.service;

import com.example.redditservice.dto.VoteDto;
import com.example.redditservice.ennumeration.VoteType;
import com.example.redditservice.exception.SpringRedditException;
import com.example.redditservice.mapper.VoteMapper;
import com.example.redditservice.model.Post;
import com.example.redditservice.model.Vote;
import com.example.redditservice.repository.PostRepo;
import com.example.redditservice.repository.UserRepo;
import com.example.redditservice.repository.VoteRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class VoteService {

    private final VoteMapper voteMapper;
    private final PostRepo postRepo;
    private final VoteRepo voteRepo;
    private final AuthService authService;

    public void save(VoteDto voteDto) {
        Post post = postRepo.findById(voteDto.getPostId())
                .orElseThrow(() -> new SpringRedditException("No Post found with id - " + voteDto.getPostId()));

        // check repetitive Vote or Not
        Optional<Vote> voteByPostAndUser = voteRepo.findTopByPostAndUserOrderByIdDesc(post, authService.getCurrentUser());

        if (voteByPostAndUser.isPresent() && voteByPostAndUser.get().getVoteType().equals(voteDto.getVoteType())) {
            throw new SpringRedditException("You have already voted for post " + post.getPostName());
        }

        // Modify To Post (voteCount)
        if(voteDto.getVoteType().equals(VoteType.UPVOTE)) {
            post.setVoteCount(post.getVoteCount() + 1);
        }
        if(voteDto.getVoteType().equals(VoteType.DOWNVOTE)) {
            post.setVoteCount(post.getVoteCount() - 1);
        }

        voteRepo.save(voteMapper.mapToVote(voteDto, post, authService.getCurrentUser()));
        postRepo.save(post);
    }
}
