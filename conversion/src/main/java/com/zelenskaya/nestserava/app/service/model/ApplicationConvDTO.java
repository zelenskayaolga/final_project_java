package com.zelenskaya.nestserava.app.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.UUID;

import static com.zelenskaya.nestserava.app.service.model.ServiceConstants.*;

@Getter
@Setter
@EqualsAndHashCode
public class ApplicationConvDTO {
    @JsonProperty(ID)
    private Long id;
    @JsonProperty(APPLICATION_CONV_ID)
    @Size(max = MAX_SIZE_UUID)
    @Pattern(regexp = PATTERN_FOR_UUID)
    private UUID applicationConvId;
    @JsonProperty(NAME_LEGAL)
    @Size(max = MAX_SIZE_NAME_LEGAL)
    private String nameLegal;
    @JsonProperty(FULL_NAME_INDIVIDUAL)
    @Size(max = MAX_SIZE_NAME_EMPLOYEE)
    private String nameEmployee;
    @JsonProperty(VALUE_LEG)
    private ValueLegEnumDTO valueLeg;
    @JsonProperty(VALUE_IND)
    private ValueIndEnumDTO valueInd;
    @JsonProperty(STATUS)
    private StatusEnumDTO status;
    @JsonProperty(PERCENT_CONV)
    private Float percent;
}
