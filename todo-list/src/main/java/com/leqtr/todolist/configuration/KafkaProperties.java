package com.leqtr.todolist.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Getter @Setter
@ConfigurationProperties(prefix = "spring.kafka")
public class KafkaProperties {

    private String bootstrapServers;
    private Consumer consumer = new Consumer();
    private Producer producer = new Producer();

    @Getter @Setter
    public static class Consumer {
        private String groupId;
        private Map<String, String> properties;
    }

    @Getter @Setter
    public static class Producer {
        private String keySerializer;
        private String valueSerializer;
        private Map<String, String> properties;
    }
}
