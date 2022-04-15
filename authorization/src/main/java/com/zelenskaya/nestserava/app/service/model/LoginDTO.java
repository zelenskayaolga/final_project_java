package com.zelenskaya.nestserava.app.service.model;

import com.zelenskaya.nestserava.app.service.validator.IsUsername;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static com.zelenskaya.nestserava.app.service.model.ServiceConstant.*;

@Getter
@Setter
@EqualsAndHashCode
public class LoginDTO {
    @NotNull(message = MESSAGE_NOT_BLANK)
    @Size(max = MAX_USERMAIL_LENGTH, message = MESSAGE_MAX_SIZE_USERNAME)
    @IsUsername(message = MESSAGE_IS_USERNAME)
    private String username;
    private String password;
}
