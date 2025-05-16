package dev.rlet.todoitem_service.mapper;

import com.leqtr.shared.util.DateTimeUtils;
import com.leqtr.shared.dto.TodoItemDTO;
import dev.rlet.todoitem_service.model.TodoItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.util.List;

@Mapper(componentModel = "spring")
public interface TodoItemMapper {
    @Mapping(target = "createdOn", expression = "java(formatInstant(todoItem.getCreatedOn()))")
    @Mapping(target = "updatedOn", expression = "java(formatInstant(todoItem.getUpdatedOn()))")
    TodoItemDTO toDto(TodoItem todoItem);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    @Mapping(target = "complete", expression = "java(defaultComplete(Boolean.FALSE))")
    TodoItem toEntity(TodoItemDTO todoItemDTO);

    List<TodoItemDTO> toDtoList(List<TodoItem> entities);
    List<TodoItem> toEntityList(List<TodoItemDTO> dtos);

    default String formatInstant(Instant instant) {
        return instant != null ? DateTimeUtils.FORMATTER.format(instant) : null;
    }

    default Boolean defaultComplete(Boolean val) {
        return val != null ? val : Boolean.TRUE;
    }
}
