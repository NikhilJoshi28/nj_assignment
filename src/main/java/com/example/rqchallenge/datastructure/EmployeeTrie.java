package com.example.rqchallenge.datastructure;

import com.example.rqchallenge.entity.Employee;
import lombok.Getter;

import java.util.function.Function;

public class EmployeeTrie {

    @Getter
    private static final EmployeeTrieNode root = new EmployeeTrieNode();

    private EmployeeTrie(){}

    public static void insertEmployee(String word,Employee employee) {
        EmployeeTrieNode current = root;
        for (char l: word.toCharArray()) {
            current = current.getChildren().computeIfAbsent(l, new Function<Character, EmployeeTrieNode>() {
                public EmployeeTrieNode apply(Character c) {
                    return new EmployeeTrieNode();
                }
            });
        }
        current.setEndOfWord(true);
        current.setEmployee(employee);
    }

    public static Employee findEmployee(String word) {
        EmployeeTrieNode current = root;
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            EmployeeTrieNode node = current.getChildren().get(ch);
            if (node == null) {
                return null;
            }
            current = node;
        }
        return current.getEmployee();
    }

    public static void delete(String word) {
        delete(root, word, 0);
    }

    private static boolean delete(EmployeeTrieNode current, String word, int index) {
        if (index == word.length()) {
            if (!current.isEndOfWord()) {
                return false;
            }
            current.setEndOfWord(false);
            return current.getChildren().isEmpty();
        }
        char ch = word.charAt(index);
        EmployeeTrieNode node = current.getChildren().get(ch);
        if (node == null) {
            return false;
        }
        boolean shouldDeleteCurrentNode = delete(node, word, index + 1) && !node.isEndOfWord();

        if (shouldDeleteCurrentNode) {
            current.getChildren().remove(ch);
            return current.getChildren().isEmpty();
        }
        return false;
    }
}
