package com.leqtr.todolist.service;

import com.leqtr.shared.dto.TodoItemDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "todoitem-service", url = "http://localhost:8081")
public interface TodoItemClient {
    @GetMapping("/todoitems/user/{userId}")
    List<TodoItemDTO> getTodoItemsByUserId(@PathVariable("userId") String userId);
    @GetMapping("/todoitems/group/{groupId}")
    List<TodoItemDTO> getTodoItemsByGroupId(@PathVariable("groupId") Long groupId);
    @GetMapping("/todoitems/{id}")
    TodoItemDTO getTodoItemById(@PathVariable("id") String id);
    @PostMapping("/todoitems")
    void updateTodoItem(@RequestBody TodoItemDTO todoItemDTO);
    @DeleteMapping("/todoitems/{id}")
    void deleteTodoItemById(@PathVariable("id") String id);
    @DeleteMapping("/todoitems/batch-delete")
    void deleteTodoItems(@RequestBody List<String> selectedIds);
    @GetMapping("/todoitems/uncompleted/{username}")
    List<TodoItemDTO> getUncompletedTodoItemByUsername(@PathVariable("username") String username);
    @GetMapping("/todoitems/completed/{username}")
    List<TodoItemDTO> getCompletedTodoItemByUsername(@PathVariable("username") String username);
    @DeleteMapping("/todoitems/group/{groupId}/{id}")
    void deleteTodoItemFromGroup(@PathVariable("groupId") long groupId, @PathVariable("id") String id);
}
