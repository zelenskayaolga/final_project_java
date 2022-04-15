package com.zelenskaya.nestserava.app.service.model;

import org.springframework.stereotype.Component;

@Component
public class ServiceConstants {

    static final String PATTERN_FOR_IBAN = "^([B][Y])([0-9]{2})([U][N][B][S])([A-Z0-9]{20})$";
    static final int MAX_NAME_LEGAL_LENGTH = 255;
    static final int MIN_UNP_VALUE = 100000000;
    static final int MAX_UNP_VALUE = 799999999;
    static final int MAX_TOTAL_EMPLOYEES_VALUE = 1000;
    static final int MIN_SEARCH_LEGAL_LENGTH = 3;
    static final int MAX_UNP_LEGAL_LENGTH = 9;
    static final int MAX_IBAN_LEGAL_LENGTH = 28;
    static final String LEGAL_ID = "LegalId";
    static final String NAME_LEGAL = "Name_Legal";
    static final String UNP = "UNP";
    static final String IBANBY_BYN = "IBANbyBYN";
    static final String TYPE_LEGAL = "Type_Legal";
    static final String TOTAL_EMPLOYEES = "Total_Employees";
    static final String CUSTOMIZED_PAGE = "customized_page";
    static final String MESSAGE_NOT_BLANC_NAME_LEGAL = "Поле 'Name_Legal' является обязательным";
    static final String MESSAGE_MAX_SIZE_NAME_LEGAL = "Превышено допустимое количество введенных символов в поле " +
            "'Name_Legal'. Пожалуйста, введите значение до 255 символов";
    static final String MESSAGE_NIT_BLANK_UNP = "Поле 'UNP' является обязательным";
    static final String MESSAGE_MIN_SIZE_UNP = "В поле 'UNP' введено значение меньше 100000000. Пожалуйста, введите " +
            "значениее от 100000000 до 799999999";
    static final String MESSAGE_MAX_SIZE_UNP = "В поле 'UNP' введено значение больше 799999999. Пожалуйста, введите " +
            "значениее от 100000000 до 799999999";
    static final String MESSAGE_REGEXP_IBAN = "Введенные данные в поле 'IBANbyBYN' не соответствуют требованиям";
    static final String MESSAGE_NOT_NULL_TOTAL_EMPLOYEE = "Полее 'Total_Employees' является обязательным";
    static final String MESSAGE_MAX_TOTAL_EMPLOYEE = "Допустимое значение для поля 'Total_Employees' - 1000. " +
            "Введенное значение превышает допустимое";
    static final String MESSAGE_NOT_NULL_IBAN_BYN = "Поле 'IBANbyBYN' является обязательным";
    static final String MESSAGE_NOT_NULL_TYPE_LEGAL = "Поле 'Type_Legal' является обязательным";

    private ServiceConstants() {
    }
}