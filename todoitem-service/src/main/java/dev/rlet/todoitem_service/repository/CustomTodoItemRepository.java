package dev.rlet.todoitem_service.repository;

import com.leqtr.shared.dto.TodoItemDTO;

public interface CustomTodoItemRepository {
    void updateTodoItem(TodoItemDTO todoItemDTO);
}
