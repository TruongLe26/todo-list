package dev.rlet.todoitem_service.service;

import com.leqtr.shared.dto.TodoItemDTO;
import dev.rlet.todoitem_service.model.TodoItem;
import dev.rlet.todoitem_service.repository.TodoItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoItemService {

    private final TodoItemRepository todoItemRepository;

    @KafkaListener(
            topics = "todo.create",
            groupId = "todoitem-service-test",
            containerFactory = "kafkaListenerContainerFactory")
    public void handleCreate(TodoItemDTO todoItemDTO) {
        System.out.println("Received TodoItemDTO: " + todoItemDTO);
        TodoItem todoItem = new TodoItem();
        todoItem.setTitle(todoItemDTO.getTitle());
        todoItem.setDescription(todoItemDTO.getDescription());
        todoItem.setCreatedBy(todoItemDTO.getCreatedBy());
        todoItem.setUpdatedBy(todoItemDTO.getUpdatedBy());
        todoItem.setGroupId(todoItemDTO.getGroupId());
        todoItem.setComplete(todoItemDTO.getComplete() != null ? todoItemDTO.getComplete() : false);
        todoItemRepository.save(todoItem);
    }

    public List<TodoItemDTO> getTodoItemByUserId(String userId) {
        List<TodoItem> todoItems = todoItemRepository.findAllByCreatedBy(userId)
                .orElseThrow();
        return todoItems.stream()
                .map(todoItem -> new TodoItemDTO(
                        todoItem.getTitle(),
                        todoItem.getDescription(),
                        todoItem.getCreatedBy(),
                        todoItem.getUpdatedBy(),
                        todoItem.getGroupId(),
                        todoItem.getComplete()))
                .toList();
    }

    public TodoItemDTO getTodoItemById(String id) {
        TodoItem todoItem = todoItemRepository.findById(id)
                .orElseThrow();
        return new TodoItemDTO(
                todoItem.getTitle(),
                todoItem.getDescription(),
                todoItem.getCreatedBy(),
                todoItem.getUpdatedBy(),
                todoItem.getGroupId(),
                todoItem.getComplete());
    }

    public void deleteTodoItemById(String id) {
        todoItemRepository.deleteById(id);
    }

    public List<TodoItemDTO> findUncompletedTodoItemByUsername(String username) {
        List<TodoItem> todoItems = todoItemRepository.findAllByCreatedByAndCompleteFalse(username);
        return todoItems.stream()
                .map(todoItem -> new TodoItemDTO(
                        todoItem.getTitle(),
                        todoItem.getDescription(),
                        todoItem.getCreatedBy(),
                        todoItem.getUpdatedBy(),
                        todoItem.getGroupId(),
                        todoItem.getComplete()))
                .toList();
    }
    public List<TodoItemDTO> findCompletedTodoItemByUsername(String username) {
        List<TodoItem> todoItems = todoItemRepository.findAllByCreatedByAndCompleteTrue(username);
        return todoItems.stream()
                .map(todoItem -> new TodoItemDTO(
                        todoItem.getTitle(),
                        todoItem.getDescription(),
                        todoItem.getCreatedBy(),
                        todoItem.getUpdatedBy(),
                        todoItem.getGroupId(),
                        todoItem.getComplete()))
                .toList();
    }
}
