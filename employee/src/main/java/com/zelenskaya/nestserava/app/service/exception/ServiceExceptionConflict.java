package com.zelenskaya.nestserava.app.service.exception;

public class ServiceExceptionConflict extends RuntimeException {
    public ServiceExceptionConflict(String errorMessage) {
        super(errorMessage);
    }
}
