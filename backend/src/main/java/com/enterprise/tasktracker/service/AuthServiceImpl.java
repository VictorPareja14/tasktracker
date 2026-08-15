package com.enterprise.tasktracker.service;

import com.enterprise.tasktracker.dto.LoginRequest;
import com.enterprise.tasktracker.dto.LoginResponse;
import com.enterprise.tasktracker.dto.UserRegisterRequest;
import com.enterprise.tasktracker.dto.UserResponse;
import com.enterprise.tasktracker.enums.UserRole;
import com.enterprise.tasktracker.mapper.UserMapper;
import com.enterprise.tasktracker.model.UserEntity;
import com.enterprise.tasktracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    // ============================================
    //  IMPLEMENTAR REGISTRO
    // ============================================
    @Override
    public UserResponse register(UserRegisterRequest request) {

        Optional<UserEntity> existingUser = userRepository.findByUsername(request.getUsername());

        // 2. Si existe, lanzar excepción (con mensaje claro)
        if (existingUser.isPresent()) {
            throw new RuntimeException("El nombre de usuario '" + request.getUsername() + "' ya está en uso");
        }
        Optional<UserEntity> existingEmail = userRepository.findByEmail(request.getEmail());

        if (existingEmail.isPresent()) {
            throw new RuntimeException("El email de usuario '" + request.getUsername() + "' ya está en uso");
        }
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        UserEntity newUser = userMapper.toEntity(request);

        newUser.setPassword(encodedPassword);

        newUser.setRole(UserRole.USER);

        UserEntity savedUser = userRepository.save(newUser);

        return userMapper.toResponse(savedUser);
    }

    // ============================================
    //  IMPLEMENTAR LOGIN (JWT)
    // ============================================
    @Override
    public LoginResponse login(LoginRequest request) {
        // TODO: Implementar login con JWT
        // 1. Buscar usuario por username
        // 2. Si no existe, lanzar ResourceNotFoundException
        // 3. Validar la contraseña con PasswordEncoder
        // 4. Si es incorrecta, lanzar AccessDeniedException
        // 5. Generar token JWT
        // 6. Devolver LoginResponse (token + UserResponse)
        throw new UnsupportedOperationException("Método login no implementado");
    }
}