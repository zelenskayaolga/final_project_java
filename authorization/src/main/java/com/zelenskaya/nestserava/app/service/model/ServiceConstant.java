package com.zelenskaya.nestserava.app.service.model;

import org.springframework.stereotype.Component;

@Component
public class ServiceConstant {
    static final String PATTERN_FOR_USERNAME = "^[a-z]{6,100}$";
    static final String PATTERN_FOR_USERMAIL = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$";
    static final String PATTERN_FOR_FIRSTNAME = "^[аА-яЯ]{1,20}$";
    static final int MIN_LOGIN_SIZE = 6;
    static final int MAX_LOGIN_SIZE = 100;
    static final int MAX_USERMAIL_LENGTH = 100;
    static final String MESSAGE_NOT_BLANK = "Поле является обязательным для заполнения";
    static final String MESSAGE_IS_USERNAME = "Пользователя с таким логином или электронной почтой не найдено";
    static final String MESSAGE_MAX_SIZE_USERNAME = "Поле может иметь размер до 100 символов";
    static final String MESSAGE_PATTERN_FOR_USERNAME = "Логин не соотвествует требованиям. Пожалуйста, введите " +
            "логин в соответствии с требованиями: от 6 до 100 букв латиницы в нижнем регистре";
    static final String MESSAGE_NOT_NULL_FOR_USERNAME = "Поле 'логин' является обязательным.";
    static final String MESSAGE_IS_UNIQUE_FOR_USERNAME = "Пользователь существует. Укажите другой логин";
    static final String MESSAGE_SIZE_FOR_USERNAME = "Электронная почта не соотвествует требованиям. Пожалуйста, введите логин в соответствии с требованиями:" +
            "от 6 до 100 букв латиницы в нижнем регистре";
    static final String MESSAGE_FOR_VALID_PASSWORD = "Пароль не соотвествует требованиям. Пожалуйста, введите пароль в соответствии с " +
            "требованиями: от 8 символов до 20 символов, состоящих из цифр, спец символов и букв в различном регистре";
    static final String MESSAGE_NOT_NULL_FOR_PASSWORD = "Поле 'пароль' является обязательным. Пожалуйста, введите пароль в соответствии с " +
            "требованиями: от 8 символов до 20 символов, состоящих из цифр, спец символов и букв в различном регистре";
    static final String MESSAGE_UNIQUE_EMAIL = "Пользователь с данной электронной почтой уже существует. Укажите другую электронную почту";
    static final String MESSAGE_PATTERN_FOR_EMAIL = "Электронная почта не соотвествует " +
            "требованиям. Пожалуйста, введите электронную почту в соответствии с форматом: allen@example.com (до 100 " +
            "символов)";
    static final String MESSAGE_NOT_NULL_FOR_EMAIL = "Поле 'электронная почта' является обязательным. Пожалуйста, введите электронную почту в " +
            "соответствии с форматом: allen@example.com (до 100 символов)";
    static final String MESSAGE_SIZE_FOR_EMAIL = "Электронная почта не соотвествует требованиям. Пожалуйста, введите " +
            "электронную почту до 100 символов";
    static final String MESSAGE_PATTERN_FOR_FIRST_NAME = "Имя сотрудника не соотвествует требованиям. Пожалуйста, " +
            "введите имя сотрудника на русском языке (до 20 символов)";
    static final String MESSAGE_NOT_NULL_FIRST_NAME = "Поле 'имя сотрудника' является обязательным. Пожалуйста, введите имя сотрудника на русском " +
            "языке (до 20 символов)";

    private ServiceConstant() {
    }
}
