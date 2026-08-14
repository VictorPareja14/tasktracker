package com.enterprise.tasktracker.service;

import com.enterprise.tasktracker.dto.TaskRequest;
import com.enterprise.tasktracker.dto.TaskResponse;
import com.enterprise.tasktracker.enums.TaskStatus;
import com.enterprise.tasktracker.security.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {
    TaskResponse createTask(TaskRequest request, Long currentUserId);

    Page<TaskResponse> getAllTasks(CustomUserDetails currentUser, int page, int size);

    TaskResponse getTaskById(Long id, CustomUserDetails currentUser);

    TaskResponse updateTask(Long id, TaskRequest request, CustomUserDetails currentUser);

    void deleteTask(Long id, CustomUserDetails currentUser);

    TaskResponse changeTaskStatus(Long id, TaskStatus status, CustomUserDetails currentUser);

    Page<TaskResponse> getTasksByUser(Long currentUserId, CustomUserDetails currentUser, Pageable pageable);

}
