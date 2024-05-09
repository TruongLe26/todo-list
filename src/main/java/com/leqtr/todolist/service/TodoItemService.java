package com.leqtr.todolist.service;

import com.leqtr.todolist.model.TodoItem;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TodoItemService {

    List<TodoItem> getUncompletedTodoItem();
    List<TodoItem> getCompletedTodoItem();
    TodoItem saveTodoItem(TodoItem todoItem);
    TodoItem getTodoItemById(long id);
    void deleteTodoItemById(long id);
    Page<TodoItem> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);

}
