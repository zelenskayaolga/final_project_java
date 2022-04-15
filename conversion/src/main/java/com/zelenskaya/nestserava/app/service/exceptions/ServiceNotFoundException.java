package com.zelenskaya.nestserava.app.service.exceptions;

public class ServiceNotFoundException extends RuntimeException {
    public ServiceNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
