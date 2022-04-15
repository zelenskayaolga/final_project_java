package com.zelenskaya.nestserava.app.service.model;

import org.springframework.stereotype.Component;

@Component
public class ServiceConstants {
    static final String APPLICATION_CONV_ID = "ApplicationConvId";
    static final String ID = "ID";
    static final String PATTERN_FOR_UUID = "^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$";
    static final String NAME_LEGAL = "NameLegal";
    static final String FULL_NAME_INDIVIDUAL = "Full_Name_Individual";
    static final String VALUE_LEG = "Value_Leg";
    static final String VALUE_IND = "Value_Ind";
    static final String STATUS = "Status";
    static final String PERCENT_CONV = "Percent_Conv";
    static final int MAX_SIZE_NAME_EMPLOYEE = 255;
    static final int MAX_SIZE_NAME_LEGAL = 100;
    static final int MAX_SIZE_UUID = 36;
    static final String CUSTOMIZES_PAGE = "customizes_page";
    static final String USER = "User";
    static final String MESSAGE_NOT_NULL_FOR_NAME_LEGAL = "Поле NameLegal является обязательным";
    static final int MAX_SIZE_FOR_UUID = 36;
    static final String MESSAGE_NOT_BLANK_FOR_UUID = "Поле ApplicationConvId является обязательным";
    static final String MESSAGE_PATTERN_FOR_UUID = "Введите ApplicationConvId в требуемом формате";

    private ServiceConstants() {
    }
}
