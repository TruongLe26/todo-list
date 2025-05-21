package com.leqtr.shared.event.base;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.leqtr.shared.event.todo.TodoItemCreatedEvent;
import com.leqtr.shared.event.todo.TodoItemDeletedEvent;
import com.leqtr.shared.event.todo.TodoItemUpdatedEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "eventType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TodoItemCreatedEvent.class, name = "TODO_ITEM_CREATED"),
        @JsonSubTypes.Type(value = TodoItemUpdatedEvent.class, name = "TODO_ITEM_UPDATED"),
        @JsonSubTypes.Type(value = TodoItemDeletedEvent.class, name = "TODO_ITEM_DELETED"),
})
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEvent {
    protected String username;
    protected String eventType;
    protected String timestamp;
}