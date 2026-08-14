package com.enterprise.tasktracker.repository;

import com.enterprise.tasktracker.model.TaskEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    @Query("SELECT t FROM TaskEntity t LEFT JOIN FETCH t.assignedTo LEFT JOIN FETCH t.createdBy")
    Page<TaskEntity> findAllWithUsers(Pageable pageable);

    @Query("SELECT t FROM TaskEntity t LEFT JOIN FETCH t.assignedTo LEFT JOIN FETCH t.createdBy WHERE t.assignedTo.id = :assignedToId")
    Page<TaskEntity> findByAssignedToIdWithUsers(@Param("assignedToId") Long assignedToId, Pageable pageable);
}
