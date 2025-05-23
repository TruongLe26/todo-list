package com.leqtr.todolist.service;

import com.leqtr.shared.dto.TodoItemDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TodoItemService {
    TodoItemDTO getTodoItemById(String id);
    void createTodoItem(TodoItemDTO todoItemDTO);
    void updateTodoItem(TodoItemDTO todoItemDTO);
    void deleteTodoItemById(String id);
    Page<TodoItemDTO> findPaginatedUncompletedTodoItems(int pageNo, int pageSize, String sortField, String sortDirection);
    List<TodoItemDTO> getCompletedTodoItem();
    void deleteTodoItems(List<String> selectedIds);
}
