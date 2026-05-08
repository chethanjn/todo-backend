package com.todoapp.todo.service;

import com.todoapp.todo.dto.TaskRequest;
import com.todoapp.todo.dto.TaskResponse;
import com.todoapp.todo.exception.ResourceNotFoundException;
import com.todoapp.todo.model.Category;
import com.todoapp.todo.model.Task;
import com.todoapp.todo.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final CategoryService categoryService;

    public List<TaskResponse> getUserTasks(
            String username,
            String search,
            String category,
            String createdFrom,
            String createdTo
            ) {
        List<Task> tasks = taskRepository.findByCreatedByOrderByCreatedAtDesc(username);

        if (search != null && !search.isBlank()) {
            String searchedLowercase = search.toLowerCase();
            tasks = tasks.stream()
                    .filter(task ->
                            (task.getTitle() != null && task.getTitle().toLowerCase().contains(searchedLowercase)) ||
                                    (task.getDescription() != null && task.getDescription().toLowerCase().contains(searchedLowercase)) ||
                                    (task.getCategoryName() != null && task.getCategoryName().toLowerCase().contains(searchedLowercase)))
                    .toList();
        }
        if (category != null && !category.isBlank()) {
            tasks = tasks.stream()
                    .filter(task -> task.getCategoryName() != null && task.getCategoryName().equalsIgnoreCase(category))
                    .toList();
        }
        if (createdFrom != null && !createdFrom.isBlank()) {
            LocalDate from = LocalDate.parse(createdFrom);
            tasks = tasks.stream()
                    .filter(task -> task.getCreatedAt() != null &&
                            !task.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDate().isBefore(from))
                    .toList();
        }

        if (createdTo != null && !createdTo.isBlank()) {
            LocalDate to = LocalDate.parse(createdTo);
            tasks = tasks.stream()
                    .filter(task -> task.getCreatedAt() != null &&
                            !task.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDate().isAfter(to))
                    .toList();
        }
        return tasks.stream().map(this::toResponse).toList();
    }

    public TaskResponse create(String username, TaskRequest request) {
        Category category = categoryService.getById(request.getCategoryId());

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .categoryId(category.getId())
                .categoryName(category.getName())
                .completed(request.isCompleted())
                .createdBy(username)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        return toResponse(taskRepository.save(task));
    }

    public TaskResponse update(String id, TaskRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found: " + id));

        Category category = categoryService.getById(request.getCategoryId());

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setCategoryId(category.getId());
        task.setCategoryName(category.getName());
        task.setCompleted(request.isCompleted());
        task.setUpdatedAt(Instant.now());

        return toResponse(taskRepository.save(task));
    }

    public void delete(String id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task not found: " + id);
        }
        taskRepository.deleteById(id);
    }

    private TaskResponse toResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .categoryId(task.getCategoryId())
                .categoryName(task.getCategoryName())
                .completed(task.isCompleted())
                .createdBy(task.getCreatedBy())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }
}
