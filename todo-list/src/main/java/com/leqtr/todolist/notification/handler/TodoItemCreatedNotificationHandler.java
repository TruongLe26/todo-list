package com.leqtr.todolist.notification.handler;

import com.leqtr.shared.event.todo.TodoItemCreatedEvent;
import com.leqtr.todolist.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TodoItemCreatedNotificationHandler implements NotificationHandler<TodoItemCreatedEvent> {

    private final NotificationService notificationService;

    @Override
    public void handle(TodoItemCreatedEvent event) {
        notificationService.createNotificationForUser(
                event.getUsername(),
                "You just created a new todo item: " + event.getTitle()
        );
    }

    @Override
    public Class<TodoItemCreatedEvent> supportedEventClass() {
        return TodoItemCreatedEvent.class;
    }
}