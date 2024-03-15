package com.example.userserver;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfiguration {

    @Bean
    public NewTopic userFollowerTopic() {
        return new NewTopic("user.follower", 1, (short) 1);
    }
}