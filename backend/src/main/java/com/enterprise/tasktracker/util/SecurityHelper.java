package com.enterprise.tasktracker.util;

import com.enterprise.tasktracker.enums.UserRole;
import com.enterprise.tasktracker.exception.AccessDeniedException;
import com.enterprise.tasktracker.exception.ResourceNotFoundException;
import com.enterprise.tasktracker.model.TaskEntity;
import com.enterprise.tasktracker.model.UserEntity;
import com.enterprise.tasktracker.repository.TaskRepository;
import com.enterprise.tasktracker.repository.UserRepository;
import com.enterprise.tasktracker.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityHelper {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public boolean isAdmin(CustomUserDetails currentUser) {
        return UserRole.ADMIN.equals(currentUser.getRole());
    }

    public boolean hasPermission(TaskEntity task, CustomUserDetails currentUser) {
        if (isAdmin(currentUser)) {
            return true;
        }
        Long userId = currentUser.getId();
        return task.getCreatedBy().getId().equals(userId) ||
                (task.getAssignedTo() != null && task.getAssignedTo().getId().equals(userId));
    }

    public void checkPermission(TaskEntity task, CustomUserDetails currentUser, String action) {
        if (!hasPermission(task, currentUser)) {
            throw new AccessDeniedException("Se produjo una excepción al " + action + " la tarea " + task.getId(), currentUser.getId());
        }
    }

    public UserEntity findUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));
    }


    /**
     * Busca una tarea por ID. Si no existe, lanza ResourceNotFoundException.
     */
    public TaskEntity findTaskOrThrow(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada ", id));
    }
}