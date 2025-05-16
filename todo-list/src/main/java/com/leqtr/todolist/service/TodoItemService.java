package com.leqtr.todolist.service;

import com.leqtr.shared.dto.TodoItemDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TodoItemService {
    List<TodoItemDTO> fetchTodoItemsForUser(String userId);
    TodoItemDTO getTodoItemById(String id);
    void updateTodoItem(TodoItemDTO todoItemDTO);
    void deleteTodoItemById(String id);
    Page<TodoItemDTO> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);
    List<TodoItemDTO> getUncompletedTodoItem();
    List<TodoItemDTO> getCompletedTodoItem();
    void deleteTodoItems(List<String> selectedIds);
}
