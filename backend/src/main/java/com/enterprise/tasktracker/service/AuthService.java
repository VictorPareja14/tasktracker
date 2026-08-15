package com.enterprise.tasktracker.service;

import com.enterprise.tasktracker.dto.LoginRequest;
import com.enterprise.tasktracker.dto.LoginResponse;
import com.enterprise.tasktracker.dto.UserRegisterRequest;
import com.enterprise.tasktracker.dto.UserResponse;

public interface AuthService {

    /**
     * Registrar un nuevo usuario
     */
    UserResponse register(UserRegisterRequest request);

    /**
     * Login de usuario (JWT)
     */
    LoginResponse login(LoginRequest request);
}