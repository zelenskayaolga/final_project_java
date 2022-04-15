package com.zelenskaya.nestserava.app.service.model;

import com.zelenskaya.nestserava.app.service.validator.UniqueEmail;
import com.zelenskaya.nestserava.app.service.validator.UniqueUsername;
import com.zelenskaya.nestserava.app.service.validator.ValidPassword;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static com.zelenskaya.nestserava.app.service.model.ServiceConstant.*;

@Getter
@Setter
@EqualsAndHashCode
public class UserDTO {
    private Long id;
    @Pattern(regexp = PATTERN_FOR_USERNAME, message = MESSAGE_PATTERN_FOR_USERNAME)
    @NotNull(message = MESSAGE_NOT_NULL_FOR_USERNAME)
    @UniqueUsername(message = MESSAGE_IS_UNIQUE_FOR_USERNAME)
    @Size(min = MIN_LOGIN_SIZE, max = MAX_LOGIN_SIZE, message = MESSAGE_SIZE_FOR_USERNAME)
    private String username;
    @ValidPassword(message = MESSAGE_FOR_VALID_PASSWORD)
    @NotNull(message = MESSAGE_NOT_NULL_FOR_PASSWORD)
    private String password;
    @UniqueEmail(message = MESSAGE_UNIQUE_EMAIL)
    @Pattern(regexp = PATTERN_FOR_USERMAIL, message = MESSAGE_PATTERN_FOR_EMAIL)
    @NotNull(message = MESSAGE_NOT_NULL_FOR_EMAIL)
    @Size(max = MAX_USERMAIL_LENGTH, message = MESSAGE_SIZE_FOR_EMAIL)
    private String usermail;
    @Pattern(regexp = PATTERN_FOR_FIRSTNAME, message = MESSAGE_PATTERN_FOR_FIRST_NAME)
    @NotNull(message = MESSAGE_NOT_NULL_FIRST_NAME)
    private String firstName;
    private RoleEnumDTO role;
}
