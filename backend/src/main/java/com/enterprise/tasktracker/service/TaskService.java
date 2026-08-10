package com.enterprise.tasktracker.service;

import com.enterprise.tasktracker.dto.TaskRequest;
import com.enterprise.tasktracker.dto.TaskResponse;
import com.enterprise.tasktracker.enums.TaskStatus;
import com.enterprise.tasktracker.security.CustomUserDetails;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TaskService {
    TaskResponse createTask(TaskRequest request, Long currentUserId);

    Page<TaskResponse> getAllTasks(CustomUserDetails currentUser, int page, int size);

    TaskResponse getTaskById(Long id);

    TaskResponse updateTask(Long id, TaskRequest request, Long currentUserId);

    void deleteTask(Long id, Long currentUserId);

    TaskResponse changeTaskStatus(Long id, TaskStatus status, Long currentUserId);

    List<TaskResponse> getTasksByUser(Long currentUserId);

}
