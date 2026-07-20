package com.Kashif.blog_api.controller;


import com.Kashif.blog_api.dto.LoginRequest;
import com.Kashif.blog_api.dto.LoginResponse;
import com.Kashif.blog_api.dto.UserRegisterRequest;
import com.Kashif.blog_api.dto.UserResponse;
import com.Kashif.blog_api.entity.Role;
import com.Kashif.blog_api.entity.User;
import com.Kashif.blog_api.security.CustomUserDetailsService;
import com.Kashif.blog_api.security.JwtUtil;
import com.Kashif.blog_api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;


    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(Role.USER);

        User savedUser = userService.registerUser(user);

        UserResponse response = new UserResponse(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {

        User savedUser = userService.getUserById(id);

        UserResponse response = new UserResponse(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail());

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<User> users = userService.getAllUsers();

        List<UserResponse> responseList = users.stream()
                .map(user -> new UserResponse(user.getId(), user.getUsername(), user.getEmail()))
                .collect(Collectors.toList());

        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username){
        User savedUser = userService.getUserByUsername(username);

        UserResponse userResponse = new UserResponse(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail());

                return new ResponseEntity<>(userResponse,HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        UserResponse response = new UserResponse(user.getId(), user.getUsername(), user.getEmail());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtUtil.generateToken(userDetails);

        return new ResponseEntity<>(new LoginResponse(token), HttpStatus.OK);
    }



}
