package com.zelenskaya.nestserava.app.integration;

public class CompanyIntegrationTestConstants {
    static final String RIGHT_URL_TEMPLATE = "/api/v2/legals";
    static final String HEADER_VALUES = "Bearer ";
    static final String JWT = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ6ZWxlbnNrYXlva2siLCJ1c2VySWQiOjEsImlhdCI6MTY0NzYxNDgyN" +
            "ywiZXhwIjoxNjQ3ODc0MDI3fQ.BFrMotMqSoiTNNRx_PuVJfH15GElcI7zY-LX-X6jTojJtFyhlkB-JkJaw5j86iEUcPcQ0QSS15ZiZ7" +
            "_aS03vxg";

    static final String RIGHT_NAME_LEGAL = "Company";
    static final int RIGHT_UNP_LEGAL = 100000000;
    static final String RIGHT_IBAN_LEGAL = "BY00UNBS00000000000000000000";
    static final int RIGHT_TOTAL_EMPLOYEES = 1000;
    static final String RIGHT_TYPE_LEGAL = "RESIDENT";

    private CompanyIntegrationTestConstants() {
    }
}
