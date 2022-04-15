package com.zelenskaya.nestserava.app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zelenskaya.nestserava.app.config.JwtUtilsConfig;
import com.zelenskaya.nestserava.app.controllers.security.point.AuthEntryPointJwt;
import com.zelenskaya.nestserava.app.controllers.security.util.JwtUtils;
import com.zelenskaya.nestserava.app.service.IsActiveSessionService;
import com.zelenskaya.nestserava.app.service.IsActiveUserService;
import com.zelenskaya.nestserava.app.service.UpdateUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = SessionController.class)
class SessionControllerTest {

    private static final String MAPPING_VERSION = "/api/v2";
    private static final String SESSION_URI = "/session";
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
    private IsActiveSessionService isActiveSessionService;
    @MockBean
    private UserDetailsService userDetailsService;
    @MockBean
    private IsActiveUserService isActiveUserService;
    @MockBean
    private UpdateUserDetailsService updateUserDetailsService;

    @Test
    void shouldReturn405WhenInvalidMethodForPost() throws Exception {
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ6ZWxlbnNrYXlva2siLCJ1c2V" +
                "ySWQiOjEsImlhdCI6MTY0NzYxNDgyNywiZXhwIjoxNjQ3ODc0MDI3fQ.BFrMotMqSoiTNNRx_" +
                "PuVJfH15GElcI7zY-LX-X6jTojJtFyhlkB-JkJaw5j86iEUcPcQ0QSS15ZiZ7_aS03vxg";
        String serviceJwtToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ6ZWxlbnNrYXlva2siLCJ1c2V" +
                "ySWQiOjEsImlhdCI6MTY0NzYxNDgyNywiZXhwIjoxNjQ3ODc0MDI3fQ.BFrMotMqSoiTNNRx_" +
                "PuVJfH15GElcI7zY-LX-X6jTojJtFyhlkB-JkJaw5j86iEUcPcQ0QSS15ZiZ7_aS03vxg";

        when(jwtUtilsConfig.getBearerPrefix()).thenReturn(BEARER_PREFIX);
        when(jwtUtilsConfig.getServiceJwtToken()).thenReturn(serviceJwtToken);
        mockMvc.perform(delete(MAPPING_VERSION + SESSION_URI)
                        .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + serviceJwtToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(token))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void shouldReturn415WhenWePostWithXmlContentType() throws Exception {
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ6ZWxlbnNrYXlva2siLCJ1c2V" +
                "ySWQiOjEsImlhdCI6MTY0NzYxNDgyNywiZXhwIjoxNjQ3ODc0MDI3fQ.BFrMotMqSoiTNNRx_" +
                "PuVJfH15GElcI7zY-LX-X6jTojJtFyhlkB-JkJaw5j86iEUcPcQ0QSS15ZiZ7_aS03vxg";
        String serviceJwtToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ6ZWxlbnNrYXlva2siLCJ1c2V" +
                "ySWQiOjEsImlhdCI6MTY0NzYxNDgyNywiZXhwIjoxNjQ3ODc0MDI3fQ.BFrMotMqSoiTNNRx_" +
                "PuVJfH15GElcI7zY-LX-X6jTojJtFyhlkB-JkJaw5j86iEUcPcQ0QSS15ZiZ7_aS03vxg";

        when(jwtUtilsConfig.getBearerPrefix()).thenReturn(BEARER_PREFIX);
        when(jwtUtilsConfig.getServiceJwtToken()).thenReturn(serviceJwtToken);
        mockMvc.perform(post(MAPPING_VERSION + SESSION_URI)
                        .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + serviceJwtToken)
                        .contentType(MediaType.APPLICATION_ATOM_XML_VALUE)
                        .content(token))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void shouldReturn200WhenWePostAuthenticatedUser() throws Exception {
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ6ZWxlbnNrYXlva2siLCJ1c2V" +
                "ySWQiOjEsImlhdCI6MTY0NzYxNDgyNywiZXhwIjoxNjQ3ODc0MDI3fQ.BFrMotMqSoiTNNRx_" +
                "PuVJfH15GElcI7zY-LX-X6jTojJtFyhlkB-JkJaw5j86iEUcPcQ0QSS15ZiZ7_aS03vxg";
        String serviceJwtToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ6ZWxlbnNrYXlva2siLCJ1c2V" +
                "ySWQiOjEsImlhdCI6MTY0NzYxNDgyNywiZXhwIjoxNjQ3ODc0MDI3fQ.BFrMotMqSoiTNNRx_" +
                "PuVJfH15GElcI7zY-LX-X6jTojJtFyhlkB-JkJaw5j86iEUcPcQ0QSS15ZiZ7_aS03vxg";

        when(jwtUtilsConfig.getBearerPrefix()).thenReturn(BEARER_PREFIX);
        when(jwtUtilsConfig.getServiceJwtToken()).thenReturn(serviceJwtToken);
        when(isActiveSessionService.isActive(token)).thenReturn(true);
        mockMvc.perform(post(MAPPING_VERSION + SESSION_URI)
                        .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + serviceJwtToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(token))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn400WhenWePostUnauthenticatedUser() throws Exception {
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ6ZWxlbnNrYXlva2siLCJ1c2V" +
                "ySWQiOjEsImlhdCI6MTY0NzYxNDgyNywiZXhwIjoxNjQ3ODc0MDI3fQ.BFrMotMqSoiTNNRx_" +
                "PuVJfH15GElcI7zY-LX-X6jTojJtFyhlkB-JkJaw5j86iEUcPcQ0QSS15ZiZ7_aS03vxg";
        String serviceJwtToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ6ZWxlbnNrYXlva2siLCJ1c2V" +
                "ySWQiOjEsImlhdCI6MTY0NzYxNDgyNywiZXhwIjoxNjQ3ODc0MDI3fQ.BFrMotMqSoiTNNRx_" +
                "PuVJfH15GElcI7zY-LX-X6jTojJtFyhlkB-JkJaw5j86iEUcPcQ0QSS15ZiZ7_aS03vxg";

        when(jwtUtilsConfig.getBearerPrefix()).thenReturn(BEARER_PREFIX);
        when(jwtUtilsConfig.getServiceJwtToken()).thenReturn(serviceJwtToken);
        when(isActiveSessionService.isActive(token)).thenReturn(false);
        mockMvc.perform(post(MAPPING_VERSION + SESSION_URI)
                        .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + serviceJwtToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(token))
                .andExpect(status().isUnauthorized());
    }
}