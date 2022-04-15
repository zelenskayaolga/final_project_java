package com.zelenskaya.nestserava.app.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class LegalControllerTestConstants {
    static final String RIGHT_URL_TEMPLATE = "/api/v2/legals";
    static final String WRONG_URL_TEMPLATE = "/api/v2/users";
    static final String RIGHT_URL_TEMPLATE_FOR_FEIGN_LEGAL_CONTROLLER = "/api/v2/legal";
    static final String RIGHT_URL_TEMPLATE_FOR_GET_LEGAL_CONTROLLER = "/api/v2/legals/{id}";
    static final String JSON_CONTENT_TYPE = MediaType.APPLICATION_JSON_VALUE;
    static final String XML_CONTENT_TYPE = MediaType.APPLICATION_XML_VALUE;
    static final String HEADER_NAME = HttpHeaders.AUTHORIZATION;
    static final String HEADER_VALUES = "Bearer ";
    static final String SERVICE_JWT_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ6ZWxlbnNrYXlva2siLCJ1c2V" +
            "ySWQiOjEsImlhdCI6MTY0NzYxNDgyNywiZXhwIjoxNjQ3ODc0MDI3fQ.BFrMotMqSoiTNNRx_" +
            "PuVJfH15GElcI7zY-LX-X6jTojJtFyhlkB-JkJaw5j86iEUcPcQ0QSS15ZiZ7_aS03vxg";
    static final String JWT = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ6ZWxlbnNrYXlva2siLCJ1c2VySWQiOjEsImlhdCI6MTY0NzYxNDgyN" +
            "ywiZXhwIjoxNjQ3ODc0MDI3fQ.BFrMotMqSoiTNNRx_PuVJfH15GElcI7zY-LX-X6jTojJtFyhlkB-JkJaw5j86iEUcPcQ0QSS15ZiZ7" +
            "_aS03vxg";

    static final Long AUTHORIZED_USER_ID = 1L;

    static final long RIGHT_LEGAL_ID = 1L;
    static final String RIGHT_NAME_LEGAL = "Company";
    static final int RIGHT_UNP_LEGAL = 100000000;
    static final String RIGHT_IBAN_LEGAL = "BY00UNBS00000000000000000000";
    static final int RIGHT_TOTAL_EMPLOYEES = 1000;
    static final String RIGHT_TYPE_LEGAL = "RESIDENT";

    static final String MESSAGE_KEY = "message";
    static final String MESSAGE_201 = "\\u041a\\u043e\\u043c\\u043f\\u0430\\u043d\\u0438\\u044f\\u0020\\u0443\\u0441\\u043f\\u0435\\u0448\\u043d\\u043e\\u0020\\u0441\\u043e\\u0437\\u0434\\u0430\\u043d\\u0430";
    static final String MESSAGE_400 = "\\u041d\\u0435\\u0432\\u0435\\u0440\\u043d\\u043e\\u0020\\u0437\\u0430\\u0434\\u0430\\u043d\\u044b\\u0020\\u043f\\u0430\\u0440\\u0430\\u043c\\u0435\\u0442\\u0440\\u044b";
    static final String MESSAGE_404_NOT_FOUND = "\\u041a\\u043e\\u043c\\u043f\\u0430\\u043d\\u0438\\u044f\\u0020\\u043d\\u0435\\u0020\\u043d\\u0430\\u0439\\u0434\\u0435\\u043d\\u0430\\u002c\\u0020\\u0438\\u0437\\u043c\\u0435\\u043d\\u0438\\u0442\\u0435\\u0020\\u043f\\u0430\\u0440\\u0430\\u043c\\u0435\\u0442\\u0440\\u044b\\u0020\\u043f\\u043e\\u0438\\u0441\\u043a\\u0430";
    static final String MESSAGE_409 = "\\u041a\\u043e\\u043c\\u043f\\u0430\\u043d\\u0438\\u044f\\u0020\\u0441\\u0443\\u0449\\u0435\\u0441\\u0442\\u0432\\u0443\\u0435\\u0442\\u0020\\u0441\\u0020\\u043f\\u0430\\u0440\\u0430\\u043c\\u0435\\u0442\\u0440\\u0430\\u043c\\u0438\\u0020";
    static final String MESSAGE_409_SEPARATOR = "\u002c\u0020";
    static final String MESSAGE_500 = "\\u0421\\u0435\\u0440\\u0432\\u0435\\u0440\\u0020\\u043d\\u0435\\u0020\\u0434\\u043e\\u0441\\u0442\\u0443\\u043f\\u0435\\u043d\\u002e\\u0020\\u0421\\u0432\\u044f\\u0436\\u0438\\u0442\\u0435\\u0441\\u044c\\u0020\\u0441\\u0020\\u0430\\u0434\\u043c\\u0438\\u043d\\u0438\\u0441\\u0442\\u0440\\u0430\\u0442\\u043e\\u0440\\u043e\\u043c";

    private LegalControllerTestConstants() {
    }
}