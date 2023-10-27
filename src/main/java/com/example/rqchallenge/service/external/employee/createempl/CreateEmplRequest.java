package com.example.rqchallenge.service.external.employee.createempl;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateEmplRequest {
    private String name;
    private String salary;
    private String age;
}
