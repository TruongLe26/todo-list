package com.leqtr.todolist.service;

import com.leqtr.todolist.model.Notification;

import java.util.List;

public interface NotificationService {

    void saveNotification(Notification notification);
    List<Notification> getNotifications();

}
