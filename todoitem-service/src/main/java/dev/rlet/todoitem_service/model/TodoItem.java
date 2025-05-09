package dev.rlet.todoitem_service.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "todoItems")
public class TodoItem {

    @Id
    private String id;

    private String title;
    private String description;

    @CreatedDate
    private Instant createdOn;
    @LastModifiedDate
    private Instant updatedOn;

    private String createdBy;
    private String updatedBy;
    private Long groupId;

    private Boolean complete;
    private Instant completedOn;

    @Override
    public String toString() {
        return id;
    }
}