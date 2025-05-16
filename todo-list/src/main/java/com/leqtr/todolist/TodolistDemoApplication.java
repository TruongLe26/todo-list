package com.leqtr.todolist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "com.leqtr.todolist")
@EnableCaching
@EnableFeignClients
public class TodolistDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodolistDemoApplication.class, args);
    }

}
