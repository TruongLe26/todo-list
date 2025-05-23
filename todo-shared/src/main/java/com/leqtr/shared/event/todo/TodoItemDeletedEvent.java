package com.leqtr.shared.event.todo;

import com.leqtr.shared.event.base.BaseEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TodoItemDeletedEvent extends BaseEvent {
    private String todoItemId;
    private String title;

    public TodoItemDeletedEvent(String username, String todoItemId, String title) {
        super(username, "TODO_ITEM_DELETED", null);
        this.todoItemId = todoItemId;
        this.title = title;
    }
}