package com.example.userserver.follow;

public class FollowMessage {
    private int userId;
    private int followerId;
    private boolean follow;

    public FollowMessage(int userId, int followerId, boolean follow) {
        this.userId = userId;
        this.followerId = followerId;
        this.follow = follow;
    }

    public int getUserId() {
        return userId;
    }

    public int getFollowerId() {
        return followerId;
    }

    public boolean isFollow() {
        return follow;
    }
}
