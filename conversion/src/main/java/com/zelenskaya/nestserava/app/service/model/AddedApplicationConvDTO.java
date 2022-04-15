package com.zelenskaya.nestserava.app.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import static com.zelenskaya.nestserava.app.service.model.ServiceConstants.APPLICATION_CONV_ID;

@Getter
@Setter
@EqualsAndHashCode
public class AddedApplicationConvDTO {
    @JsonProperty(APPLICATION_CONV_ID)
    public String applicationConvId;
}
