package com.enterprise.tasktracker.security;

import com.enterprise.tasktracker.enums.UserRole;
import com.enterprise.tasktracker.model.UserEntity;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

@Getter
public class CustomUserDetails extends User {

    private final Long id;
    private final String name;
    private final String email;
    private final UserRole role;

    public CustomUserDetails(UserEntity user) {
        super(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
        this.id = user.getId();
        this.name = user.getUsername();
        this.email = user.getEmail();
        this.role = user.getRole();
    }

}