package com.example.rqchallenge.service.external.employee.getempl;

import com.example.rqchallenge.exceptions.ExternalServiceException;
import com.example.rqchallenge.service.external.AbstractExternalService;
import com.example.rqchallenge.service.external.employee.getempls.GetEmployeeParams;
import com.example.rqchallenge.service.external.employee.getempls.GetEmployeesResponse;
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
public class GetEmplService extends AbstractExternalService<GetEmplResponse, GetEmplParams> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetEmplService.class);
    private static final String BASE_URL = "https://dummy.restapiexample.com/api/v1/employee/";
    private RestTemplate restTemplate;

    @Autowired
    public GetEmplService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Logger getLogger() {
        return LOGGER;
    }

    @Override
    protected boolean validateRequest(Map<GetEmplParams, Object> args) {
        return args.containsKey(GetEmplParams.ID);
    }

    @Override
    protected boolean validateResponse(GetEmplResponse data) {
        if(data.getStatus().equals("success")){
            GetEmplResponse.EmplDetailsObj employeeDetails = data.getData();
            return null != employeeDetails;
        }
        return false;
    }

    @Override
    protected GetEmplResponse callingExternalService(Map<GetEmplParams, Object> args)
            throws URISyntaxException, ExternalServiceException {
        StringBuffer uriString = new StringBuffer(BASE_URL);
        uriString.append(args.get(GetEmplParams.ID).toString());

        URI uri = new URI(uriString.toString());

        ResponseEntity<GetEmplResponse> response = restTemplate.exchange(uri, HttpMethod.GET,
                null, GetEmplResponse.class );
        if (response.getStatusCode() != HttpStatus.OK || null == response.getBody()) {
            throw new ExternalServiceException("Failed to fetch persona response");
        }
        return response.getBody();
    }
}
