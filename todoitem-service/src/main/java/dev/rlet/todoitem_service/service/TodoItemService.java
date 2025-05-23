package dev.rlet.todoitem_service.service;

import com.leqtr.shared.dto.TodoItemDTO;

import java.util.List;

public interface TodoItemService {
    void createTodoItem(TodoItemDTO todoItemDTO);
    List<TodoItemDTO> getTodoItemByUserId(String userId);
    TodoItemDTO getTodoItemById(String id);
    List<TodoItemDTO> getTodoItemsByGroupId(Long groupId);
    void updateTodoItem(TodoItemDTO todoItemDTO);
    void deleteTodoItemById(String id);
    List<TodoItemDTO> findUncompletedTodoItemByUsername(String username);
    List<TodoItemDTO> findCompletedTodoItemByUsername(String username);
    void deleteTodoItems(List<String> selectedIds);
    void deleteTodoItemFromGroup(Long groupId, String todoItemId);
}
