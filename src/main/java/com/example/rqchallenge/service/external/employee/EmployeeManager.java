package com.example.rqchallenge.service.external.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmployeeManager {

    private RestTemplate restTemplate;

    @Autowired
    public EmployeeManager(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


}
