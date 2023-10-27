package com.example.rqchallenge.entity;

import com.example.rqchallenge.enums.EmployeeDesignation;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Employee {
    private int id;
    private String name;
    private String email;
    private EmployeeDesignation designation;
    private int age;
    private String phoneNumber;
    private int salary;
}
