package com.leqtr.todolist.configuration;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.BytesDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.utils.Bytes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.converter.BytesJsonMessageConverter;
import org.springframework.kafka.support.converter.RecordMessageConverter;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfiguration {
    @Bean
    public ConsumerFactory<String, Bytes> consumerFactory() {
        Map<String, Object> consumerConfigs = new HashMap<>();
        consumerConfigs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092,localhost:9093,localhost:9094");
        consumerConfigs.put(ConsumerConfig.GROUP_ID_CONFIG, "notification-group");
        return new DefaultKafkaConsumerFactory<>(
                consumerConfigs,
                new StringDeserializer(),
                new BytesDeserializer()
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Bytes> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Bytes> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setRecordMessageConverter(messageConverter());
        return factory;
    }

    @Bean
    public RecordMessageConverter messageConverter() {
        return new BytesJsonMessageConverter();
    }

    /* Old configuration */
    //    @Bean
//    public ConsumerFactory<String, Object> consumerFactory() {
//        JsonDeserializer<Object> valueDeserializer = new JsonDeserializer<>();
//        valueDeserializer.addTrustedPackages("com.leqtr.shared.event");
//        valueDeserializer.setUseTypeMapperForKey(false);
////        valueDeserializer.setRemoveTypeHeaders(false);
//        valueDeserializer.setUseTypeHeaders(false);
//
//        // Use default type mapper
////        valueDeserializer.setTypeMapper(new DefaultJackson2JavaTypeMapper());
////        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
////        typeMapper.addTrustedPackages("com.leqtr.shared.event");
//
//
//        ErrorHandlingDeserializer<Object> errorHandlingDeserializer
//                = new ErrorHandlingDeserializer<>(valueDeserializer);
//
//        Map<String, Object> consumerConfigs = new HashMap<>();
//        consumerConfigs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092,localhost:9093,localhost:9094");
//        consumerConfigs.put(ConsumerConfig.GROUP_ID_CONFIG, "notification-group");
//
//        return new DefaultKafkaConsumerFactory<>(
//                consumerConfigs,
//                new StringDeserializer(),
//                errorHandlingDeserializer
//        );
//
//        // Normal configuration
////        Map<String, Object> consumerConfigs = new HashMap<>();
////        consumerConfigs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092,localhost:9093,localhost:9094");
////        consumerConfigs.put(ConsumerConfig.GROUP_ID_CONFIG, "notification-group");
////
////        consumerConfigs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
////        consumerConfigs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
////        consumerConfigs.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
////        consumerConfigs.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
////
////        consumerConfigs.put(JsonDeserializer.TRUSTED_PACKAGES, "com.leqtr.shared.event.base");
////
////        return new DefaultKafkaConsumerFactory<>(consumerConfigs);
//    }
//
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
//                new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory());
//        return factory;
//    }
}