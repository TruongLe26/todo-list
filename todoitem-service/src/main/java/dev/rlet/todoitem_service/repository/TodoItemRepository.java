package dev.rlet.todoitem_service.repository;

import dev.rlet.todoitem_service.model.TodoItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TodoItemRepository extends MongoRepository<TodoItem, String>, CustomTodoItemRepository {
    long count();
    @Query(value = "{ 'createdBy' : ?0, 'groupId' : null }")
    Optional<List<TodoItem>> findAllByCreatedBy(String createdBy);
    @Query(value = "{ 'groupId' : ?0 }")
    Optional<List<TodoItem>> findAllByGroupId(Long groupId);
    @Query(value = "{ 'createdBy' : ?0, 'complete' : false, 'groupId' : null }")
    List<TodoItem> findUncompletedUserTodoItems(String createdBy);
    @Query(value = "{ 'createdBy' : ?0, 'complete' : true, 'groupId' : null }")
    List<TodoItem> findCompletedUserTodoItems(String createdBy);
}