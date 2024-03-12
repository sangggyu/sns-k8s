package com.example.notificationbatch;

import java.time.ZonedDateTime;

public class NotificationInfo {

    private int followId;
    private String email;
    private String username;
    private String followerName;
    private int followerId;

    public int getFollowId() {
        return followId;
    }

    public void setFollowId(int followId) {
        this.followId = followId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFollowerName() {
        return followerName;
    }

    public void setFollowerName(String followerName) {
        this.followerName = followerName;
    }

    public int getFollowerId() {
        return followerId;
    }

    public void setFollowerId(int followerId) {
        this.followerId = followerId;
    }

    public ZonedDateTime getFollowerDateTime() {
        return followerDateTime;
    }

    public void setFollowerDateTime(ZonedDateTime followerDateTime) {
        this.followerDateTime = followerDateTime;
    }

    private ZonedDateTime followerDateTime;
}
