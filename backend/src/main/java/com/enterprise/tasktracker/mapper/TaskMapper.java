package com.enterprise.tasktracker.mapper;

import com.enterprise.tasktracker.dto.TaskRequest;
import com.enterprise.tasktracker.dto.TaskResponse;
import com.enterprise.tasktracker.model.TaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface TaskMapper {


    @Mapping(target = "assignedTo", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    TaskEntity toEntity(TaskRequest request);

    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    TaskResponse toResponse(TaskEntity task);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "completedAt", ignore = true)
    void updateEntity(TaskRequest request, @MappingTarget TaskEntity task);
}