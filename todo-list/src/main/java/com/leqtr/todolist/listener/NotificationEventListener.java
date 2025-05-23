package com.leqtr.todolist.listener;

import com.leqtr.shared.event.base.BaseEvent;
import com.leqtr.shared.event.todo.TodoItemCreatedEvent;
import com.leqtr.shared.event.todo.TodoItemDeletedEvent;
import com.leqtr.shared.event.todo.TodoItemUpdatedEvent;
import com.leqtr.todolist.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationEventListener {

    private final NotificationService notificationService;
    private final Logger logger = LoggerFactory.getLogger(NotificationEventListener.class);

    @KafkaListener(
            topics = "user-notification",
            groupId = "notification-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleNotificationEvent(BaseEvent event) {
        logger.info("Received event: {}", event);
        if (event instanceof TodoItemCreatedEvent createdEvent) {
            notificationService.createNotificationForUser(
                    createdEvent.getUsername(),
                    "You just created a new todo item: " + createdEvent.getTitle()
            );
        } else if (event instanceof TodoItemUpdatedEvent updatedEvent) {
            StringBuilder changes = new StringBuilder("You just updated a todo item: ");
            for (TodoItemUpdatedEvent.FieldChange change : updatedEvent.getChanges()) {
                changes.append(String.format(
                        "[%s: '%s' ➜ '%s'] ",
                        change.getField(),
                        change.getOldValue(),
                        change.getNewValue()
                ));
            }
            notificationService.createNotificationForUser(
                    updatedEvent.getUsername(),
                    changes.toString().trim()
            );
            logger.info("Notification saved: {}", changes.toString().trim());
        } else if (event instanceof TodoItemDeletedEvent deletedEvent) {
            notificationService.createNotificationForUser(
                    deletedEvent.getUsername(),
                    "You just deleted a todo item: " + deletedEvent.getTitle()
            );
        } else {
            logger.warn("Unknown event type: {}", event.getClass().getName());
        }
    }
}