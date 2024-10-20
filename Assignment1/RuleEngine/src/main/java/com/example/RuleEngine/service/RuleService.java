package com.example.RuleEngine.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.RuleEngine.model.Node;
import com.example.RuleEngine.model.Rule;
import com.example.RuleEngine.repository.RuleRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RuleService {
    
    @Autowired
    private RuleRepository ruleRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Parse the rule string into AST format
    public Node createRule(String ruleString) throws Exception {
        // For simplicity, we assume rules are in a specific format and handle AND/OR operators.
        // For a more robust solution, consider using a parsing library or writing a comprehensive parser.
        return parseRule(ruleString);
    }

    // Combine multiple rules into a single AST
    public Node combineRules(List<String> ruleStrings) throws Exception {
        Node combinedAST = null;
        for (String rule : ruleStrings) {

            if (rule == null || rule.trim().isEmpty()) {
                throw new Exception("Rule cannot be null or empty.");
            }

            Node ruleAST = createRule(rule);


            if (combinedAST == null) {
                combinedAST = ruleAST;
            } else {
                combinedAST = new Node("operator", combinedAST, ruleAST, "AND");
            }
        }
        return combinedAST;
    }

    // Evaluate the AST based on user data
public boolean evaluateRule(Node ast, Map<String, Object> data) throws Exception {
    if (ast.getType().equals("operand")) {
        boolean result = evaluateCondition(ast.getValue(), data);
        System.out.println("Evaluating condition: " + ast.getValue() + " Result: " + result);
        return result;
    } else if (ast.getType().equals("operator")) {
        boolean leftEval = evaluateRule(ast.getLeft(), data);
        boolean rightEval = evaluateRule(ast.getRight(), data);
        boolean result = ast.getValue().equalsIgnoreCase("AND") ? leftEval && rightEval : leftEval || rightEval;
        System.out.println("Evaluating operator: " + ast.getValue() + " Result: " + result);
        return result;
    }
    return false;
}


    private boolean evaluateCondition(String condition, Map<String, Object> data) throws Exception {
        // Handle conditions like "age > 30", "department = 'Sales'"
        condition = condition.trim();
        if (condition.contains(">=")) {
            String[] parts = condition.split(">=");
            String field = parts[0].trim();
            int value = Integer.parseInt(parts[1].trim());
            return ((Integer)data.get(field)) >= value;
        } else if (condition.contains("<=")) {
            String[] parts = condition.split("<=");
            String field = parts[0].trim();
            int value = Integer.parseInt(parts[1].trim());
            return ((Integer)data.get(field)) <= value;
        } else if (condition.contains(">")) {
            String[] parts = condition.split(">");
            String field = parts[0].trim();
            int value = Integer.parseInt(parts[1].trim());
            return ((Integer)data.get(field)) > value;
        } else if (condition.contains("<")) {
            String[] parts = condition.split("<");
            String field = parts[0].trim();
            int value = Integer.parseInt(parts[1].trim());
            return ((Integer)data.get(field)) < value;
        } else if (condition.contains("=")) {
            String[] parts = condition.split("=");
            String field = parts[0].trim();
            String value = parts[1].trim().replaceAll("'", "");
            return data.get(field).toString().equalsIgnoreCase(value);
        } else {
            throw new Exception("Invalid condition format: " + condition);
        }
    }

    // Simplistic parser for demonstration purposes
    private Node parseRule(String rule) throws Exception {
        rule = rule.replaceAll("\\s+", "");
        if (rule.startsWith("(") && rule.endsWith(")")) {
            rule = rule.substring(1, rule.length() - 1);
        }

        int level = 0;
        int splitIndex = -1;
        String operator = "";
        for (int i = rule.length() - 1; i >= 0; i--) {
            char c = rule.charAt(i);
            if (c == ')') level++;
            if (c == '(') level--;
            if (level == 0) {
                if (rule.startsWith("AND", i - 2)) {
                    splitIndex = i - 2;
                    operator = "AND";
                    break;
                } else if (rule.startsWith("OR", i - 1)) {
                    splitIndex = i - 1;
                    operator = "OR";
                    break;
                }
            }
        }

        if (splitIndex != -1) {
            String leftPart = rule.substring(0, splitIndex);
            String rightPart = rule.substring(splitIndex + operator.length());
            Node leftNode = parseRule(leftPart);
            Node rightNode = parseRule(rightPart);
            return new Node("operator", leftNode, rightNode, operator);
        } else {
            return new Node("operand", null, null, rule);
        }
    }

    // Save rule to the database
    public Rule saveRule(String ruleString, Node ast) throws JsonProcessingException {
        String astJson = objectMapper.writeValueAsString(ast);
        Rule rule = new Rule();
        rule.setRuleString(ruleString);
        rule.setAstJson(astJson);
        return ruleRepository.save(rule);
    }

    // Retrieve rule from the database
    public Rule getRule(Long id) throws JsonProcessingException {
        Optional<Rule> optionalRule = ruleRepository.findById(id);
        if (optionalRule.isPresent()) {
            return optionalRule.get();
        } else {
            throw new NoSuchElementException("Rule not found with id: " + id);
        }
    }

    // Modify existing rule
    public Rule modifyRule(Long id, String newRuleString) throws Exception {
        Rule existingRule = getRule(id);
        Node newAST = createRule(newRuleString);
        existingRule.setRuleString(newRuleString);
        existingRule.setAstJson(objectMapper.writeValueAsString(newAST));
        return ruleRepository.save(existingRule);
    }

    // Helper method to convert AST to JSON
    public String convertAstToJson(Node ast) throws JsonProcessingException {
        return objectMapper.writeValueAsString(ast);
    }

    // Helper method to convert JSON to AST
    public Node convertJsonToAst(String astJson) throws JsonProcessingException {
        return objectMapper.readValue(astJson, Node.class);
    }

}
