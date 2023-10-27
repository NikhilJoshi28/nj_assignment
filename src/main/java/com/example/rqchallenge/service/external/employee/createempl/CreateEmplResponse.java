package com.example.rqchallenge.service.external.employee.createempl;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateEmplResponse {
    private String status;
    private EmplObj data;

    @Data
    public class EmplObj{
        private String name;
        private int age;
        private int salary;
        private int id;
    }
}
