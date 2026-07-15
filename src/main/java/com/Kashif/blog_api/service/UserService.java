package com.Kashif.blog_api.service;

import com.Kashif.blog_api.entity.User;

import java.util.List;

public interface UserService {
    User registerUser(User user);

    User getUserById(Long id);

    User getUserByUsername(String username);

    List<User> getAllUsers();

    void deleteUser(Long id);

    User getUserByEmail(String email);
}
