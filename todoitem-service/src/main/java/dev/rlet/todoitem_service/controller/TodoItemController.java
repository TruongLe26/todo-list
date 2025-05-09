package dev.rlet.todoitem_service.controller;

import com.leqtr.shared.dto.TodoItemDTO;
import dev.rlet.todoitem_service.service.TodoItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todoitems")
@RequiredArgsConstructor
public class TodoItemController {

    private final TodoItemService todoItemService;

    @GetMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<TodoItemDTO> getTodoItemsByUser(@PathVariable String userId) {
        return todoItemService.getTodoItemByUserId(userId);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TodoItemDTO getTodoItemById(@PathVariable String id) {
        return todoItemService.getTodoItemById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTodoItemById(@PathVariable String id) {
        todoItemService.deleteTodoItemById(id);
    }

    @GetMapping("/uncompleted/{username}")
    @ResponseStatus(HttpStatus.OK)
    public List<TodoItemDTO> getUncompletedTodoItemByUsername(@PathVariable String username) {
        return todoItemService.findUncompletedTodoItemByUsername(username);
    }
    @GetMapping("/completed/{username}")
    @ResponseStatus(HttpStatus.OK)
    public List<TodoItemDTO> getCompletedTodoItemByUsername(@PathVariable String username) {
        return todoItemService.findCompletedTodoItemByUsername(username);
    }
}
