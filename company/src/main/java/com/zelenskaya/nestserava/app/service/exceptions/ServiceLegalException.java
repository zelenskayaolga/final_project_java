package com.zelenskaya.nestserava.app.service.exceptions;

public class ServiceLegalException extends RuntimeException {
    public ServiceLegalException() {
    }

    public ServiceLegalException(String errorMessage) {
        super(errorMessage);
    }
}