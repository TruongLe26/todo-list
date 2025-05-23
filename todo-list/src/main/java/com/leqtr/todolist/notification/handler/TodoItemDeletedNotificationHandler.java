package com.leqtr.todolist.notification.handler;

import com.leqtr.shared.event.todo.TodoItemDeletedEvent;
import com.leqtr.todolist.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TodoItemDeletedNotificationHandler implements NotificationHandler<TodoItemDeletedEvent> {

    private final NotificationService notificationService;

    @Override
    public void handle(TodoItemDeletedEvent event) {
        notificationService.createNotificationForUser(
                event.getUsername(),
                "You just deleted a todo item: " + event.getTitle()
        );
    }

    @Override
    public Class<TodoItemDeletedEvent> supportedEventClass() {
        return TodoItemDeletedEvent.class;
    }
}