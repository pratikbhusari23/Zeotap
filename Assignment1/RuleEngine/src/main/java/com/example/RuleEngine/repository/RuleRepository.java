package com.example.RuleEngine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.RuleEngine.model.Rule;

@Repository
public interface RuleRepository extends JpaRepository<Rule,Long> {
    
}
