package com.example.userserver.follow;

import com.example.userserver.user.UserInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FollowService {

    private final FollowRepository followRepository;

    private KafkaTemplate<String, String> kafkaTemplate;
    private ObjectMapper objectMapper = new ObjectMapper();

    public FollowService(FollowRepository followRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.followRepository = followRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public boolean isFollow(int userId, int followerId) {
        Follow follow = followRepository.findByUserIdAndFollowId(userId, followerId);
        return follow != null;
    }
    @Transactional
    public Follow followUser(int userId, int followerId) {
        if (isFollow(userId, followerId)) {
            return null;
        }

        sendFollowerMessage(userId, followerId, true);

        return followRepository.save(new Follow(userId, followerId));
    }

    @Transactional
    public boolean unfollowUser(int userId, int followerId) {
        Follow follow = followRepository.findByUserIdAndFollowId(userId, followerId);
        if (follow == null) {
            return false;
        }

        sendFollowerMessage(userId, followerId, false);
        followRepository.delete(follow);
        return true;
    }

    private void sendFollowerMessage(int userId, int followerId, boolean isFollow) {
        FollowMessage message = new FollowMessage(userId, followerId, isFollow);

        try {
            kafkaTemplate.send("user.follower", objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public List<UserInfo> listFollower(int userId) {
        return followRepository.findFollowersByUserId(userId);
    }

    public List<UserInfo> listFollowing(int userId) {
        return followRepository.findFollowingByUserId(userId);
    }

}
