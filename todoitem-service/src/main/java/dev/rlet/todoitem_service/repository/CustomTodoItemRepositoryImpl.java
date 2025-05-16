package dev.rlet.todoitem_service.repository;

import com.leqtr.shared.dto.TodoItemDTO;
import com.mongodb.client.result.UpdateResult;
import dev.rlet.todoitem_service.model.TodoItem;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CustomTodoItemRepositoryImpl implements CustomTodoItemRepository {

    private static final Logger logger = LoggerFactory.getLogger(CustomTodoItemRepositoryImpl.class);

    private final MongoTemplate mongoTemplate;

    @Override
    public void updateTodoItem(TodoItemDTO todoItemDTO) {
        Query query = new Query(Criteria.where("_id").is(todoItemDTO.getId()));
        Update update = new Update();

        if (todoItemDTO.getTitle() != null)
            update.set("title", todoItemDTO.getTitle());
        if (todoItemDTO.getDescription() != null)
            update.set("description", todoItemDTO.getDescription());
        if (todoItemDTO.getComplete() != null)
            update.set("complete", todoItemDTO.getComplete());

        if (update.getUpdateObject().isEmpty()) {
            logger.info("No fields to update for TodoItem with ID: {}", todoItemDTO.getId());
            return;
        }

        update.set("updatedOn", LocalDateTime.now());

        UpdateResult result = mongoTemplate.updateFirst(query, update, TodoItem.class);
        if (result.getModifiedCount() == 0) {
            logger.warn("No TodoItem found with id: {}", todoItemDTO.getId());
        } else {
            logger.info("{} document(s) updated for TodoItem id: {}", result.getModifiedCount(), todoItemDTO.getId());
        }
    }
}