package com.example.rqchallenge.datastructure;

import com.example.rqchallenge.entity.Employee;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class EmployeeTrieNode {
    private Map<Character, EmployeeTrieNode> children;
    private String content;
    private boolean isEndOfWord;
    private Employee employee;

    public EmployeeTrieNode() {
        children = new HashMap<Character, EmployeeTrieNode>();
    }
}
