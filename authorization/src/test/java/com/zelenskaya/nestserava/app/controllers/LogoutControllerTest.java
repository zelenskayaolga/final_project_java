package com.zelenskaya.nestserava.app.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.zelenskaya.nestserava.app.config.JwtUtilsConfig;
import com.zelenskaya.nestserava.app.controllers.security.point.AuthEntryPointJwt;
import com.zelenskaya.nestserava.app.controllers.security.util.JwtUtils;
import com.zelenskaya.nestserava.app.service.CloseSessionService;
import com.zelenskaya.nestserava.app.service.IsActiveSessionService;
import com.zelenskaya.nestserava.app.service.IsActiveUserService;
import com.zelenskaya.nestserava.app.service.UpdateUserDetailsService;
import com.zelenskaya.nestserava.app.service.model.LogoutDTO;
import com.zelenskaya.nestserava.app.service.model.UserDetailsImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = LogoutController.class)
class LogoutControllerTest {

    private static final String MAPPING_VERSION = "/api/v2";
    private static final String AUTH_LOGOUT_URI = "/auth/logout";
    private static final String BEARER_PREFIX = "Bearer ";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthEntryPointJwt authEntryPointJwt;
    @MockBean
    private JwtUtils jwtUtils;
    @MockBean
    private JwtUtilsConfig jwtUtilsConfig;
    @MockBean
    private CloseSessionService closeSessionService;
    @MockBean
    private UserDetailsService userDetailsService;
    @MockBean
    private IsActiveUserService isActiveUserService;
    @MockBean
    private IsActiveSessionService isActiveSessionService;
    @MockBean
    private UpdateUserDetailsService updateUserDetailsService;

