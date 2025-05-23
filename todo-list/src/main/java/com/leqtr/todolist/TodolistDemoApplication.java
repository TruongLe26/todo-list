package com.leqtr.todolist;

import com.leqtr.todolist.configuration.KafkaProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "com.leqtr.todolist")
@EnableCaching
@EnableFeignClients
@EnableConfigurationProperties(KafkaProperties.class)
public class TodolistDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodolistDemoApplication.class, args);
    }

}
