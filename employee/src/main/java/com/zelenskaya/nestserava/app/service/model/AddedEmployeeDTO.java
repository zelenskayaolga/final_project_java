package com.zelenskaya.nestserava.app.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import static com.zelenskaya.nestserava.app.service.model.ServiceModelConstants.EMPLOYEE_ID;

@Getter
@Setter
@EqualsAndHashCode
public class AddedEmployeeDTO {
    @JsonProperty(EMPLOYEE_ID)
    private Long id;
}
