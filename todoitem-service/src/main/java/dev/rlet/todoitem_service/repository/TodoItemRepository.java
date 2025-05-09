package dev.rlet.todoitem_service.repository;

import dev.rlet.todoitem_service.model.TodoItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TodoItemRepository extends MongoRepository<TodoItem, String> {

    @Query("{ 'title' : ?0 }")
    Optional<TodoItem> findByTitle(String title);

    @Query(value = "{ 'createdBy' : ?0 }", fields = "{ 'title' : 1, 'description' : 1 }")
    Optional<List<TodoItem>> findAll(String createdBy);

    long count();

    @Query(value = "{ 'createdBy' : ?0 }")
    Optional<List<TodoItem>> findAllByCreatedBy(String createdBy);

    List<TodoItem> findAllByCreatedByAndCompleteFalse(String createdBy);
    List<TodoItem> findAllByCreatedByAndCompleteTrue(String createdBy);
}
