package com.todoapp.todo.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class CategoryResponse {
    private String id;
    private String name;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
}
