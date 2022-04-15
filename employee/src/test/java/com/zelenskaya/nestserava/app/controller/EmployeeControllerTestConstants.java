package com.zelenskaya.nestserava.app.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EmployeeControllerTestConstants {
    static final String RIGHT_URL_FOR_ADD_EMPLOYEES = "/api/v2/employees";
    static final String WRONG_URL_FOR_ADD_EMPLOYEES = "/api/v2/users";
    static final String RIGHT_URL_FOR_FEIGN_EMPLOYEE = "/api/v2/employee";
    static final String URL_BY_ID_EMPLOYEE = "/api/v2/employees/{employeeId}";
    static final String URL_EMPLOYEE = "/api/v2/employees";
    static final String JSON_CONTENT_TYPE = MediaType.APPLICATION_JSON_VALUE;
    static final String XML_CONTENT_TYPE = MediaType.APPLICATION_XML_VALUE;
    static final String HEADER_NAME = HttpHeaders.AUTHORIZATION;
    static final String HEADER_VALUES = "Bearer ";
    static final String JWT = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ6ZWxlbnNrYXlva2siLCJ1c2VySWQiOjEsImlhdCI6MTY0NzYxNDgyN" +
            "ywiZXhwIjoxNjQ3ODc0MDI3fQ.BFrMotMqSoiTNNRx_PuVJfH15GElcI7zY-LX-X6jTojJtFyhlkB-JkJaw5j86iEUcPcQ0QSS15ZiZ7" +
            "_aS03vxg";
    static final String SERVICE_JWT_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ6ZWxlbnNrYXlva2siLCJ1c2V" +
            "ySWQiOjEsImlhdCI6MTY0NzYxNDgyNywiZXhwIjoxNjQ3ODc0MDI3fQ.BFrMotMqSoiTNNRx_" +
            "PuVJfH15GElcI7zY-LX-X6jTojJtFyhlkB-JkJaw5j86iEUcPcQ0QSS15ZiZ7_aS03vxg";

    static final Long AUTHORIZED_USER_ID = 1L;
    static final String MESSAGE_KEY = "message";
    static final String MESSAGE_400 = "\\u041d\\u0435\\u0432\\u0435\\u0440\\u043d\\u043e\\u0020\\u0437\\u0430\\u0434\\u0430\\u043d\\u044b\\u0020\\u043f\\u0430\\u0440\\u0430\\u043c\\u0435\\u0442\\u0440\\u044b";
    static final String MESSAGE_404_NOT_FOUND = "\\u041a\\u043e\\u043c\\u043f\\u0430\\u043d\\u0438\\u044f\\u0020\\u043d\\u0435\\u0020\\u043d\\u0430\\u0439\\u0434\\u0435\\u043d\\u0430\\u002c\\u0020\\u0438\\u0437\\u043c\\u0435\\u043d\\u0438\\u0442\\u0435\\u0020\\u043f\\u0430\\u0440\\u0430\\u043c\\u0435\\u0442\\u0440\\u044b\\u0020\\u043f\\u043e\\u0438\\u0441\\u043a\\u0430";
    static final Long RIGHT_ID = 1L;
    static final String RIGHT_NAME_EMPLOYEE = "Александр";
    static final String RIGHT_IBAN_BYN = "BY00UNBS00000000000000000000";
    static final String RIGHT_IBAN_CURRENCY = "LT00UNBS00000000000000000000";
    static final String RIGHT_NAME_LEGAL = "Company";
    static final LocalDate RIGHT_RECRUITMENT_DATE = LocalDate.parse(
            "22/03/2022", DateTimeFormatter.ofPattern("dd/MM/yyyy")
    );
    static final LocalDate RIGHT_TERMINATION_DATE = LocalDate.parse(
            "12/12/2022", DateTimeFormatter.ofPattern("dd/MM/yyyy")
    );

    private EmployeeControllerTestConstants() {
    }
}