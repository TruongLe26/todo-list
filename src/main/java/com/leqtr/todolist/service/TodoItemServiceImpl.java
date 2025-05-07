package com.leqtr.todolist.service;

import com.leqtr.todolist.configuration.SecurityUtil;
import com.leqtr.todolist.model.TodoItem;
import com.leqtr.todolist.model.User;
import com.leqtr.todolist.repository.TodoItemRepository;
import com.leqtr.todolist.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TodoItemServiceImpl implements TodoItemService {

    private final TodoItemRepository todoItemRepository;

    private final UserRepository userRepository;

    private final SecurityUtil securityUtil;

    @Override
//    @Cacheable(value = "uncompletedTodoItem")
    public List<TodoItem> getUncompletedTodoItem() {
//        try {
//            System.out.println("Retrieving uncompleted to-do items...");
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        String username = securityUtil.getSessionUser();
        return todoItemRepository.findUncompletedTodoItemByUsername(username);
    }

    @Override
//    @Cacheable(value = "completedTodoItem")
    public List<TodoItem> getCompletedTodoItem() {
//        try {
//            System.out.println("Retrieving uncompleted to-do items...");
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        String username = securityUtil.getSessionUser();
        return todoItemRepository.findCompletedTodoItemByUsername(username);
    }

    @Override
//    @CachePut(value = "SavedTodoItem")
    public TodoItem saveTodoItem(TodoItem todoItem) {
        String username = securityUtil.getSessionUser();
        User user = userRepository.findByEmail(username);
        todoItem.setUser(user);
        todoItem.setUpdUser(user);
        return todoItemRepository.save(todoItem);
    }

    @Override
//    @Cacheable(value = "todoItemById", key = "#id")
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
//    @CacheEvict(value = "DeletedTodoItem", key = "#id")
    public void deleteTodoItemById(long id) {
        todoItemRepository.deleteById(id);
    }

    @Override
//    @Cacheable(value = "FoundTodoItems")
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
