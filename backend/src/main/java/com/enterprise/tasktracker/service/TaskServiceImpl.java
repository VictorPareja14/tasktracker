package com.enterprise.tasktracker.service;

import com.enterprise.tasktracker.dto.TaskRequest;
import com.enterprise.tasktracker.dto.TaskResponse;
import com.enterprise.tasktracker.enums.TaskStatus;
import com.enterprise.tasktracker.enums.UserRole;
import com.enterprise.tasktracker.mapper.TaskMapper;
import com.enterprise.tasktracker.mapper.UserMapper;
import com.enterprise.tasktracker.model.TaskEntity;
import com.enterprise.tasktracker.model.UserEntity;
import com.enterprise.tasktracker.repository.TaskRepository;
import com.enterprise.tasktracker.repository.UserRepository;
import com.enterprise.tasktracker.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * @param request
     * @param currentUserId
     * @return
     */
    @Override
    public TaskResponse createTask(TaskRequest request, Long currentUserId) {

        UserEntity createdBy = userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("Usuario creador no encontrado"));
        UserEntity assignedTo = userRepository.findById(request.getAssignedToId())
                .orElseThrow(() -> new RuntimeException("Usuario asignado no encontrado"));

        TaskEntity task = taskMapper.toEntity(request);

        task.setCreatedBy(createdBy);
        task.setAssignedTo(assignedTo);


        task.setStatus(TaskStatus.PENDIENTE);
        TaskEntity savedTask = taskRepository.save(task);
        TaskResponse response = taskMapper.toResponse(savedTask);

        response.setCreatedBy(userMapper.toResponse(createdBy));
        response.setAssignedTo(userMapper.toResponse(assignedTo));

        return response;
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
            taskPage = taskRepository.findAll(pageable);

        } else {
            taskPage = taskRepository.findByAssignedToId(currentUser.getId(), pageable);

        }
        responsePage = taskPage.map(taskMapper::toResponse);
        return responsePage;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public TaskResponse getTaskById(Long id) {
        return null;
    }

    /**
     * @param id
     * @param request
     * @param currentUserId
     * @return
     */
    @Override
    public TaskResponse updateTask(Long id, TaskRequest request, Long currentUserId) {
        return null;
    }

    /**
     * @param id
     * @param currentUserId
     */
    @Override
    public void deleteTask(Long id, Long currentUserId) {

    }

    /**
     * @param id
     * @param status
     * @param currentUserId
     * @return
     */
    @Override
    public TaskResponse changeTaskStatus(Long id, TaskStatus status, Long currentUserId) {
        return null;
    }

    /**
     * @param currentUserId
     * @return
     */
    @Override
    public List<TaskResponse> getTasksByUser(Long currentUserId) {
        return List.of();
    }
}
