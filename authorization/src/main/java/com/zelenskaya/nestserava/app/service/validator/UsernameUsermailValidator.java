package com.zelenskaya.nestserava.app.service.validator;

import com.zelenskaya.nestserava.app.service.SelectUserByUsernameService;
import com.zelenskaya.nestserava.app.service.model.UserDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UsernameUsermailValidator implements ConstraintValidator<IsUsername, String> {
    private final SelectUserByUsernameService selectUserByUsernameService;

    public UsernameUsermailValidator(SelectUserByUsernameService selectUserByUsernameService) {
        this.selectUserByUsernameService = selectUserByUsernameService;
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        UserDTO user = selectUserByUsernameService.findByUsername(username);
        return user != null;
    }
}
