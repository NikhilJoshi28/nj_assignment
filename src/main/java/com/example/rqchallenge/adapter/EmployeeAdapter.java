package com.example.rqchallenge.adapter;

import com.example.rqchallenge.entity.Employee;
import com.example.rqchallenge.service.external.employee.getempls.GetEmployeesResponse;

import java.util.ArrayList;
import java.util.List;

public class EmployeeAdapter {
    public static List<Employee> buildEmployee(List<GetEmployeesResponse.EmployeeDetails> employeeDetails){
        List<Employee> employees = new ArrayList<Employee>();
        for(GetEmployeesResponse.EmployeeDetails details : employeeDetails){
            employees.add(Employee.builder()
                            .id(details.getId())
                            .name(details.getName())
                            .salary(details.getSalary())
                            .age(details.getAge())
                            .build());
        }
        return employees;
    }

    public static List<String> buildEmployeeName(List<Employee> employees){
        List<String> names = new ArrayList<String>();
        for(Employee employee : employees){
            names.add(employee.getName());
        }
        return names;
    }
}
