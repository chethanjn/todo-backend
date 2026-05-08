package com.todoapp.todo.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class TaskResponse {
    private String id;
    private String title;
    private String description;
    private String categoryId;
    private String categoryName;
    private boolean completed;
    private String createdBy;
    private Instant createdAt;
    private Instant updatedAt;
}