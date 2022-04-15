package com.zelenskaya.nestserava.app.service.validator;

import com.zelenskaya.nestserava.app.service.ValidatorUserMailService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    private final ValidatorUserMailService validatorUsermailService;

    public UniqueEmailValidator(ValidatorUserMailService validatorUsermailService) {
        this.validatorUsermailService = validatorUsermailService;
    }

    @Override
    public boolean isValid(String usermail, ConstraintValidatorContext context) {
        return validatorUsermailService.isUniqueUserMail(usermail);
    }
}
