package com.zelenskaya.nestserava.app.controllers.security.filter;

import com.zelenskaya.nestserava.app.config.JwtUtilsConfig;
import com.zelenskaya.nestserava.app.controllers.security.util.JwtUtils;
import com.zelenskaya.nestserava.app.service.IsActiveSessionService;
import com.zelenskaya.nestserava.app.service.IsActiveUserService;
import com.zelenskaya.nestserava.app.service.UpdateUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class AuthTokenFilterByUsername extends OncePerRequestFilter {

    @Autowired
    private JwtUtilsConfig jwtUtilsConfig;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private IsActiveUserService isActiveUserService;
    @Autowired
    private IsActiveSessionService isActiveSessionService;
    @Autowired
    private UpdateUserDetailsService updateUserDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if (!jwtUtilsConfig.getServiceJwtToken().equals(jwt)) {
                String username = jwtUtils.getUsernameFromJwtToken(jwt);
                if (isValid(jwt, username)) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    updateUserDetailsService.updateAuthorizationDateByUsername(username);
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

    private boolean isValid(String jwt, String username) {
        return jwt != null &&
                jwtUtils.validateJwtToken(jwt) &&
                isActiveUserService.isActiveUserByUsername(username) &&
                isActiveSessionService.isActive(jwt);
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
