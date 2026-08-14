package com.enterprise.tasktracker.controller;

import com.enterprise.tasktracker.dto.TaskRequest;
import com.enterprise.tasktracker.dto.TaskResponse;
import com.enterprise.tasktracker.enums.TaskStatus;
import com.enterprise.tasktracker.security.CustomUserDetails;
import com.enterprise.tasktracker.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    /**
     * Crear una nueva tarea
     * POST /api/tasks
     */
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Validated @RequestBody TaskRequest request, @AuthenticationPrincipal CustomUserDetails currentUser) {
        Long currentUserId = currentUser.getId();
        TaskResponse response = taskService.createTask(request, currentUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Listar todas las tareas (ADMIN ve todas, USER ve solo las suyas)
     * GET /api/tasks
     */
    @GetMapping
    public ResponseEntity<Page<TaskResponse>> getAllTasks(@AuthenticationPrincipal CustomUserDetails currentUser, @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size) {
        Page<TaskResponse> tasks = taskService.getAllTasks(currentUser, page, size);
        return ResponseEntity.ok(tasks);
    }

    /**
     * Obtener una tarea por ID
     * GET /api/tasks/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails currentUser) {
        TaskResponse task = taskService.getTaskById(id, currentUser);
        return ResponseEntity.ok(task);
    }

    /**
     * Actualizar una tarea existente
     * PUT /api/tasks/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long id,
            @Validated @RequestBody TaskRequest request,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        TaskResponse response = taskService.updateTask(id, request, currentUser);
        return ResponseEntity.ok(response);
    }

    /**
     * Eliminar una tarea (soft delete - la archiva)
     * DELETE /api/tasks/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@AuthenticationPrincipal CustomUserDetails currentUser, @PathVariable Long id) {
        taskService.deleteTask(id, currentUser);
        return ResponseEntity.noContent().build();
    }

    /**
     * Cambiar el estado de una tarea
     * PATCH /api/tasks/{id}/status
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskResponse> changeTaskStatus(
            @PathVariable Long id,
            @RequestParam TaskStatus status,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        TaskResponse response = taskService.changeTaskStatus(id, status, currentUser);
        return ResponseEntity.ok(response);
    }
}