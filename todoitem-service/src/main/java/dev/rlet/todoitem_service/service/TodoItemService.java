package dev.rlet.todoitem_service.service;

import com.leqtr.shared.dto.TodoItemDTO;
import dev.rlet.todoitem_service.mapper.TodoItemMapper;
import dev.rlet.todoitem_service.model.TodoItem;
import dev.rlet.todoitem_service.repository.TodoItemRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoItemService {

    private final Logger logger = LoggerFactory.getLogger(TodoItemService.class);
    private final TodoItemRepository todoItemRepository;
    private final TodoItemMapper todoItemMapper;

    @KafkaListener(
            topics = "todo.create",
            groupId = "todoitem-service-test",
            containerFactory = "kafkaListenerContainerFactory")
    public void handleCreate(TodoItemDTO todoItemDTO) {
        TodoItem todoItem = todoItemMapper.toEntity(todoItemDTO);
        if (todoItem.getComplete() == null) {
            todoItem.setComplete(Boolean.FALSE);
        }
        todoItemRepository.save(todoItem);
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
        todoItemRepository.updateTodoItem(todoItemDTO);
    }

    public void deleteTodoItemById(String id) {
        todoItemRepository.deleteById(id);
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
