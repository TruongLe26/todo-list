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

    @GetMapping("/group/{groupId}")
    @ResponseStatus(HttpStatus.OK)
    public List<TodoItemDTO> getTodoItemsByGroup(@PathVariable Long groupId) {
        return todoItemService.getTodoItemsByGroupId(groupId);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TodoItemDTO getTodoItemById(@PathVariable String id) {
        return todoItemService.getTodoItemById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateTodoItem(@RequestBody TodoItemDTO todoItemDTO) {
        todoItemService.updateTodoItem(todoItemDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTodoItemById(@PathVariable String id) {
        todoItemService.deleteTodoItemById(id);
    }

    @DeleteMapping("/batch-delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTodoItems(@RequestBody List<String> selectedIds) {
        todoItemService.deleteTodoItems(selectedIds);
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

    @DeleteMapping("/group/{groupId}/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTodoItemFromGroup(@PathVariable Long groupId,
                                        @PathVariable String id) {
        todoItemService.deleteTodoItemFromGroup(groupId, id);
    }
}