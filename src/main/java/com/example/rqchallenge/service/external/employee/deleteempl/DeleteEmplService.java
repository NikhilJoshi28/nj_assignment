package com.example.rqchallenge.service.external.employee.deleteempl;

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
import java.util.Map;

@Service
public class DeleteEmplService extends AbstractExternalService<DeleteEmplResponse, DeleteEmplRequest> {


    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteEmplService.class);
    private static final String BASE_URL = "https://dummy.restapiexample.com/api/v1/delete/";

    private RestTemplate restTemplate;

    @Autowired
    public DeleteEmplService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Logger getLogger() {
        return LOGGER;
    }

    @Override
    protected boolean validateRequest(Map<DeleteEmplRequest, Object> args) {
        return args.containsKey(DeleteEmplRequest.ID);
    }

    @Override
    protected boolean validateResponse(DeleteEmplResponse data) {
        return true;
    }

    @Override
    protected DeleteEmplResponse callingExternalService(Map<DeleteEmplRequest, Object> args)
            throws URISyntaxException, ExternalServiceException {
        String uriString = BASE_URL + args.get(DeleteEmplRequest.ID).toString();
        ResponseEntity<DeleteEmplResponse> response = null;

        URI uri = new URI(uriString);
        response = restTemplate.exchange(uri, HttpMethod.DELETE, null, DeleteEmplResponse.class);
        if (response.getStatusCode() != HttpStatus.OK || null == response.getBody()) {
            throw new ExternalServiceException("Failed to fetch persona response");
        }
        return response.getBody();
    }
}
