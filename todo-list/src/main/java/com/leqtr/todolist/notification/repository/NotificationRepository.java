package com.leqtr.todolist.notification.repository;

import com.leqtr.todolist.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUsername(String username);
}