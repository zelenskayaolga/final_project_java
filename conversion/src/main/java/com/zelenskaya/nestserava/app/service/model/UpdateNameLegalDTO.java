package com.zelenskaya.nestserava.app.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static com.zelenskaya.nestserava.app.service.model.ServiceConstants.*;

@Getter
@Setter
@EqualsAndHashCode
public class UpdateNameLegalDTO {
    @JsonProperty(NAME_LEGAL)
    @Size(max = MAX_SIZE_NAME_LEGAL)
    @NotBlank(message = MESSAGE_NOT_NULL_FOR_NAME_LEGAL)
    private String nameLegal;
    @JsonProperty(APPLICATION_CONV_ID)
    @Size(max = MAX_SIZE_FOR_UUID)
    @NotBlank(message = MESSAGE_NOT_BLANK_FOR_UUID)
    @Pattern(regexp = PATTERN_FOR_UUID,
            message = MESSAGE_PATTERN_FOR_UUID)
    private String applicationConvId;
}
