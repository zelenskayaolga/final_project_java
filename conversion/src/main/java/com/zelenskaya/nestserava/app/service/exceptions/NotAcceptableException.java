package com.zelenskaya.nestserava.app.service.exceptions;

public class NotAcceptableException extends RuntimeException {
    public NotAcceptableException(String errorMessage) {
        super(errorMessage);
    }
}
