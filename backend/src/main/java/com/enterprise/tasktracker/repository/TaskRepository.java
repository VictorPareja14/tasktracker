package com.enterprise.tasktracker.repository;

import com.enterprise.tasktracker.model.TaskEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    Page<TaskEntity> findByAssignedToId(Long assignedToId, Pageable pageable);
}
