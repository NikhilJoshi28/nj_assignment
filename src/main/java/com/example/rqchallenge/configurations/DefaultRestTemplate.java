package com.example.rqchallenge.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class DefaultRestTemplate {

    @Bean
    public RestTemplate customRestTemplate(){
        return new RestTemplate();
    }
}
