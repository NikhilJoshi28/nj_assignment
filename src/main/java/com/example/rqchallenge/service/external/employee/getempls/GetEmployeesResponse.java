package com.example.rqchallenge.service.external.employee.getempls;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class GetEmployeesResponse {

    private String status;
    private List<EmployeeDetails> data;
    @Data
    public class EmployeeDetails {
        private int id;
        private String name;
        private int salary;
        private int age;
        private String profileImage;
    }

}
