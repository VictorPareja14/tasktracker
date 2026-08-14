package com.enterprise.tasktracker.service;

import com.enterprise.tasktracker.dto.TaskRequest;
import com.enterprise.tasktracker.dto.TaskResponse;
import com.enterprise.tasktracker.enums.TaskStatus;
import com.enterprise.tasktracker.enums.UserRole;
import com.enterprise.tasktracker.exception.AccessDeniedException;
import com.enterprise.tasktracker.mapper.TaskMapper;
import com.enterprise.tasktracker.model.TaskEntity;
import com.enterprise.tasktracker.model.UserEntity;
import com.enterprise.tasktracker.repository.TaskRepository;
import com.enterprise.tasktracker.security.CustomUserDetails;
import com.enterprise.tasktracker.util.SecurityHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final SecurityHelper securityHelper;

    /**
     * @param request
     * @param currentUserId
     * @return
     */
    @Override
    public TaskResponse createTask(TaskRequest request, Long currentUserId) {

        UserEntity createdBy = securityHelper.findUserOrThrow(currentUserId);
        UserEntity assignedTo = securityHelper.findUserOrThrow(request.getAssignedToId());

        TaskEntity task = taskMapper.toEntity(request);

        task.setCreatedBy(createdBy);
        task.setAssignedTo(assignedTo);


        task.setStatus(TaskStatus.PENDIENTE);
        TaskEntity savedTask = taskRepository.save(task);

        return taskMapper.toResponse(savedTask);

    }

    /**
     * @return
     */
    @Override
    public Page<TaskResponse> getAllTasks(CustomUserDetails currentUser, int page, int size) {
        Page<TaskResponse> responsePage;
        Pageable pageable = PageRequest.of(page, size);

        Page<TaskEntity> taskPage;
        if (UserRole.ADMIN.equals(currentUser.getRole())) {
            taskPage = taskRepository.findAllWithUsers(pageable);

        } else {
            taskPage = taskRepository.findByAssignedToIdWithUsers(currentUser.getId(), pageable);

        }
        responsePage = taskPage.map(taskMapper::toResponse);
        return responsePage;
    }


    /**
     * @param id
     * @param request
     * @param currentUser
     * @return
     */
    @Override
    public TaskResponse updateTask(Long id, TaskRequest request, CustomUserDetails currentUser) {
        TaskEntity task = securityHelper.findTaskOrThrow(id);
        securityHelper.checkPermission(task, currentUser, "cambiar el estado de");
        taskMapper.updateEntity(request, task);
        TaskEntity savedTask = taskRepository.save(task);
        return taskMapper.toResponse(savedTask);
    }

    /**
     * @param id
     * @param status
     * @param currentUser
     * @return
     */

    @Override
    public TaskResponse changeTaskStatus(Long id, TaskStatus status, CustomUserDetails currentUser) {
        TaskEntity task = securityHelper.findTaskOrThrow(id);
        securityHelper.checkPermission(task, currentUser, "cambiar el estado de");
        task.setStatus(status);
        if (status.equals(TaskStatus.COMPLETADA)) {
            task.setCompletedAt(LocalDateTime.now());
        } else {
            task.setCompletedAt(null);
        }
        TaskEntity updatedTask = taskRepository.save(task);
        return taskMapper.toResponse(updatedTask);
    }

    /**
     * @param id
     * @param currentUser
     */
    @Override
    public void deleteTask(Long id, CustomUserDetails currentUser) {
        TaskEntity task = securityHelper.findTaskOrThrow(id);
        securityHelper.checkPermission(task, currentUser, "eliminar");
        taskRepository.delete(task);
    }

    @Override
    public Page<TaskResponse> getTasksByUser(Long currentUserId, CustomUserDetails currentUser, Pageable pageable) {
        Page<TaskEntity> taskPage;

        if (UserRole.ADMIN.equals(currentUser.getRole())) {
            taskPage = taskRepository.findByAssignedToIdWithUsers(currentUserId, pageable);
        } else {
            if (!Objects.equals(currentUserId, currentUser.getId())) {
                throw new AccessDeniedException("No tienes permiso para ver las tareas de otro usuario");
            }
            taskPage = taskRepository.findByAssignedToIdWithUsers(currentUserId, pageable);
        }

        return taskPage.map(taskMapper::toResponse);
    }

    /**
     * @param id
     * @param currentUser
     * @return
     */
    @Override
    public TaskResponse getTaskById(Long id, CustomUserDetails currentUser) {
        TaskEntity task = securityHelper.findTaskOrThrow(id);
        securityHelper.checkPermission(task, currentUser, "consultar detalle");
        return taskMapper.toResponse(task);
    }

}
