package com.leqtr.todolist.notification.listener;

import com.leqtr.shared.event.base.BaseEvent;
import com.leqtr.todolist.notification.handler.NotificationHandler;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationEventListener {

    private final List<NotificationHandler<? extends BaseEvent>> handlers;
    private final Logger logger = LoggerFactory.getLogger(NotificationEventListener.class);

    private Map<Class<?>, NotificationHandler<?>> handlerMap;

    @PostConstruct
    public void init() {
        handlerMap = handlers.stream()
                .collect(Collectors.toMap(
                        NotificationHandler::supportedEventClass,
                        Function.identity()
                ));
    }

    @KafkaListener(
            topics = "user-notification",
            groupId = "notification-group",
            containerFactory = "notificationKafkaListenerContainerFactory"
    )
    public void handleNotificationEvent(BaseEvent event) {
        logger.info("Received event: {}", event);
        NotificationHandler<?> rawHandler = handlerMap.get(event.getClass());
        if (rawHandler != null) {
            try {
                @SuppressWarnings("unchecked")
                NotificationHandler<BaseEvent> handler = (NotificationHandler<BaseEvent>) rawHandler;
                handler.handle(event);
            } catch (Exception e) {
                logger.error("Error handling event: {}", event, e);
            }
        } else {
            logger.warn("No handler found for event type: {}", event.getClass().getName());
        }
    }
}