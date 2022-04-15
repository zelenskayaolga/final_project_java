package com.zelenskaya.nestserava.app.integration;

import com.zelenskaya.nestserava.app.controllers.security.util.JwtUtils;
import com.zelenskaya.nestserava.app.service.model.LoginDTO;
import com.zelenskaya.nestserava.app.service.model.LogoutDTO;
import com.zelenskaya.nestserava.app.service.model.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class IntegrationAuthorizationTest extends BaseIT {

    private static final String MAPPING_VERSION = "/api/v2";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private JwtUtils jwtUtils;

    @Test
    @Sql("/sql/users.sql")
    void addUser() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("parameter");
        userDTO.setPassword("Password2!");
        userDTO.setFirstName("Анатолий");
        userDTO.setUsermail("usermail@gmail.com");

        HttpEntity<String> entity = new HttpEntity(userDTO, headers);

        ResponseEntity<Object> response = testRestTemplate.exchange(
                createURLWithPort(MAPPING_VERSION + "/auth/signin"),
                HttpMethod.POST,
                entity,
                Object.class
        );
        assertTrue(response.getStatusCode().equals(HttpStatus.CREATED));
    }

    @Test
    @Sql("/sql/login.sql")
    void authenticateUser() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("employee");
        loginDTO.setPassword("Password1!");

        HttpEntity<String> entity = new HttpEntity(loginDTO, headers);

        ResponseEntity<Object> response = testRestTemplate.exchange(
                createURLWithPort(MAPPING_VERSION + "/auth/login"),
                HttpMethod.POST,
                entity,
                Object.class
        );
        assertTrue(response.getStatusCode().equals(HttpStatus.OK));
    }

    @Test
    @Sql("/sql/logout.sql")
    void closeSession() {
        HttpHeaders headers = new HttpHeaders();
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ6ZWxlbnNrYXlhb2xnYSIsInVzZXJJZCI6MTAsImlhdCI6MTY0ODU3Njg5OCwiZXhwIjoxNjQ4ODM2MDk4fQ.Z4S7mTpbIw7uhX7aX0PD5wNRbIilJ59G-4clO7BTM_zpOdNcL4vl9cW8CbAFFNEVXHjC7MSocLD0-NakO-Tt_Q";
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        LogoutDTO logoutDTO = new LogoutDTO();
        logoutDTO.setUsername("zelenskayaolga");
        logoutDTO.setSessionId(token);

        HttpEntity<String> entity = new HttpEntity(logoutDTO, headers);

        ResponseEntity<Object> response = testRestTemplate.exchange(
                createURLWithPort(MAPPING_VERSION + "/auth/logout"),
                HttpMethod.POST,
                entity,
                Object.class
        );
        assertTrue(response.getStatusCode().equals(HttpStatus.OK));
    }

    private String createURLWithPort(String url) {
        return "http://localhost:" + port + url;
    }
}