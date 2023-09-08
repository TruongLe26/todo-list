package com.leqtr.todolistdemo2.repository;

import com.leqtr.todolistdemo2.model.TodoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TodoItemRepository extends JpaRepository<TodoItem, Long> {

    @Query("SELECT t FROM TodoItem t WHERE t.user.email = :username AND t.inGroup IS NULL AND t.complete = false ORDER BY t.createdOn DESC")
    List<TodoItem> findUncompletedTodoItemByUsername(String username);

    @Query("SELECT t FROM TodoItem t WHERE t.user.email = :username AND t.inGroup IS NULL AND t.complete = true ORDER BY t.completedOn DESC")
    List<TodoItem> findCompletedTodoItemByUsername(String username);

}
