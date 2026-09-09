package com.chatApplication.chat.controller;


import org.springframework.web.bind.annotation.*;

import com.chatApplication.chat.entity.User;
import com.chatApplication.chat.repo.UserRepository;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {

        return userRepository.save(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {

        return userRepository.findByEmail(user.getEmail())
                .filter(u -> u.getPassword().equals(user.getPassword()))
                .map(u -> "Login Successful")
                .orElse("Invalid Email or Password");
    }
}