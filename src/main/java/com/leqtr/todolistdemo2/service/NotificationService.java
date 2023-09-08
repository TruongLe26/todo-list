package com.leqtr.todolistdemo2.service;

import com.leqtr.todolistdemo2.model.Notification;

import java.util.List;

public interface NotificationService {

    void saveNotification(Notification notification);
    List<Notification> getNotifications();

}
