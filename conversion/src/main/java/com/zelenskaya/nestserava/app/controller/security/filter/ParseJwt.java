package com.zelenskaya.nestserava.app.controller.security.filter;

import com.zelenskaya.nestserava.app.config.JwtUtilConfig;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

@Component
@AllArgsConstructor
public class ParseJwt {
    private JwtUtilConfig jwtUtilConfig;

    public String parse(HttpServletRequest request) {
        String headerAuth = request.getHeader(HttpHeaders.AUTHORIZATION);
        String bearerPrefix = jwtUtilConfig.getBearerPrefix();
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(bearerPrefix)) {
            return headerAuth.substring(bearerPrefix.length());
        } else {
            return null;
        }
    }
}
