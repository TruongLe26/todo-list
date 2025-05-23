package dev.rlet.todoitem_service;

import dev.rlet.todoitem_service.configuration.KafkaProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableMongoRepositories
@EnableKafka
@EnableConfigurationProperties(KafkaProperties.class)
public class TodoitemServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodoitemServiceApplication.class, args);
	}
}
