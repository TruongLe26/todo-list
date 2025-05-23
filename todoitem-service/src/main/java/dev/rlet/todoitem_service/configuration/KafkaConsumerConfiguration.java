package dev.rlet.todoitem_service.configuration;

import com.leqtr.shared.dto.TodoItemDTO;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfiguration {

    private final KafkaProperties kafkaProperties;

    @Bean
    public ConsumerFactory<String, TodoItemDTO> consumerFactory() {
        JsonDeserializer<TodoItemDTO> deserializer = new JsonDeserializer<>(TodoItemDTO.class);
        deserializer.addTrustedPackages("*");
        deserializer.setRemoveTypeHeaders(false);
        deserializer.setUseTypeMapperForKey(true);

        Map<String, Object> consumerConfigProps = getStringObjectMap();

        if (kafkaProperties.getConsumer().getProperties() != null) {
            consumerConfigProps.putAll(kafkaProperties.getConsumer().getProperties());
        }

        return new DefaultKafkaConsumerFactory<>(consumerConfigProps, new StringDeserializer(), deserializer);
    }

    private Map<String, Object> getStringObjectMap() {
        Map<String, Object> consumerConfigProps = new HashMap<>();
        consumerConfigProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        consumerConfigProps.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getConsumer().getGroupId());
        consumerConfigProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaProperties.getConsumer().getAutoOffsetReset());
        consumerConfigProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaProperties.getConsumer().getKeyDeserializer());
        consumerConfigProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafkaProperties.getConsumer().getValueDeserializer());
        return consumerConfigProps;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TodoItemDTO> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, TodoItemDTO> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}