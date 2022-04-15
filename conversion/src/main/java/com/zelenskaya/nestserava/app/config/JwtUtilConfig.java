package com.zelenskaya.nestserava.app.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class JwtUtilConfig {
    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.expiration.hours}")
    private long jwtExpirationHours;
    @Value("${service.jwt.token}")
    private String serviceJwtToken;
    @Value("${bearer.prefix}")
    private String bearerPrefix;
}
