package com.example.rqchallenge.enums;

public enum EmployeeDesignation {

    ASSOCIATE_MANAGER("associate_manager"),
    MANAGER("manager"),
    VICE_PRESIDENT("vice_president"),
    PRESIDENT("president");
    private String key;


    EmployeeDesignation(String manager) {
    }


    public String getKey() {
        return key;
    }
}
