package com.shubham.security.service;


import com.shubham.security.entity.UserEntity;
import com.shubham.security.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByusername(username);
        if(Objects.isNull(user)){
            System.out.println("User not available");
            throw new UsernameNotFoundException("User not present with this username");
        }
        return new CustomUserDetails(user);
    }
}
