package dev.rlet.todoitem_service.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Getter @Setter
@ConfigurationProperties(prefix = "spring.kafka")
public class KafkaProperties {

    private String bootstrapServers;

    private Producer producer = new Producer();
    private Consumer consumer = new Consumer();

    @Getter @Setter
    public static class Producer {
        private String keySerializer;
        private String valueSerializer;
        private String acks;
        private String partitionerClass;
        private Map<String, String> properties;
    }

    @Getter @Setter
    public static class Consumer {
        private String groupId;
        private String autoOffsetReset;
        private String keyDeserializer;
        private String valueDeserializer;
        private Map<String, String> properties;
    }
}