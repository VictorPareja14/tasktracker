package com.enterprise.tasktracker.dto;

import com.enterprise.tasktracker.enums.TaskPriority;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest {
    private @NotBlank String title;
    private TaskPriority priority;
    private Long assignedToId;
    private String description;
    private LocalDate dueDate;
}