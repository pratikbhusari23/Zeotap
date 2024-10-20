package com.example.RuleEngine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.RuleEngine.model.Node;
import com.example.RuleEngine.model.Rule;
import com.example.RuleEngine.service.RuleService;

import java.util.*;


@Controller
public class RuleController {
    
    @Autowired
    private RuleService ruleService;

    // Serve the UI
    @GetMapping("/")
    public String home() {      
        return "index";  // Renders the `index.html` from the `templates` folder
    }

    // API to create a rule
    @PostMapping("/api/rules/create")
    @ResponseBody
    public ResponseEntity<Rule> createRule(@RequestParam String ruleString) {
        try {
            Node ast = ruleService.createRule(ruleString);
            Rule savedRule = ruleService.saveRule(ruleString, ast);
            return ResponseEntity.ok(savedRule);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // API to combine rules
    @PostMapping("/api/rules/combine")
    @ResponseBody
    public ResponseEntity<Node> combineRules(@RequestBody List<String> rules) {
        try {
            Node combinedAST = ruleService.combineRules(rules);
            return ResponseEntity.ok(combinedAST);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // API to evaluate a rule
    @PostMapping("/api/rules/evaluate")
    @ResponseBody
    public ResponseEntity<Boolean> evaluateRule(@RequestBody Map<String, Object> data, 
                                               @RequestParam String ruleString) {
        try {
            Node ast = ruleService.createRule(ruleString);
            boolean result = ruleService.evaluateRule(ast, data);
            System.out.println("Evaluation result: " + result);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(false);
        }
    }

    // API to get a rule by ID
    @GetMapping("/api/rules/get/{id}")
    @ResponseBody
    public ResponseEntity<Rule> getRule(@PathVariable Long id) {
        try {
            Rule rule = ruleService.getRule(id);
            return ResponseEntity.ok(rule);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // API to modify a rule
    @PutMapping("/api/rules/modify/{id}")
    @ResponseBody
    public ResponseEntity<Rule> modifyRule(@PathVariable Long id, @RequestParam String newRuleString) {
        try {
            Rule updatedRule = ruleService.modifyRule(id, newRuleString);
            return ResponseEntity.ok(updatedRule);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
