package com.leqtr.todolistdemo2.service;

import com.leqtr.todolistdemo2.config.SecurityUtil;
import com.leqtr.todolistdemo2.model.Notification;
import com.leqtr.todolistdemo2.model.User;
import com.leqtr.todolistdemo2.repository.NotificationRepository;
import com.leqtr.todolistdemo2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private SecurityUtil securityUtil;

    @Override
    public void saveNotification(Notification notification) {
        notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getNotifications() {
        String email = securityUtil.getSessionUser();
        User curUser = userRepository.findByEmail(email);

        return notificationRepository.getNotificationsById(curUser.getId());
    }

}
