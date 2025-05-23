package dev.rlet.todoitem_service.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfiguration {
    public static final String USER_NOTIFICATION_TOPIC = "user-notification";
    public static final String TODO_ITEM_CACHE_INVALIDATION_TOPIC = "todo-item-cache-invalidation";

    @Bean
    public NewTopic userNotificationTopic() {
        return TopicBuilder.name(USER_NOTIFICATION_TOPIC)
                .partitions(6)
                .replicas(3)
                .config(TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG, "2")
                .build();
    }

    @Bean
    public NewTopic todoItemCacheInvalidationTopic() {
        return TopicBuilder.name(TODO_ITEM_CACHE_INVALIDATION_TOPIC)
                .partitions(6)
                .replicas(3)
                .config(TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG, "2")
                .build();
    }
}