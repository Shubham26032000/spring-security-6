package com.shubham.security.controller;


import com.shubham.security.entity.UserEntity;
import com.shubham.security.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public UserEntity register(@RequestBody UserEntity user){
        return userRepository.save(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody UserEntity user){
        UserEntity userEntity = userRepository.findByusername(user.getUsername());
        if(Objects.isNull(userEntity))
            return "failure";
        return "success";
    }

}
