package com.zelenskaya.nestserava.app.service.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UsernameUsermailValidator.class)
public @interface IsUsername {
    String message() default "No users with username  were found";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
