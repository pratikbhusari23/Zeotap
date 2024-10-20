package com.example.RuleEngine.service;

import org.springframework.stereotype.Service;


import com.example.RuleEngine.repository.UserRepository;

@Service
public class UserDetailsService {
    
    private final UserRepository userRepository;

    public UserDetailsService(UserRepository userRepository){
        this.userRepository =userRepository;
    }

}