    @Test
    void shouldReturn200WhenWePostUserWithOpenedSession() throws Exception {
        String accessToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJiYmJqYmJibCIsImlhdCI6MTY0Njg2MzkwMSwiZXhwIjoxNjQ2ODcxO" +
                "TAxfQ.EqAFswsvA3G_2L7Xq0xQJDAr-oF_cJjH_vpctF7pu9Yk3UJYds4PY5hQdGTPOPf3QyiBeffsJe4-yYwo9vPZxg";
        String serviceJwtToken = "iyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJiYmJqYmJibCIsImlhdCI6MTY0Njg2MzkwMSwiZXhwIjoxNjQ2O" +
                "DcxOTAxfQ.EqAFswsvA3G_2L7Xq0xQJDAr-oF_cJjH_vpctF7pu9Yk3UJYds4PY5hQdGTPOPf3QyiBeffsJe4-yYwo9vPZxg";

        String username = "username";

        LogoutDTO logoutDTO = new LogoutDTO();
        logoutDTO.setUsername(username);
        logoutDTO.setSessionId(accessToken);

        UserDetails userDetails = new UserDetailsImpl(
                1L,
                "username",
                "password",
                "usermail",
                "firstName",
                null
        );

        when(jwtUtils.getUsernameFromJwtToken(accessToken)).thenReturn(logoutDTO.getUsername());
        when(jwtUtilsConfig.getBearerPrefix()).thenReturn(BEARER_PREFIX);
        when(jwtUtilsConfig.getServiceJwtToken()).thenReturn(serviceJwtToken);
        when(jwtUtils.validateJwtToken(logoutDTO.getSessionId())).thenReturn(true);
        when(isActiveUserService.isActiveUserByUsername(logoutDTO.getUsername())).thenReturn(true);
        when(isActiveSessionService.isActive(logoutDTO.getSessionId())).thenReturn(true);
        when(userDetailsService.loadUserByUsername(logoutDTO.getUsername())).thenReturn(userDetails);
        when(updateUserDetailsService.updateAuthorizationDateByUsername(username)).thenReturn(true);
        when(closeSessionService.close(logoutDTO)).thenReturn(true);
        mockMvc.perform(post(MAPPING_VERSION + AUTH_LOGOUT_URI)
                        .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + accessToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(logoutDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn415WhenWePostWithXmlContentType() throws Exception {
        String accessToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJiYmJqYmJibCIsImlhdCI6MTY0Njg2MzkwMSwiZXhwIjoxNjQ2ODcxO" +
                "TAxfQ.EqAFswsvA3G_2L7Xq0xQJDAr-oF_cJjH_vpctF7pu9Yk3UJYds4PY5hQdGTPOPf3QyiBeffsJe4-yYwo9vPZxg";
        String serviceJwtToken = "iyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJiYmJqYmJibCIsImlhdCI6MTY0Njg2MzkwMSwiZXhwIjoxNjQ2O" +
                "DcxOTAxfQ.EqAFswsvA3G_2L7Xq0xQJDAr-oF_cJjH_vpctF7pu9Yk3UJYds4PY5hQdGTPOPf3QyiBeffsJe4-yYwo9vPZxg";

        String username = "username";

        LogoutDTO logoutDTO = new LogoutDTO();
        logoutDTO.setUsername(username);
        logoutDTO.setSessionId(accessToken);

        UserDetails userDetails = new UserDetailsImpl(
                1L,
                "username",
                "password",
                "usermail",
                "firstName",
                null
        );

        when(jwtUtils.getUsernameFromJwtToken(accessToken)).thenReturn(logoutDTO.getUsername());
        when(jwtUtilsConfig.getBearerPrefix()).thenReturn(BEARER_PREFIX);
        when(jwtUtilsConfig.getServiceJwtToken()).thenReturn(serviceJwtToken);
        when(jwtUtils.validateJwtToken(logoutDTO.getSessionId())).thenReturn(true);
        when(isActiveUserService.isActiveUserByUsername(logoutDTO.getUsername())).thenReturn(true);
        when(isActiveSessionService.isActive(logoutDTO.getSessionId())).thenReturn(true);
        when(userDetailsService.loadUserByUsername(logoutDTO.getUsername())).thenReturn(userDetails);
        when(updateUserDetailsService.updateAuthorizationDateByUsername(username)).thenReturn(true);
        mockMvc.perform(post(MAPPING_VERSION + AUTH_LOGOUT_URI)
                        .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + accessToken)
                        .contentType(MediaType.APPLICATION_ATOM_XML)
                        .content(objectMapper.writeValueAsString(logoutDTO)))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void shouldReturn404WhenInvalidUrlForPost() throws Exception {
        String accessToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJiYmJqYmJibCIsImlhdCI6MTY0Njg2MzkwMSwiZXhwIjoxNjQ2ODcxO" +
                "TAxfQ.EqAFswsvA3G_2L7Xq0xQJDAr-oF_cJjH_vpctF7pu9Yk3UJYds4PY5hQdGTPOPf3QyiBeffsJe4-yYwo9vPZxg";
        String serviceJwtToken = "iyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJiYmJqYmJibCIsImlhdCI6MTY0Njg2MzkwMSwiZXhwIjoxNjQ2O" +
                "DcxOTAxfQ.EqAFswsvA3G_2L7Xq0xQJDAr-oF_cJjH_vpctF7pu9Yk3UJYds4PY5hQdGTPOPf3QyiBeffsJe4-yYwo9vPZxg";

        String username = "username";

        LogoutDTO logoutDTO = new LogoutDTO();
        logoutDTO.setUsername(username);
        logoutDTO.setSessionId(accessToken);

        UserDetails userDetails = new UserDetailsImpl(
                1L,
                "username",
                "password",
                "usermail",
                "firstName",
                null
        );

        when(jwtUtils.getUsernameFromJwtToken(accessToken)).thenReturn(logoutDTO.getUsername());
        when(jwtUtilsConfig.getBearerPrefix()).thenReturn(BEARER_PREFIX);
        when(jwtUtilsConfig.getServiceJwtToken()).thenReturn(serviceJwtToken);
        when(jwtUtils.validateJwtToken(logoutDTO.getSessionId())).thenReturn(true);
        when(isActiveUserService.isActiveUserByUsername(logoutDTO.getUsername())).thenReturn(true);
        when(isActiveSessionService.isActive(logoutDTO.getSessionId())).thenReturn(true);
        when(userDetailsService.loadUserByUsername(logoutDTO.getUsername())).thenReturn(userDetails);
        when(updateUserDetailsService.updateAuthorizationDateByUsername(username)).thenReturn(true);
        mockMvc.perform(post(MAPPING_VERSION + "/auth/log")
                        .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + accessToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(logoutDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn405WhenInvalidMethodForPost() throws Exception {
        String accessToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJiYmJqYmJibCIsImlhdCI6MTY0Njg2MzkwMSwiZXhwIjoxNjQ2ODcxO" +
                "TAxfQ.EqAFswsvA3G_2L7Xq0xQJDAr-oF_cJjH_vpctF7pu9Yk3UJYds4PY5hQdGTPOPf3QyiBeffsJe4-yYwo9vPZxg";
        String serviceJwtToken = "iyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJiYmJqYmJibCIsImlhdCI6MTY0Njg2MzkwMSwiZXhwIjoxNjQ2O" +
                "DcxOTAxfQ.EqAFswsvA3G_2L7Xq0xQJDAr-oF_cJjH_vpctF7pu9Yk3UJYds4PY5hQdGTPOPf3QyiBeffsJe4-yYwo9vPZxg";

        String username = "username";

        LogoutDTO logoutDTO = new LogoutDTO();
        logoutDTO.setUsername(username);
        logoutDTO.setSessionId(accessToken);

        UserDetails userDetails = new UserDetailsImpl(
                1L,
                "username",
                "password",
                "usermail",
                "firstName",
                null
        );

        when(jwtUtils.getUsernameFromJwtToken(accessToken)).thenReturn(logoutDTO.getUsername());
        when(jwtUtilsConfig.getBearerPrefix()).thenReturn(BEARER_PREFIX);
        when(jwtUtilsConfig.getServiceJwtToken()).thenReturn(serviceJwtToken);
        when(jwtUtils.validateJwtToken(logoutDTO.getSessionId())).thenReturn(true);
        when(isActiveUserService.isActiveUserByUsername(logoutDTO.getUsername())).thenReturn(true);
        when(isActiveSessionService.isActive(logoutDTO.getSessionId())).thenReturn(true);
        when(userDetailsService.loadUserByUsername(logoutDTO.getUsername())).thenReturn(userDetails);
        when(updateUserDetailsService.updateAuthorizationDateByUsername(username)).thenReturn(true);
        mockMvc.perform(delete(MAPPING_VERSION + AUTH_LOGOUT_URI)
                        .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + accessToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(logoutDTO)))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void shouldReturn200WhenWePostUserWithClosedSession() throws Exception {
        String accessToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJiYmJqYmJibCIsImlhdCI6MTY0Njg2MzkwMSwiZXhwIjoxNjQ2ODcxO" +
                "TAxfQ.EqAFswsvA3G_2L7Xq0xQJDAr-oF_cJjH_vpctF7pu9Yk3UJYds4PY5hQdGTPOPf3QyiBeffsJe4-yYwo9vPZxg";
        String serviceJwtToken = "iyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJiYmJqYmJibCIsImlhdCI6MTY0Njg2MzkwMSwiZXhwIjoxNjQ2O" +
                "DcxOTAxfQ.EqAFswsvA3G_2L7Xq0xQJDAr-oF_cJjH_vpctF7pu9Yk3UJYds4PY5hQdGTPOPf3QyiBeffsJe4-yYwo9vPZxg";

        String username = "username";

        LogoutDTO logoutDTO = new LogoutDTO();
        logoutDTO.setUsername(username);
        logoutDTO.setSessionId(accessToken);

        UserDetails userDetails = new UserDetailsImpl(
                1L,
                "username",
                "password",
                "usermail",
                "firstName",
                null
        );

        when(jwtUtils.getUsernameFromJwtToken(accessToken)).thenReturn(logoutDTO.getUsername());
        when(jwtUtilsConfig.getBearerPrefix()).thenReturn(BEARER_PREFIX);
        when(jwtUtilsConfig.getServiceJwtToken()).thenReturn(serviceJwtToken);
        when(jwtUtils.validateJwtToken(logoutDTO.getSessionId())).thenReturn(true);
        when(isActiveUserService.isActiveUserByUsername(logoutDTO.getUsername())).thenReturn(true);
        when(isActiveSessionService.isActive(logoutDTO.getSessionId())).thenReturn(true);
        when(userDetailsService.loadUserByUsername(logoutDTO.getUsername())).thenReturn(userDetails);
        when(updateUserDetailsService.updateAuthorizationDateByUsername(username)).thenReturn(true);
        when(closeSessionService.close(logoutDTO)).thenReturn(false);
        mockMvc.perform(post(MAPPING_VERSION + AUTH_LOGOUT_URI)
                        .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + accessToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(logoutDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldMapsToBusinessModelWhenWeCloseSessionWhichWasOpened() throws Exception {
        String accessToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJiYmJqYmJibCIsImlhdCI6MTY0Njg2MzkwMSwiZXhwIjoxNjQ2ODcxO" +
                "TAxfQ.EqAFswsvA3G_2L7Xq0xQJDAr-oF_cJjH_vpctF7pu9Yk3UJYds4PY5hQdGTPOPf3QyiBeffsJe4-yYwo9vPZxg";
        String serviceJwtToken = "iyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJiYmJqYmJibCIsImlhdCI6MTY0Njg2MzkwMSwiZXhwIjoxNjQ2O" +
                "DcxOTAxfQ.EqAFswsvA3G_2L7Xq0xQJDAr-oF_cJjH_vpctF7pu9Yk3UJYds4PY5hQdGTPOPf3QyiBeffsJe4-yYwo9vPZxg";

        String username = "username";

        LogoutDTO logoutDTO = new LogoutDTO();
        logoutDTO.setUsername(username);
        logoutDTO.setSessionId(accessToken);

        UserDetails userDetails = new UserDetailsImpl(
                1L,
                "username",
                "password",
                "usermail",
                "firstName",
                null
        );

        when(jwtUtils.getUsernameFromJwtToken(accessToken)).thenReturn(logoutDTO.getUsername());
        when(jwtUtilsConfig.getBearerPrefix()).thenReturn(BEARER_PREFIX);
        when(jwtUtilsConfig.getServiceJwtToken()).thenReturn(serviceJwtToken);
        when(jwtUtils.validateJwtToken(logoutDTO.getSessionId())).thenReturn(true);
        when(isActiveUserService.isActiveUserByUsername(logoutDTO.getUsername())).thenReturn(true);
        when(isActiveSessionService.isActive(logoutDTO.getSessionId())).thenReturn(true);
        when(userDetailsService.loadUserByUsername(logoutDTO.getUsername())).thenReturn(userDetails);
        when(updateUserDetailsService.updateAuthorizationDateByUsername(username)).thenReturn(true);
        when(closeSessionService.close(logoutDTO)).thenReturn(true);
        MvcResult mvcResult = mockMvc.perform(post(MAPPING_VERSION + AUTH_LOGOUT_URI)
                        .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + accessToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(logoutDTO)))
                .andExpect(status().isOk())
                .andReturn();
        verify(closeSessionService, times(1)).close(logoutDTO);
        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Map<String, String> result = objectMapper.readValue(actualResponseBody, Map.class);
        Assertions.assertEquals(Map.of("message", "Сессия с указанным sessionId закрыта"), result);
    }

    @Test
    void shouldMapsToBusinessModelWhenAllSessionsForUserAreClosed() throws Exception {
        String accessToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJiYmJqYmJibCIsImlhdCI6MTY0Njg2MzkwMSwiZXhwIjoxNjQ2ODcxO" +
                "TAxfQ.EqAFswsvA3G_2L7Xq0xQJDAr-oF_cJjH_vpctF7pu9Yk3UJYds4PY5hQdGTPOPf3QyiBeffsJe4-yYwo9vPZxg";
        String serviceJwtToken = "iyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJiYmJqYmJibCIsImlhdCI6MTY0Njg2MzkwMSwiZXhwIjoxNjQ2O" +
                "DcxOTAxfQ.EqAFswsvA3G_2L7Xq0xQJDAr-oF_cJjH_vpctF7pu9Yk3UJYds4PY5hQdGTPOPf3QyiBeffsJe4-yYwo9vPZxg";

        String username = "username";

        LogoutDTO logoutDTO = new LogoutDTO();
        logoutDTO.setUsername(username);
        logoutDTO.setSessionId(accessToken);

        UserDetails userDetails = new UserDetailsImpl(
                1L,
                "username",
                "password",
                "usermail",
                "firstName",
                null
        );

        when(jwtUtils.getUsernameFromJwtToken(accessToken)).thenReturn(logoutDTO.getUsername());
        when(jwtUtilsConfig.getBearerPrefix()).thenReturn(BEARER_PREFIX);
        when(jwtUtilsConfig.getServiceJwtToken()).thenReturn(serviceJwtToken);
        when(jwtUtils.validateJwtToken(logoutDTO.getSessionId())).thenReturn(true);
        when(isActiveUserService.isActiveUserByUsername(logoutDTO.getUsername())).thenReturn(true);
        when(isActiveSessionService.isActive(logoutDTO.getSessionId())).thenReturn(true);
        when(userDetailsService.loadUserByUsername(logoutDTO.getUsername())).thenReturn(userDetails);
        when(updateUserDetailsService.updateAuthorizationDateByUsername(username)).thenReturn(true);
        when(closeSessionService.close(logoutDTO)).thenReturn(false);
        MvcResult mvcResult = mockMvc.perform(post(MAPPING_VERSION + AUTH_LOGOUT_URI)
                        .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + accessToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(logoutDTO)))
                .andExpect(status().isOk())
                .andReturn();
        verify(closeSessionService, times(1)).close(logoutDTO);
        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Map<String, String> result = objectMapper.readValue(actualResponseBody, Map.class);
        Assertions.assertEquals(Map.of(
                "message", "Сессия с указанным sessionId не активна. Все активные сессии закрыты"), result
        );
    }
}