package com.zelenskaya.nestserava.app.service.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ServiceConfig {
    @Value("${time.zone}")
    private String zoneTime;
    @Value("${jwt.expiration.hours}")
    private Integer jwtExpirationHours;
}
