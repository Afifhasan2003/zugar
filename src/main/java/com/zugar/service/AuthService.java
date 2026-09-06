package com.zugar.service;

import com.zugar.model.User;
import com.zugar.repository.UserRepository;

import java.util.UUID;

public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public User register(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        User existing = userRepository.findByUsername(username.trim());
        if (existing != null) {
            throw new IllegalArgumentException("Username already exists: " + username);
        }

        String id = UUID.randomUUID().toString();
        String passwordHash = PasswordHasher.hashPassword(password);
        long now = System.currentTimeMillis() / 1000L;

        User user = new User(id, username.trim(), passwordHash, now);
        userRepository.save(user);
        return user;
    }

    public String login(String username, String password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException("Username and password are required");
        }

        User user = userRepository.findByUsername(username.trim());
        if (user == null) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        boolean valid = PasswordHasher.verifyPassword(password, user.getPasswordHash());
        if (!valid) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        return jwtService.generateToken(user.getId(), user.getUsername());
    }

    public User validateAndGetUser(String token) {
        if (!jwtService.validateToken(token)) {
            return null;
        }
        String userId = jwtService.extractUserId(token);
        if (userId == null) {
            return null;
        }
        return userRepository.findById(userId);
    }
}
