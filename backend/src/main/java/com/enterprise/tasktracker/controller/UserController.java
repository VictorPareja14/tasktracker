package com.enterprise.tasktracker.controller;

import com.enterprise.tasktracker.dto.TaskResponse;
import com.enterprise.tasktracker.security.CustomUserDetails;
import com.enterprise.tasktracker.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final TaskService taskService;

    /**
     * Obtener todas las tareas de un usuario específico (solo ADMIN o el propio usuario)
     * GET /api/users/{userId}/tasks?page=0&size=10
     */
    @GetMapping("/{userId}/tasks")
    public ResponseEntity<Page<TaskResponse>> getTasksByUser(
            @PathVariable Long userId,
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<TaskResponse> tasks = taskService.getTasksByUser(userId, currentUser, pageable);
        return ResponseEntity.ok(tasks);
    }
}