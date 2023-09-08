package com.leqtr.todolistdemo2.service;

import com.leqtr.todolistdemo2.model.TodoItem;
import com.leqtr.todolistdemo2.model.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TodoItemService {

    List<TodoItem> getUncompletedTodoItem();
    List<TodoItem> getCompletedTodoItem();
    void saveTodoItem(TodoItem todoItem);
    TodoItem getTodoItemById(long id);
    void deleteTodoItemById(long id);
    Page<TodoItem> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);

}
