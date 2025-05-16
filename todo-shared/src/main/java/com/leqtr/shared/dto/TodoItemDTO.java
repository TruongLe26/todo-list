package com.leqtr.shared.dto;

import com.leqtr.shared.util.DateTimeUtils;
import lombok.*;

import java.time.Instant;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class TodoItemDTO {
    String id;
    private String title;
    private String description;
    private String createdOn;
    private String updatedOn;
    private String createdBy;
    private String updatedBy;
    private Long groupId;
    private Boolean complete;

    public static class Builder {
        private String id;
        private String title;
        private String description;
        private String createdOn;
        private String updatedOn;
        private String createdBy;
        private String updatedBy;
        private Long groupId;
        private Boolean complete;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder createdOn(Instant createdOn) {
            this.createdOn = DateTimeUtils.FORMATTER.format(createdOn);
            return this;
        }

        public Builder updatedOn(Instant updatedOn) {
            this.updatedOn = DateTimeUtils.FORMATTER.format(updatedOn);
            return this;
        }

        public Builder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public Builder updatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public Builder groupId(Long groupId) {
            this.groupId = groupId;
            return this;
        }

        public Builder complete(Boolean complete) {
            this.complete = complete;
            return this;
        }

        public static Builder customBuilder() {
            return new Builder();
        }

        public TodoItemDTO build() {
            return new TodoItemDTO(
                    id,
                    title,
                    description,
                    createdOn,
                    updatedOn,
                    createdBy,
                    updatedBy,
                    groupId,
                    Boolean.FALSE
            );
        }
    }

    public TodoItemDTO(String title, String description, Instant createdOn, Instant updateOn, String createdBy,
                       String updatedBy, Long groupId, Boolean complete) {
        this.title = title;
        this.description = description;
        this.createdOn = DateTimeUtils.FORMATTER.format(createdOn);
        this.updatedOn = DateTimeUtils.FORMATTER.format(updateOn);
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.groupId = groupId;
        this.complete = complete;
    }
    public TodoItemDTO(String id, String title, String description, Instant createdOn, Instant updateOn, String createdBy,
                       String updatedBy, Long groupId, Boolean complete) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdOn = DateTimeUtils.FORMATTER.format(createdOn);
        this.updatedOn = DateTimeUtils.FORMATTER.format(updateOn);
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.groupId = groupId;
        this.complete = complete;
    }

}
//public record TodoItemDTO(
//        String title,
//        String description,
//        String createdBy,
//        String updatedBy,
//        Long groupId,
//        Boolean complete
//) {}