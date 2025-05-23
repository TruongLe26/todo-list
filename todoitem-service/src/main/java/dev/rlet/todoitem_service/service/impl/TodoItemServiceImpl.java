package dev.rlet.todoitem_service.service.impl;

import com.leqtr.shared.dto.TodoItemDTO;
import com.leqtr.shared.event.todo.TodoItemCreatedEvent;
import com.leqtr.shared.event.todo.TodoItemDeletedEvent;
import com.leqtr.shared.event.todo.TodoItemUpdatedEvent;
import dev.rlet.todoitem_service.configuration.KafkaTopicConfiguration;
import dev.rlet.todoitem_service.mapper.TodoItemMapper;
import dev.rlet.todoitem_service.model.TodoItem;
import dev.rlet.todoitem_service.repository.TodoItemRepository;
import dev.rlet.todoitem_service.service.TodoItemService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TodoItemServiceImpl implements TodoItemService {

    private final Logger logger = LoggerFactory.getLogger(TodoItemServiceImpl.class);
    private final TodoItemRepository todoItemRepository;
    private final TodoItemMapper todoItemMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void createTodoItem(TodoItemDTO todoItemDTO) {
        TodoItem savedTodoItem = todoItemRepository.save(todoItemMapper.toEntity(todoItemDTO));

        TodoItemCreatedEvent event = new TodoItemCreatedEvent(
                savedTodoItem.getCreatedBy(),
                savedTodoItem.getId(),
                savedTodoItem.getTitle()
        );

        logger.info("Sending TodoItemCreated event to topic: {}", KafkaTopicConfiguration.USER_NOTIFICATION_TOPIC);
        kafkaTemplate.send(
                KafkaTopicConfiguration.USER_NOTIFICATION_TOPIC,
                savedTodoItem.getCreatedBy(),
                event
        );
    }

    public List<TodoItemDTO> getTodoItemByUserId(String userId) {
        List<TodoItem> todoItems = todoItemRepository.findAllByCreatedBy(userId)
                .orElseThrow();
        return todoItemMapper.toDtoList(todoItems);
    }

    public TodoItemDTO getTodoItemById(String id) {
        TodoItem todoItem = todoItemRepository.findById(id)
                .orElseThrow();
        return todoItemMapper.toDto(todoItem);
    }

    public List<TodoItemDTO> getTodoItemsByGroupId(Long groupId) {
        List<TodoItem> todoItems = todoItemRepository.findAllByGroupId(groupId)
                .orElseThrow();
        return todoItemMapper.toDtoList(todoItems);
    }

    public void updateTodoItem(TodoItemDTO todoItemDTO) {
        TodoItem originalTodoItem = todoItemRepository.findById(todoItemDTO.getId())
                .orElseThrow();
        todoItemRepository.updateTodoItem(todoItemDTO);

        List<TodoItemUpdatedEvent.FieldChange> changes = new ArrayList<>();
        if (!Objects.equals(originalTodoItem.getTitle(), todoItemDTO.getTitle())) {
            changes.add(new TodoItemUpdatedEvent.FieldChange(
                    "title",
                    originalTodoItem.getTitle(),
                    todoItemDTO.getTitle()
            ));
        }
        if (!Objects.equals(originalTodoItem.getDescription(), todoItemDTO.getDescription())) {
            changes.add(new TodoItemUpdatedEvent.FieldChange(
                    "description",
                    originalTodoItem.getDescription(),
                    todoItemDTO.getDescription()
            ));
        }
        if (Boolean.compare(originalTodoItem.getComplete(), todoItemDTO.getComplete()) != 0) {
            changes.add(new TodoItemUpdatedEvent.FieldChange(
                    "complete",
                    String.valueOf(originalTodoItem.getComplete()),
                    String.valueOf(todoItemDTO.getComplete())
            ));
        }

        if (!changes.isEmpty()) {
            TodoItemUpdatedEvent event = new TodoItemUpdatedEvent(
                    originalTodoItem.getCreatedBy(),
                    originalTodoItem.getId(),
                    changes
            );
            logger.info("Sending TodoItemUpdated event to topics: {}",
                    KafkaTopicConfiguration.USER_NOTIFICATION_TOPIC
//                    KafkaTopicConfiguration.TODO_ITEM_CACHE_INVALIDATION_TOPIC
            );
            kafkaTemplate.send(
                    KafkaTopicConfiguration.USER_NOTIFICATION_TOPIC,
                    originalTodoItem.getCreatedBy(),
                    event
            );
        }
    }

    public void deleteTodoItemById(String id) {
        TodoItem todoItem = todoItemRepository.findById(id)
                .orElseThrow();
        String todoItemTitle = todoItem.getTitle();
        String todoItemOwner = todoItem.getCreatedBy();
        todoItemRepository.deleteById(id);

        TodoItemDeletedEvent event = new TodoItemDeletedEvent(todoItemOwner, id, todoItemTitle);
        logger.info("Sending TodoItemDeleted event to topic: {}", KafkaTopicConfiguration.USER_NOTIFICATION_TOPIC);
        kafkaTemplate.send(
                KafkaTopicConfiguration.USER_NOTIFICATION_TOPIC,
                todoItemOwner,
                event
        );
        kafkaTemplate.send(
                KafkaTopicConfiguration.TODO_ITEM_CACHE_INVALIDATION_TOPIC,
                todoItemOwner,
                event
        );
    }

    public List<TodoItemDTO> findUncompletedTodoItemByUsername(String username) {
        List<TodoItem> todoItems = todoItemRepository.findUncompletedUserTodoItems(username);
        return todoItemMapper.toDtoList(todoItems);
    }

    public List<TodoItemDTO> findCompletedTodoItemByUsername(String username) {
        List<TodoItem> todoItems = todoItemRepository.findCompletedUserTodoItems(username);
        return todoItemMapper.toDtoList(todoItems);
    }

    public void deleteTodoItems(List<String> selectedIds) {
        if (selectedIds == null || selectedIds.isEmpty()) {
            logger.warn("No item IDs provided for deletion.");
            return;
        }

        try {
            List<TodoItem> itemsToDelete = todoItemRepository.findAllById(selectedIds);

            if (itemsToDelete.isEmpty()) {
                logger.warn("No todo items found matching the provided IDs: {}", selectedIds);
            }

            todoItemRepository.deleteAll(itemsToDelete);
            logger.info("Successfully deleted {} todo item(s).", itemsToDelete.size());

        } catch (Exception ex) {
            logger.error("Error occurred while deleting todo items: {}", ex.getMessage(), ex);
        }
    }

    public void deleteTodoItemFromGroup(Long groupId, String todoItemId) {
        TodoItem todoItem = todoItemRepository.findById(todoItemId)
                .orElseThrow();

        if (todoItem.getGroupId() != null && todoItem.getGroupId().equals(groupId)) {
            todoItem.setGroupId(null);
            todoItemRepository.save(todoItem);
        } else {
            logger.warn("Todo item with ID {} does not belong to group {}", todoItemId, groupId);
        }
    }
}