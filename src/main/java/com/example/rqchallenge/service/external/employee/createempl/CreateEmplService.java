package com.example.rqchallenge.service.external.employee.createempl;

import com.example.rqchallenge.exceptions.ExternalServiceException;
import com.example.rqchallenge.service.external.AbstractExternalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Objects;

@Service
public class CreateEmplService extends AbstractExternalService<CreateEmplResponse, CreateEmplRequestParams> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateEmplService.class);
    private static final String BASE_URL = "https://dummy.restapiexample.com/api/v1/create";
    private RestTemplate restTemplate;

    @Autowired
    public CreateEmplService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Logger getLogger() {
        return LOGGER;
    }

    @Override
    protected boolean validateRequest(Map<CreateEmplRequestParams, Object> args) {
        if (!args.containsKey(CreateEmplRequestParams.NAME) ||
                !args.containsKey(CreateEmplRequestParams.AGE) ||
                !args.containsKey(CreateEmplRequestParams.SALARY)) {
            return false;
        }
        return true;
    }

    @Override
    protected boolean validateResponse(CreateEmplResponse data) {
        if (data.getStatus().equals("success")) {
            CreateEmplResponse.EmplObj employeeDetails = data.getData();
            return null != employeeDetails;
        }
        return false;
    }

    @Override
    protected CreateEmplResponse callingExternalService(Map<CreateEmplRequestParams, Object> args)
            throws URISyntaxException, ExternalServiceException {

        CreateEmplRequest request = CreateEmplRequest.builder()
                .age(args.get(CreateEmplRequestParams.AGE).toString())
                .name(args.get(CreateEmplRequestParams.NAME).toString())
                .salary(args.get(CreateEmplRequestParams.SALARY).toString())
                .build();


        URI uri = new URI(BASE_URL);
        ResponseEntity<CreateEmplResponse> response = restTemplate.exchange(uri,
                HttpMethod.POST,
                new HttpEntity(request),
                CreateEmplResponse.class);

        if (response.getStatusCode() != HttpStatus.OK || null == response.getBody()) {
            throw new ExternalServiceException("Failed to fetch persona response");
        }
        return response.getBody();
    }
}
