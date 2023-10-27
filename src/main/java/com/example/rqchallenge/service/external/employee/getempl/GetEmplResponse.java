package com.example.rqchallenge.service.external.employee.getempl;

import lombok.Builder;
import lombok.Data;

@Data
public class GetEmplResponse {
    private String status;
    private EmplDetailsObj data;

    @Data
    public class EmplDetailsObj {
        private int id;
        private String name;
        private long salary;
        private int age;
        private String profileImage;
    }
}
