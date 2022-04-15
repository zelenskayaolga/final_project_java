package com.zelenskaya.nestserava.app.service.impl;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Getter
@PropertySource({"classpath:default-value.properties"})
public class ServiceLegalConstants {
    @Value("${default.page}")
    private int defaultPage;
    @Value("${default.pages}")
    private Integer[] defaultPages;
}