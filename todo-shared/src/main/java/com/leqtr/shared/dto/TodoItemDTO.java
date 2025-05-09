package com.leqtr.shared.dto;

public class TodoItemDTO {
    private String title;
    private String description;
    private String createdBy;
    private String updatedBy;
    private Long groupId;
    private Boolean complete;
    public TodoItemDTO() {
    }
    public TodoItemDTO(String title, String description, String createdBy,
            String updatedBy, Long groupId, Boolean complete) {
        this.title = title;
        this.description = description;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.groupId = groupId;
        this.complete = complete;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Boolean getComplete() {
        return complete;
    }

    public void setComplete(Boolean complete) {
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