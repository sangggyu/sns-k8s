package com.example.feedserver.feed;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feeds")
public class SocialFeedController {

    private SocialFeedService feedService;

    public SocialFeedController(SocialFeedService feedService) {
        this.feedService = feedService;
    }

    @GetMapping
    public List<SocialFeed> getAllFeeds() {
        return feedService.getAllFeeds();
    }

    @GetMapping("/user/{userId}")
    public List<SocialFeed> getAllFeedsByUser(@PathVariable("userId") int userId) {
        return feedService.getAllFeedsByUploaderId(userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SocialFeed> getFeedById(@PathVariable int id) {
        SocialFeed feed = feedService.getFeedById(id);
        if (feed == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(feed);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFeed(@PathVariable int id) {
        feedService.deleteFeed(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public SocialFeed createFeed(@RequestBody FeedRequest feed) {
        return feedService.createFeed(feed);
    }
}
