package com.zelenskaya.nestserava.app.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class ApplicationConvTestConstants {
    static final String ADD_URL_TEMPLATE = "/api/v2/files";
    static final String MULTIPART_FORM_DATA_VALUE = MediaType.MULTIPART_FORM_DATA_VALUE;
    static final String URL_BY_ID_TEMPLATE = "/api/v2/applications/{id}";
    static final String URL_TEMPLATE = "/api/v2/applications";
    static final String JSON_CONTENT_TYPE = MediaType.APPLICATION_JSON_VALUE;
    static final String XML_CONTENT_TYPE = MediaType.APPLICATION_XML_VALUE;
    static final String HEADER_NAME = HttpHeaders.AUTHORIZATION;
    static final String BEARER_PREFIX = "Bearer ";
    static final String JWT = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ6ZWxlbnNrYXlva2siLCJ1c2VySWQiOjEsImlhdCI6MTY0NzYxNDgyN" +
            "ywiZXhwIjoxNjQ3ODc0MDI3fQ.BFrMotMqSoiTNNRx_PuVJfH15GElcI7zY-LX-X6jTojJtFyhlkB-JkJaw5j86iEUcPcQ0QSS15ZiZ7" +
            "_aS03vxg";
    static final String APPLICATION_CONV_UUID = "37978c5e-d9e0-402d-b7fc-1b551117813a";
    static final String NAME_FILE = "file";
    static final String APPLICATION_CONV_CVS = "applicationConv.cvs";
    static final String CVS_DATA = "<<cvs data>>";
    static final String MESSAGE_KEY = "message";
    static final Long APPLICATION_CONV_ID = 1L;
    static final String USERNAME = "olgazel";
    static final String NAME_LEGAL = "OMA";
    static final String ERRORS_KEY = "errors";
    static final String INVALID_UUID = "14b3d9e6-57a9-4839-a88a-4b9a";
    static final String BAD_URL_TEMPLATE = "/api/v2/application";
}
