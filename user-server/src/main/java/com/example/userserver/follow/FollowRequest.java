package com.example.userserver.follow;

public class FollowRequest {
    private final int userId;
    private final int followerId;

    public FollowRequest(int userId, int followerId) {
        this.userId = userId;
        this.followerId = followerId;
    }

    public int getUserId() {
        return userId;
    }

    public int getFollowerId() {
        return followerId;
    }
}
