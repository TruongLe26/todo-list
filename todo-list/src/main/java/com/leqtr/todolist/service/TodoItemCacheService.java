package com.leqtr.todolist.service;

import com.leqtr.shared.dto.TodoItemDTO;

import java.util.List;

public interface TodoItemCacheService {
    List<TodoItemDTO> getUncompletedTodoItem(String username);
    void invalidateAllForUser(String username, String itemId);
    void invalidateAllForUser(String username);
}
