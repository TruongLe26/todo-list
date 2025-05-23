package com.leqtr.todolist.notification.service;

import com.leqtr.todolist.dto.FormattedNotificationDto;
import com.leqtr.todolist.dto.NotificationDto;
import com.leqtr.todolist.model.Notification;
import com.leqtr.todolist.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public void createNotificationForUser(String username, String message) {
        Notification notification = Notification.builder()
                .username(username)
                .message(message)
                .isRead(false)
                .createdAt(Instant.now())
                .build();
        notificationRepository.save(notification);

        messagingTemplate.convertAndSendToUser(
                username,
                "/queue/notifications",
                new NotificationDto(
                        message,
                        notification.getCreatedAt().toString())
        );
    }

    public List<FormattedNotificationDto> formatNotifications(String username) {
        List<Notification> notifications = notificationRepository.findByUsername(username);
        return notifications.stream()
                .sorted(Comparator.comparing(Notification::getCreatedAt).reversed())
                .map(notification -> {
                    String timestamp = notification.getCreatedAt().toString();
                    String formattedDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                            .withZone(ZoneId.systemDefault())
                            .format(Instant.parse(timestamp));
                    return new FormattedNotificationDto(
                            notification.getMessage(),
                            timestamp,
                            formattedDate
                    );
                })
                .toList();
    }
}