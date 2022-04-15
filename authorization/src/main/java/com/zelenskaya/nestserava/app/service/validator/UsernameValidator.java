package com.zelenskaya.nestserava.app.service.validator;

import com.zelenskaya.nestserava.app.service.ValidatorUsernameService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UsernameValidator implements ConstraintValidator<UniqueUsername, String> {
    private final ValidatorUsernameService validatorUsernameService;

    public UsernameValidator(ValidatorUsernameService validatorUsernameService) {
        this.validatorUsernameService = validatorUsernameService;
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        return validatorUsernameService.isUniqueUsername(username);
    }
}
