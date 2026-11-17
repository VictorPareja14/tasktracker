package com.enterprise.tasktracker.util;

import com.enterprise.tasktracker.enums.UserRole;
import com.enterprise.tasktracker.exception.AccessDeniedException;
import com.enterprise.tasktracker.exception.ResourceNotFoundException;
import com.enterprise.tasktracker.exception.TaskNotFoundException;
import com.enterprise.tasktracker.model.TaskEntity;
import com.enterprise.tasktracker.model.UserEntity;
import com.enterprise.tasktracker.repository.TaskRepository;
import com.enterprise.tasktracker.repository.UserRepository;
import com.enterprise.tasktracker.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
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
            log.warn("Permiso denegado: usuario {} intentó {} la tarea {}",
                    currentUser.getId(), action, task.getId());
            throw new AccessDeniedException( "No tienes permiso para " + action + " la tarea " + task.getId(),
                    currentUser.getId());
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
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    public void checkCanViewUserTasks(Long targetUserId, CustomUserDetails currentUser, String action) {
        if (isAdmin(currentUser)) {
            return;   // admin puede ver las de cualquiera
        }
        if (!Objects.equals(targetUserId, currentUser.getId())) {
            log.warn("Permiso denegado: usuario {} intentó {} tareas del usuario {}",
                    currentUser.getId(), action, targetUserId);
            throw new AccessDeniedException(
                    "No tienes permiso para " + action + " ver las tareas de otro usuario",
                    currentUser.getId()
            );
        }
    }
}