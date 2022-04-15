package com.zelenskaya.nestserava.app.service.exception;

public class ServiceEmployeeException extends RuntimeException {
    public ServiceEmployeeException() {
    }

    public ServiceEmployeeException(String message) {
        super(message);
    }
}