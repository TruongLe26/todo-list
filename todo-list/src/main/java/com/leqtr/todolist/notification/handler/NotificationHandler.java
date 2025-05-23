package com.leqtr.todolist.notification.handler;

import com.leqtr.shared.event.base.BaseEvent;

public interface NotificationHandler<E extends BaseEvent> {
    Class<E> supportedEventClass();
    void handle(E event);
}
