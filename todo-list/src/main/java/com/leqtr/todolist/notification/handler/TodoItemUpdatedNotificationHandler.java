package com.leqtr.todolist.notification.handler;

import com.leqtr.shared.event.todo.TodoItemUpdatedEvent;
import com.leqtr.todolist.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TodoItemUpdatedNotificationHandler implements NotificationHandler<TodoItemUpdatedEvent> {

    private final NotificationService notificationService;

    @Override
    public void handle(TodoItemUpdatedEvent event) {
        StringBuilder changes = new StringBuilder("You just updated a todo item: ");
        for (TodoItemUpdatedEvent.FieldChange change : event.getChanges()) {
            changes.append(String.format(
                    "[%s: '%s' ➜ '%s'] ",
                    change.getField(),
                    change.getOldValue(),
                    change.getNewValue()
            ));
        }
        notificationService.createNotificationForUser(
                event.getUsername(),
                changes.toString().trim()
        );
    }

    @Override
    public Class<TodoItemUpdatedEvent> supportedEventClass() {
        return TodoItemUpdatedEvent.class;
    }
}