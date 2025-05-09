//package com.leqtr.todolist.repository;
//
//import com.leqtr.todolist.model.TodoItem;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//
//import java.util.List;
//
//public interface TodoItemRepository extends JpaRepository<TodoItem, Long> {
//
//    @Query("SELECT t FROM TodoItem t WHERE t.user.email = :username AND t.inGroup IS NULL AND t.complete = false ORDER BY t.createdOn DESC")
//    List<TodoItem> findUncompletedTodoItemByUsername(String username);
//
//    @Query("SELECT t FROM TodoItem t WHERE t.user.email = :username AND t.inGroup IS NULL AND t.complete = true ORDER BY t.completedOn DESC")
//    List<TodoItem> findCompletedTodoItemByUsername(String username);
//
//}
