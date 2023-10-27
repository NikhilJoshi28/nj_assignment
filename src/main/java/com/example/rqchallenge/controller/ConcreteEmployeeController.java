package com.example.rqchallenge.controller;

import com.example.rqchallenge.entity.Employee;
import com.example.rqchallenge.service.EmployeeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class ConcreteEmployeeController implements IEmployeeController {

    private EmployeeManager employeeManager;

    @Autowired
    public ConcreteEmployeeController(EmployeeManager employeeManager) {
        this.employeeManager = employeeManager;
    }

    public ResponseEntity<List<Employee>> getAllEmployees() throws IOException {
        return new ResponseEntity<List<Employee>>(employeeManager.getAllEmployees(), HttpStatus.OK);
    }

    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(String searchString) {
        return new ResponseEntity<List<Employee>>(employeeManager.searchEmployee(searchString), HttpStatus.OK);
    }

    public ResponseEntity<Employee> getEmployeeById(String id) {
        Employee employee = employeeManager.getEmployeeDetails(id);
        if (null == employee) {
            return new ResponseEntity<Employee>(employee, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Employee>(employee, HttpStatus.OK);
    }

    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        Integer emplId = employeeManager.getHighestPaidEmployee();
        return new ResponseEntity<Integer>(emplId, HttpStatus.OK);
    }

    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        List<String> emplyeeNames = employeeManager.getTopTenEmpl();
        return new ResponseEntity<List<String>>(emplyeeNames, HttpStatus.OK);
    }

    public ResponseEntity<Employee> createEmployee(Map<String, Object> employeeInput) {
        Employee employee = employeeManager.createEmployee(employeeInput);
        return new ResponseEntity<Employee>(employee, HttpStatus.OK);
    }

    public ResponseEntity<String> deleteEmployeeById(String id) {
        String employeeName = employeeManager.deleteEmployee(id);

        if (null == employeeName) {
            return new ResponseEntity<String>("null", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(employeeName, HttpStatus.OK);
    }
}
