package com.example.RuleEngine.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.RuleEngine.model.User;

public interface UserRepository extends JpaRepository<User,Long> {
    
    User findByUsername(String username);
}
