package com.leqtr.todolist.service;

import com.leqtr.todolist.configuration.SecurityUtil;
import com.leqtr.todolist.model.Notification;
import com.leqtr.todolist.model.User;
import com.leqtr.todolist.repository.NotificationRepository;
import com.leqtr.todolist.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final UserRepository userRepository;

    private final NotificationRepository notificationRepository;

    private final SecurityUtil securityUtil;

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
