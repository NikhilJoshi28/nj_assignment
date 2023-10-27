package com.example.rqchallenge.service.external;

import com.example.rqchallenge.exceptions.ExternalServiceException;
import com.example.rqchallenge.exceptions.InvalidResponseDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.util.Map;

public abstract class AbstractExternalService<O, P> {

    private Logger logger = LoggerFactory.getLogger(AbstractExternalService.class);

    public Logger getLogger() {
        return logger;
    }

    protected abstract boolean validateRequest(Map<P, Object> args);

    protected abstract boolean validateResponse(O data);

    public O callExternalService(Map<P, Object> args) throws InvalidResponseDataException, ExternalServiceException, URISyntaxException {
        getLogger().debug(
                String.format("External API call : Getting Data from service with request %s", args.toString())
        );
        if(validateRequest(args)){
          O data = callingExternalService(args);

          if(validateResponse(data)){
              return data;
          }
          else {
              throw new InvalidResponseDataException();
          }
        }
        else {
            getLogger().debug("Invalid Request");
        }
        return null;
    }

    protected abstract O callingExternalService(Map<P, Object> args)
            throws URISyntaxException, ExternalServiceException;
}
