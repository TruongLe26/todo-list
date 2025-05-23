package com.leqtr.todolist.service.impl;

import com.leqtr.shared.dto.TodoItemDTO;
import com.leqtr.todolist.service.TodoItemCacheService;
import com.leqtr.todolist.service.TodoItemClient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoItemCacheServiceImpl implements TodoItemCacheService {

    private final TodoItemClient todoItemClient;
    private final RedisTemplate<String, Object> redisTemplate;
    private final Logger logger = LoggerFactory.getLogger(TodoItemCacheServiceImpl.class);

    @Cacheable(value = "todoItems", key = "'uncompleted:' + #username")
    public List<TodoItemDTO> getUncompletedTodoItem(String username) {
        return todoItemClient.getUncompletedTodoItemByUsername(username);
    }

    public void invalidateAllForUser(String username, String itemId) {
        logger.info("Invalidating cache for user: {}, itemId: {}", username, itemId);
        redisTemplate.delete("todoItemsByUser::" + username);
        redisTemplate.delete("todoItemById::" + itemId);
        redisTemplate.delete("todoItems::uncompleted:" + username);
        logger.info("Cache invalidated for user: {}, itemId: {}", username, itemId);
    }

    public void invalidateAllForUser(String username) {
        logger.info("Invalidating cache for user: {}", username);
        redisTemplate.delete("todoItemsByUser::" + username);
        redisTemplate.delete("todoItems::uncompleted:" + username);
        logger.info("Cache invalidated for user: {}", username);
    }
}