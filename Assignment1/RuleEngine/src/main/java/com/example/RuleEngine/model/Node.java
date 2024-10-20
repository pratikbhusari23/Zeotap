package com.example.RuleEngine.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Node {
    
    private String type;  
    private Node left;
    private Node right;
    private String value;
}
