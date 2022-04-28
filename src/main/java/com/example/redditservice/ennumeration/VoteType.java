package com.example.redditservice.ennumeration;

public enum VoteType {
    UPVOTE(1), DOWNVOTE(-1);

    private final Integer direction;

    public Integer getDirection() {
        return direction;
    }

    VoteType(Integer direction) {
        this.direction = direction;
    }
}
