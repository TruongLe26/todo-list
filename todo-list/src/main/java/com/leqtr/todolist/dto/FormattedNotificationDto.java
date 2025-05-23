package com.leqtr.todolist.dto;

public record FormattedNotificationDto(
        String content,
        String timestamp,
        String formattedDate
) {}