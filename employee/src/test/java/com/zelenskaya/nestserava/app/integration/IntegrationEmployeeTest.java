package com.zelenskaya.nestserava.app.integration;

import com.zelenskaya.nestserava.app.controller.security.util.JwtUtilsEmployee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class IntegrationEmployeeTest extends BaseIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private JwtUtilsEmployee jwtUtils;

//    @Test
//    @Sql("/sql/employees.sql")
//    void addEmployee() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
//        headers.add(HttpHeaders.AUTHORIZATION,  HEADER_VALUE + JWT);
//
//        AddEmployeeDTO employeeDTO = new AddEmployeeDTO();
//        employeeDTO.setName(RIGHT_NAME_EMPLOYEE);
//        employeeDTO.setRecruitmentDate(RIGHT_RECRUITMENT_DATE);
//        employeeDTO.setTerminationDate(RIGHT_TERMINATION_DATE);
//        employeeDTO.setNameLegal(RIGHT_NAME_LEGAL);
//        employeeDTO.setIbanByn(RIGHT_IBAN_BYN);
//        employeeDTO.setIbanCurrency(RIGHT_IBAN_CURRENCY);
//
//        HttpEntity<String> entity = new HttpEntity(employeeDTO, headers);
//
//        ResponseEntity<Object> response = testRestTemplate.exchange(
//                createURLWithPort(RIGHT_URL_ADD_EMPLOYEE),
//                HttpMethod.POST,
//                entity,
//                Object.class
//        );
//        assertTrue(response.getStatusCode().equals(HttpStatus.CREATED));
//    }

    private String createURLWithPort(String url) {
        return "http://localhost:" + port + url;
    }
}