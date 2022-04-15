package com.zelenskaya.nestserava.app.service.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddedUserDTO {
    private Long userId;
    private StatusEnumDTO status;
}
