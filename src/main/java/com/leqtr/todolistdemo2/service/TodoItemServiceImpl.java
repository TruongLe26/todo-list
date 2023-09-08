package com.leqtr.todolistdemo2.service;

import com.leqtr.todolistdemo2.config.SecurityUtil;
import com.leqtr.todolistdemo2.model.TodoItem;
import com.leqtr.todolistdemo2.model.User;
import com.leqtr.todolistdemo2.repository.TodoItemRepository;
import com.leqtr.todolistdemo2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TodoItemServiceImpl implements TodoItemService {

    @Autowired
    private TodoItemRepository todoItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityUtil securityUtil;

    @Override
    public List<TodoItem> getUncompletedTodoItem() {
        String username = securityUtil.getSessionUser();
        return todoItemRepository.findUncompletedTodoItemByUsername(username);
    }

    @Override
    public List<TodoItem> getCompletedTodoItem() {
        String username = securityUtil.getSessionUser();
        return todoItemRepository.findCompletedTodoItemByUsername(username);
    }

    @Override
    public void saveTodoItem(TodoItem todoItem) {
        String username = securityUtil.getSessionUser();
        User user = userRepository.findByEmail(username);
        todoItem.setUser(user);
        todoItem.setUpdUser(user);
        todoItemRepository.save(todoItem);
    }

    @Override
    public TodoItem getTodoItemById(long id) {
        Optional<TodoItem> optional = todoItemRepository.findById(id);
        TodoItem todoItem = null;
        if (optional.isPresent()) {
            todoItem = optional.get();
        } else {
            throw new RuntimeException("Todo item not found for id: " + id);
        }
        return todoItem;
    }

    @Override
    public void deleteTodoItemById(long id) {
        todoItemRepository.deleteById(id);
    }

    @Override
    public Page<TodoItem> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        List<TodoItem> curList = getUncompletedTodoItem();
        return new PageImpl<>(curList.subList(
                (int) pageable.getOffset(),
                Math.min(
                        (int) pageable.getOffset() + pageable.getPageSize(),
                        curList.size()
                )
        ), pageable, curList.size());
    }

}
