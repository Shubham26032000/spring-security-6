package com.shubham.security.controller;


import com.shubham.security.entity.UserEntity;
import com.shubham.security.repository.UserRepository;
import com.shubham.security.service.UserService;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    public UserController(UserRepository userRepository,UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @PostMapping("/register")
    public UserEntity register(@RequestBody UserEntity user){
        return userService.register(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody UserEntity user){
        return userService.verify(user);

    }

}
