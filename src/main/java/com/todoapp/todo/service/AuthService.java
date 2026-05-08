package com.todoapp.todo.service;

import com.todoapp.todo.dto.AuthRequest;
import com.todoapp.todo.dto.AuthResponse;
import com.todoapp.todo.model.Role;
import com.todoapp.todo.model.User;
import com.todoapp.todo.repository.UserRepository;
import com.todoapp.todo.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        String token = jwtService.generateToken(request.getUsername());
        return AuthResponse.builder()
                .token(token)
                .username(request.getUsername())
                .build();
    }
}
