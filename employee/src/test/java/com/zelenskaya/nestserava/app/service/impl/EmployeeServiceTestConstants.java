package com.zelenskaya.nestserava.app.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EmployeeServiceTestConstants {

    static final Long RIGHT_ID = 1L;
    static final String RIGHT_NAME_EMPLOYEE = "Александр";
    static final String RIGHT_IBAN_BYN = "BY00UNBS00000000000000000000";
    static final String RIGHT_IBAN_CURRENCY = "LT00UNBS00000000000000000000";
    static final String RIGHT_NAME_LEGAL = "Company";
    static final LocalDate RIGHT_RECRUITMENT_DATE = LocalDate.now();
    static final LocalDate RIGHT_TERMINATION_DATE = LocalDate.parse(
            "12/12/2022", DateTimeFormatter.ofPattern("dd/MM/yyyy")
    );
    static final int RIGHT_UNP_LEGAL = 100000000;
    static final String TIME_ZONE = "Europe/Minsk";
    static final String FULL_NAME_INDIVIDUAL = "Alexandr";

    private EmployeeServiceTestConstants() {
    }
}