package com.zelenskaya.nestserava.app.integration;

import com.zelenskaya.nestserava.app.controllers.security.util.JwtUtilsCompany;
import com.zelenskaya.nestserava.app.service.model.LegalDTO;
import com.zelenskaya.nestserava.app.service.model.TypeEnumDTO;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static com.zelenskaya.nestserava.app.integration.CompanyIntegrationTestConstants.HEADER_VALUES;
import static com.zelenskaya.nestserava.app.integration.CompanyIntegrationTestConstants.JWT;
import static com.zelenskaya.nestserava.app.integration.CompanyIntegrationTestConstants.RIGHT_IBAN_LEGAL;
import static com.zelenskaya.nestserava.app.integration.CompanyIntegrationTestConstants.RIGHT_NAME_LEGAL;
import static com.zelenskaya.nestserava.app.integration.CompanyIntegrationTestConstants.RIGHT_TOTAL_EMPLOYEES;
import static com.zelenskaya.nestserava.app.integration.CompanyIntegrationTestConstants.RIGHT_TYPE_LEGAL;
import static com.zelenskaya.nestserava.app.integration.CompanyIntegrationTestConstants.RIGHT_UNP_LEGAL;
import static com.zelenskaya.nestserava.app.integration.CompanyIntegrationTestConstants.RIGHT_URL_TEMPLATE;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class IntegrationCompanyTest extends BaseIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private JwtUtilsCompany jwtUtils;

    @Test
    @WithMockUser
    @Sql("/sql/legals.sql")
    void addLegal() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, HEADER_VALUES + JWT);

        LegalDTO legalDTO = new LegalDTO();
        legalDTO.setNameLegal(RIGHT_NAME_LEGAL);
        legalDTO.setUnp(RIGHT_UNP_LEGAL);
        legalDTO.setIban(RIGHT_IBAN_LEGAL);
        legalDTO.setTypeLegal(TypeEnumDTO.valueOf(RIGHT_TYPE_LEGAL));
        legalDTO.setTotalEmployees(RIGHT_TOTAL_EMPLOYEES);

        HttpEntity<String> entity = new HttpEntity(legalDTO, headers);

        ResponseEntity<Object> response = testRestTemplate.exchange(
                createURLWithPort(RIGHT_URL_TEMPLATE),
                HttpMethod.POST,
                entity,
                Object.class
        );
        assertTrue(response.getStatusCode().equals(HttpStatus.CREATED));
    }

    private String createURLWithPort(String url) {
        return "http://localhost:" + port + url;
    }
}
