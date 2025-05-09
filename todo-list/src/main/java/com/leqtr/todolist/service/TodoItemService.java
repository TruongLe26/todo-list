package com.leqtr.todolist.service;

import com.leqtr.shared.dto.TodoItemDTO;
import org.springframework.data.domain.Page;
//import com.leqtr.todolist.model.TodoItem;
//import org.springframework.data.domain.Page;

import java.util.List;

public interface TodoItemService {
    List<TodoItemDTO> fetchTodoItemsForUser(String userId);
    TodoItemDTO getTodoItemById(String id);
    void deleteTodoItemById(String id);
    Page<TodoItemDTO> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);

    List<TodoItemDTO> getUncompletedTodoItem();
    List<TodoItemDTO> getCompletedTodoItem();
//
//    List<TodoItem> getUncompletedTodoItem();
//    List<TodoItem> getCompletedTodoItem();
//    TodoItem saveTodoItem(TodoItem todoItem);
//    TodoItem getTodoItemById(long id);
//    void deleteTodoItemById(long id);
//    Page<TodoItem> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);
//
}
