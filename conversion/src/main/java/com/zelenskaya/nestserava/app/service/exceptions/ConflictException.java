package com.zelenskaya.nestserava.app.service.exceptions;

public class ConflictException extends RuntimeException {
    public ConflictException(String errorMessage) {
        super(errorMessage);
    }
}
