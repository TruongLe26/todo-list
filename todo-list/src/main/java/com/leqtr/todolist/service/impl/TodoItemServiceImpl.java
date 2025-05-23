package com.leqtr.todolist.service.impl;

import com.leqtr.shared.dto.TodoItemDTO;
import com.leqtr.todolist.annotation.CacheEvictAfter;
import com.leqtr.todolist.configuration.SecurityUtil;
import com.leqtr.todolist.service.TodoItemClient;
import com.leqtr.todolist.service.TodoItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoItemServiceImpl implements TodoItemService {

    private final SecurityUtil securityUtil;
    private final TodoItemClient todoItemClient;
    private final TodoItemCacheServiceImpl todoItemCacheServiceImpl;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    @Cacheable(value = "todoItemById", key = "#id")
    public TodoItemDTO getTodoItemById(String id) {
        return todoItemClient.getTodoItemById(id);
    }

    @CacheEvictAfter
    public void createTodoItem(TodoItemDTO todoItemDTO) {
        String username = securityUtil.getSessionUser();
        todoItemDTO.setCreatedBy(username);
        todoItemDTO.setUpdatedBy(username);
        todoItemClient.createTodoItem(todoItemDTO);
    }

    @Override
    @CacheEvictAfter
    public void updateTodoItem(TodoItemDTO todoItemDTO) {
        todoItemClient.updateTodoItem(todoItemDTO);
    }

    @Override
    @CacheEvictAfter
    public void deleteTodoItemById(String id) {
        todoItemClient.deleteTodoItemById(id);
    }

    public List<TodoItemDTO> getCompletedTodoItem() {
        String username = securityUtil.getSessionUser();
        return todoItemClient.getCompletedTodoItemByUsername(username);
    }

    @Override
    public void deleteTodoItems(List<String> selectedIds) {
        String username = securityUtil.getSessionUser();
        redisTemplate.delete("todoItems::uncompleted" + username);
        redisTemplate.delete("todoItems::completed" + username);
        for (String id : selectedIds) {
            redisTemplate.delete("todoItemById::" + id);
        }

        todoItemClient.deleteTodoItems(selectedIds);
    }

    //    @Override
    public Page<TodoItemDTO> findPaginatedUncompletedTodoItems(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

        String username = securityUtil.getSessionUser();
        List<TodoItemDTO> curList = todoItemCacheServiceImpl.getUncompletedTodoItem(username);

        Comparator<TodoItemDTO> comparator = switch (sortField) {
            case "title" -> Comparator.comparing(TodoItemDTO::getTitle, String.CASE_INSENSITIVE_ORDER);
            case "description" -> Comparator.comparing(TodoItemDTO::getDescription, String.CASE_INSENSITIVE_ORDER);
            case "createdOn" -> Comparator.comparing(TodoItemDTO::getCreatedOn);
            case "updatedOn" -> Comparator.comparing(TodoItemDTO::getUpdatedOn);
            default -> Comparator.comparing(TodoItemDTO::getTitle);
        };
        if (sortDirection.equalsIgnoreCase("desc")) {
            comparator = comparator.reversed();
        }
        curList.sort(comparator);

        return new PageImpl<>(curList.subList(
                (int) pageable.getOffset(),
                Math.min((int) pageable.getOffset() + pageable.getPageSize(),
                        curList.size())), pageable, curList.size()
        );
    }
}
