package com.leqtr.todolist.service;

import com.leqtr.shared.dto.TodoItemDTO;
import com.leqtr.todolist.configuration.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaService {

    private final KafkaTemplate<String, com.leqtr.shared.dto.TodoItemDTO> kafkaTemplate;
    private final SecurityUtil securityUtil;

    public void createTodoItem(TodoItemDTO todoItemDTO) {
        String username = securityUtil.getSessionUser();
        todoItemDTO.setCreatedBy(username);
        todoItemDTO.setUpdatedBy(username);
        kafkaTemplate.send("todo.create", todoItemDTO);
    }
}
