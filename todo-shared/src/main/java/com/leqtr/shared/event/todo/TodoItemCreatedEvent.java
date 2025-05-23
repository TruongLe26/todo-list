package com.leqtr.shared.event.todo;

import com.leqtr.shared.event.base.BaseEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class TodoItemCreatedEvent extends BaseEvent {
    private String todoItemId;
    private String title;

    public TodoItemCreatedEvent(String username, String todoItemId, String title) {
        super(username, "TODO_ITEM_CREATED", Instant.now().toString());
        this.todoItemId = todoItemId;
        this.title = title;
    }
}