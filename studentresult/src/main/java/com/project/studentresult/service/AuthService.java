package com.project.studentresult.service;

import com.project.studentresult.dto.AuthRequest;
import com.project.studentresult.dto.AuthResponse;
import com.project.studentresult.model.User;
import com.project.studentresult.repository.UserRepository;
import com.project.studentresult.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository        userRepository;
    private final JwtUtil               jwtUtil;

    public AuthService(AuthenticationManager authenticationManager,
                       UserRepository userRepository,
                       JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userRepository        = userRepository;
        this.jwtUtil               = jwtUtil;
    }

    public AuthResponse login(AuthRequest request) {
    	 // 🔍 DEBUG (ADD HERE)
        System.out.println("USERNAME: " + request.getUsername());
        System.out.println("PASSWORD: " + request.getPassword());

        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getUsername(), request.getPassword()));
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid username or password");
        }

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());

        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getRole().name())
                .userId(user.getId())
                .message("Login successful")
                .build();
        
        
    }
}