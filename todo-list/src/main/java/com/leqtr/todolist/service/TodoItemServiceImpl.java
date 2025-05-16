package com.leqtr.todolist.service;

import com.leqtr.shared.dto.TodoItemDTO;
import com.leqtr.todolist.configuration.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoItemServiceImpl implements TodoItemService {

    private final SecurityUtil securityUtil;
    private final TodoItemClient todoItemClient;

    public List<TodoItemDTO> fetchTodoItemsForUser(String userId) {
        return todoItemClient.getTodoItemsByUserId(userId);
    }

    @Override
    public TodoItemDTO getTodoItemById(String id) {
        return todoItemClient.getTodoItemById(id);
    }

    @Override
    public void updateTodoItem(TodoItemDTO todoItemDTO) {
        todoItemClient.updateTodoItem(todoItemDTO);
    }

    @Override
    public void deleteTodoItemById(String id) {
        todoItemClient.deleteTodoItemById(id);
    }

//    @Cacheable(value = "uncompletedTodoItem")
    public List<TodoItemDTO> getUncompletedTodoItem() {
        String username = securityUtil.getSessionUser();
        return todoItemClient.getUncompletedTodoItemByUsername(username);
    }

//    @Cacheable(value = "completedTodoItem")
    public List<TodoItemDTO> getCompletedTodoItem() {
        String username = securityUtil.getSessionUser();
        return todoItemClient.getCompletedTodoItemByUsername(username);
    }

    @Override
    public void deleteTodoItems(List<String> selectedIds) {
        todoItemClient.deleteTodoItems(selectedIds);
    }

    //    @Override
//    @Cacheable(value = "FoundTodoItems")
    public Page<TodoItemDTO> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        List<TodoItemDTO> curList = getUncompletedTodoItem();
        return new PageImpl<>(curList.subList(
                (int) pageable.getOffset(),
                Math.min((int) pageable.getOffset() + pageable.getPageSize(),
                        curList.size())), pageable, curList.size()
        );
    }
}
