package com.zelenskaya.nestserava.app.controllers.security.filter;

import com.zelenskaya.nestserava.app.config.JwtUtilsConfig;
import com.zelenskaya.nestserava.app.controllers.security.util.JwtUtilsCompany;
import com.zelenskaya.nestserava.app.service.PostJwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtilsConfig jwtUtilsConfig;
    @Autowired
    private JwtUtilsCompany jwtUtils;
    @Autowired
    private PostJwtService postJwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if (!jwtUtilsConfig.getServiceJwtToken().equals(jwt)) {
                if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                    Long userId = jwtUtils.getIdFromJwtToken(jwt);
                    ResponseEntity<Object> status = postJwtService.status(jwt);
                    if (status.getStatusCode().is2xxSuccessful()) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userId, null, null
                        );
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            } else {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        null, "service", null
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            log.error("Cannot set user authentication by login: {}", e.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader(HttpHeaders.AUTHORIZATION);
        String bearerPrefix = jwtUtilsConfig.getBearerPrefix();
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(bearerPrefix)) {
            return headerAuth.substring(bearerPrefix.length());
        } else {
            return null;
        }
    }
}
