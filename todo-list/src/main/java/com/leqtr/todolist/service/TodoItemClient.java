package com.leqtr.todolist.service;

import com.leqtr.shared.dto.TodoItemDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "todoitem-service", url = "http://localhost:8081")
public interface TodoItemClient {
    @GetMapping("/todoitems/user/{userId}")
    List<TodoItemDTO> getTodoItemsByUserId(@PathVariable("userId") String userId);
    @GetMapping("/todoitems/{id}")
    TodoItemDTO getTodoItemById(@PathVariable("id") String id);
    @DeleteMapping("/todoitems/{id}")
    void deleteTodoItemById(@PathVariable("id") String id);
    @GetMapping("/todoitems/uncompleted/{username}")
    List<TodoItemDTO> getUncompletedTodoItemByUsername(@PathVariable("username") String username);
    @GetMapping("/todoitems/completed/{username}")
    List<TodoItemDTO> getCompletedTodoItemByUsername(@PathVariable("username") String username);
}
