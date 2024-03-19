package com.example.feedserver.feed;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class SocialFeedService {

    private SocialFeedRepository feedRepository;
    private KafkaTemplate<String, String> kafkaTemplate;
    private ObjectMapper objectMapper;

    public SocialFeedService(SocialFeedRepository feedRepository, KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.feedRepository = feedRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Value("${sns.user-server}")
    private String userServerUrl;

    private RestClient restClient = RestClient.create();


    public List<SocialFeed> getAllFeeds() {
        return feedRepository.findAll();
    }

    public List<SocialFeed> getAllFeedsByUploaderId(int uploaderId) {
        return feedRepository.findByUploaderId(uploaderId);
    }

    public SocialFeed getFeedById(int feedId) {
        return feedRepository.findById(feedId).orElse(null);
    }

    public void deleteFeed(int feedId) {
        feedRepository.deleteById(feedId);
    }
    @Transactional
    public SocialFeed createFeed(FeedRequest feed) {
        SocialFeed saveFeed = feedRepository.save(new SocialFeed(feed));
        UserInfo uploader = getUserInfo(saveFeed.getUploaderId());
        FeedInfo feedInfo = new FeedInfo(saveFeed, uploader.getUsername());
        try {
            kafkaTemplate.send("feed.created", objectMapper.writeValueAsString(feedInfo));
        } catch (JsonProcessingException e) {
            throw  new RuntimeException(e);
        }

        return saveFeed;
    }

    public void refreshAllFeeds() {
        List<SocialFeed> feeds = getAllFeeds();
        for (SocialFeed feed: feeds) {
            UserInfo uploader = getUserInfo(feed.getUploaderId());
            FeedInfo feedInfo = new FeedInfo(feed, uploader.getUsername());
            try {
                kafkaTemplate.send("feed.created", objectMapper.writeValueAsString(feedInfo));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public UserInfo getUserInfo(int userId) {
        return restClient.get()
                .uri(userServerUrl + "/api/users/" + userId)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    throw new RuntimeException("invalid server response" + response.getStatusText());
                })
                .body(UserInfo.class);
    }
}
