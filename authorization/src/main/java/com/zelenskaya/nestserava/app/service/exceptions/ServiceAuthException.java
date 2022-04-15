package com.zelenskaya.nestserava.app.service.exceptions;

public class ServiceAuthException extends RuntimeException {
    public ServiceAuthException(String errorMessage) {
        super(errorMessage);
    }
}
