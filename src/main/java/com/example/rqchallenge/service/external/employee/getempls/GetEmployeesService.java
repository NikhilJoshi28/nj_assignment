package com.example.rqchallenge.service.external.employee.getempls;

import com.example.rqchallenge.exceptions.ExternalServiceException;
import com.example.rqchallenge.service.external.AbstractExternalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

@Service
public class GetEmployeesService extends AbstractExternalService<GetEmployeesResponse, GetEmployeeParams> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetEmployeesService.class);
    private static final String BASE_URL = "https://dummy.restapiexample.com/api/v1/employees";

    private RestTemplate restTemplate;

    @Autowired
    public GetEmployeesService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Logger getLogger() {
        return LOGGER;
    }

    @Override
    protected boolean validateRequest(Map<GetEmployeeParams, Object> args) {
        return true;
    }

    @Override
    protected boolean validateResponse(GetEmployeesResponse data) {
        if(data.getStatus().equals("success")){
            List<GetEmployeesResponse.EmployeeDetails> employeeDetails = data.getData();
            return null != employeeDetails;
        }
        return false;
    }

    @Override
    protected GetEmployeesResponse callingExternalService(Map<GetEmployeeParams, Object> args) throws URISyntaxException, ExternalServiceException {
        StringBuffer uriString = new StringBuffer(BASE_URL);
        URI uri = new URI(uriString.toString());
        ResponseEntity<GetEmployeesResponse> response = restTemplate.exchange(uri, HttpMethod.GET,
                null, GetEmployeesResponse.class );
        if (response.getStatusCode() != HttpStatus.OK || null == response.getBody()) {
            throw new ExternalServiceException("Failed to fetch persona response");
        }
        return response.getBody();
    }
}
