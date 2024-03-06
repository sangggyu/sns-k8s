package com.example.userserver.follow;

import jakarta.persistence.*;

import java.time.ZonedDateTime;

@Entity
@Table
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int followId;
    private int userId;
    private int follwerId;
    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime followDatetime;

    public Follow() {
    }

    public Follow(int userId, int follwerId) {
        this.userId = userId;
        this.follwerId = follwerId;
    }

    @PrePersist
    protected void onCreate() {
        followDatetime = ZonedDateTime.now();
    }
}
