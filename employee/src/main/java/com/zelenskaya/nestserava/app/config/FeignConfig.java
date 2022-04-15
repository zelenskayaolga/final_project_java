package com.zelenskaya.nestserava.app.config;

import feign.RequestInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Configuration
@EnableFeignClients(basePackages = "com.zelenskaya.nestserava.app.service")
@AllArgsConstructor
public class FeignConfig {
    private JwtUtilsConfig jwtUtilsConfig;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header(
                    HttpHeaders.AUTHORIZATION, jwtUtilsConfig.getBearerPrefix() + jwtUtilsConfig.getServiceJwtToken()
            );
            requestTemplate.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        };
    }
}