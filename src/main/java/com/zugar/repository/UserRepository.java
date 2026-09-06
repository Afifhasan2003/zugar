package com.zugar.repository;

import com.zugar.model.User;

public interface UserRepository {
    void save(User user);
    User findById(String id);
    User findByUsername(String username);
}
