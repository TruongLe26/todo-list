package dev.rlet.todoitem_service.repository;

public interface CustomTodoItemRepository {
    void updateTodoItemTitle(String id, String newTitle);
}
