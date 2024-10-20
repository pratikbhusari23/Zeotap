package com.example.RuleEngine.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rule {
    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    private String ruleString;

    @Lob
    private String astJson;
}
