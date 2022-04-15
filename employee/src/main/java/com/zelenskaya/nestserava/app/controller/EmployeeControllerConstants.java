package com.zelenskaya.nestserava.app.controller;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Getter
@PropertySource("classpath:message.properties")
public class EmployeeControllerConstants {
    @Value("${message.key}")
    private String messageKey;
    @Value("${message.201}")
    private String message201;
    @Value("${message.400}")
    private String message400;
    @Value("${message.404.not.found}")
    private String message404NotFound;
    @Value("${message.404.not.exist}")
    private String message404NotExist;
    @Value("${message.409}")
    private String message409;
    @Value("${message.500}")
    private String message500;
}