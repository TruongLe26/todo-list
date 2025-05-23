package com.leqtr.shared.event.todo;

import com.leqtr.shared.event.base.BaseEvent;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class TodoItemUpdatedEvent extends BaseEvent {
    private String todoItemId;
    private List<FieldChange> changes;

    public TodoItemUpdatedEvent(String username, String todoItemId, List<FieldChange> changes) {
        super(username, "TODO_ITEM_UPDATED", Instant.now().toString());
        this.todoItemId = todoItemId;
        this.changes = changes;
    }

    protected TodoItemUpdatedEvent() {
        super();
    }

    @Getter @Setter
    public static class FieldChange {
        private String field;
        private String oldValue;
        private String newValue;

        public FieldChange() {}

        public FieldChange(String field, String oldValue, String newValue) {
            this.field = field;
            this.oldValue = oldValue;
            this.newValue = newValue;
        }
    }
}