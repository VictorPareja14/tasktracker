package com.enterprise.tasktracker.mapper;

import com.enterprise.tasktracker.dto.UserRegisterRequest;
import com.enterprise.tasktracker.dto.UserResponse;
import com.enterprise.tasktracker.model.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toResponse(UserEntity user);

    UserEntity toEntity(UserRegisterRequest userRegisterRequest);

}
