package com.todoapp.todo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TaskRequest {
    @NotBlank
    private String title;

    private String description;

    @NotBlank
    private String categoryId;

    private boolean completed;
}
