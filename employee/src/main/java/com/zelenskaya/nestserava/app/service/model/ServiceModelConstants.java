package com.zelenskaya.nestserava.app.service.model;

public class ServiceModelConstants {
    static final String EMPLOYEE_ID = "EmployeeId";
    static final String FULL_NAME_INDIVIDUAL = "Full_Name_Individual";
    static final String RECRUITMENT_DATE = "Recruitment_date";
    static final String TERMINATION_DATE = "Termination_date";
    static final String NAME_LEGAL = "Name_Legal";
    static final String PERSON_IBAN_BYN = "Person_Iban_Byn";
    static final String PERSON_IBAN_CURRENCY = "Person_Iban_Currency";
    static final String REGEXP_FOR_BYN = "^([B][Y])([0-9]{2})([U][N][B][S])([A-Z0-9]{20})$";
    static final String REGEXP_FOR_CURRENCY = "^([A-Z]{2})([0-9]{2})([U][N][B][S])([A-Z0-9]{20})$";
    static final String PATTERN_DATE = "dd/MM/yyyy";
    static final String UNP = "UNP";
    static final String CUSTOMIZED_PAG = "customized_pag";
    static final String MESSAGE_NOT_BLANK_NAME = "Поле 'Full_Name_Individual' является обязательным";
    static final String MESSAGE_NOT_BLANK_NAME_LEGAL = "Поле 'Name_Legal' является обязательным";
    static final String MESSAGE_PATTERN_IBAN_BY_BYN = "Введенные данные в поле 'IBANbyBYN' не соответствуют требованиям";
    static final String MESSAGE_PATTERN_IBAN_BY_CURRENCY = "Введенные данные в поле 'IBANbyCurrency' не соответствуют требованиям";
    static final String MESSAGE_NOT_NULL_TERMINATION_DATE = "Поле 'Termination_date' является обязательным";
    static final String MESSAGE_NOT_NULL_RECRUITMENT_DATE = "Поле 'Recruitment_date' является обязательным";
    static final String MESSAGE_NOT_NULL_IBAN_BYN = "Поле 'Person_Iban_Byn' является обязательным";
    static final String MESSAGE_NOT_NULL_IBAN_CURRENCY = "Поле 'Person_Iban_Currency' является обязательным.";

    private ServiceModelConstants() {
    }
}
