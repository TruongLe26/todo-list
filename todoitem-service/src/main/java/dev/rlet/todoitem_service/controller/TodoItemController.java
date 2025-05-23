package dev.rlet.todoitem_service.controller;

import com.leqtr.shared.dto.TodoItemDTO;
import dev.rlet.todoitem_service.service.impl.TodoItemServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todoitems")
@RequiredArgsConstructor
public class TodoItemController {

    private final TodoItemServiceImpl todoItemServiceImpl;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createTodoItem(@RequestBody TodoItemDTO todoItemDTO) {
        todoItemServiceImpl.createTodoItem(todoItemDTO);
    }

    @GetMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<TodoItemDTO> getTodoItemsByUser(@PathVariable String userId) {
        return todoItemServiceImpl.getTodoItemByUserId(userId);
    }

    @GetMapping("/group/{groupId}")
    @ResponseStatus(HttpStatus.OK)
    public List<TodoItemDTO> getTodoItemsByGroup(@PathVariable Long groupId) {
        return todoItemServiceImpl.getTodoItemsByGroupId(groupId);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TodoItemDTO getTodoItemById(@PathVariable String id) {
        return todoItemServiceImpl.getTodoItemById(id);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateTodoItem(@RequestBody TodoItemDTO todoItemDTO) {
        todoItemServiceImpl.updateTodoItem(todoItemDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTodoItemById(@PathVariable String id) {
        todoItemServiceImpl.deleteTodoItemById(id);
    }

    @DeleteMapping("/batch-delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTodoItems(@RequestBody List<String> selectedIds) {
        todoItemServiceImpl.deleteTodoItems(selectedIds);
    }

    @GetMapping("/uncompleted/{username}")
    @ResponseStatus(HttpStatus.OK)
    public List<TodoItemDTO> getUncompletedTodoItemByUsername(@PathVariable String username) {
        return todoItemServiceImpl.findUncompletedTodoItemByUsername(username);
    }
    @GetMapping("/completed/{username}")
    @ResponseStatus(HttpStatus.OK)
    public List<TodoItemDTO> getCompletedTodoItemByUsername(@PathVariable String username) {
        return todoItemServiceImpl.findCompletedTodoItemByUsername(username);
    }

    @DeleteMapping("/group/{groupId}/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTodoItemFromGroup(@PathVariable Long groupId,
                                        @PathVariable String id) {
        todoItemServiceImpl.deleteTodoItemFromGroup(groupId, id);
    }
}