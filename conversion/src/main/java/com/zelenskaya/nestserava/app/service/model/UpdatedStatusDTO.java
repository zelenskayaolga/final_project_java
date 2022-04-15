package com.zelenskaya.nestserava.app.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import static com.zelenskaya.nestserava.app.service.model.ServiceConstants.*;

@Getter
@Setter
@EqualsAndHashCode
public class UpdatedStatusDTO {
    @JsonProperty(STATUS)
    private StatusEnumDTO status;
    @JsonProperty(USER)
    private String username;
}
