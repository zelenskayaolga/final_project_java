package com.zelenskaya.nestserava.app.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static com.zelenskaya.nestserava.app.service.model.ServiceConstants.*;

@Getter
@Setter
@EqualsAndHashCode
public class LegalDTO {
    @JsonProperty(LEGAL_ID)
    private Long id;
    @NotBlank(message = MESSAGE_NOT_BLANC_NAME_LEGAL)
    @Size(max = MAX_NAME_LEGAL_LENGTH, message = MESSAGE_MAX_SIZE_NAME_LEGAL)
    @JsonProperty(NAME_LEGAL)
    private String nameLegal;
    @NotNull(message = MESSAGE_NIT_BLANK_UNP)
    @Min(value = MIN_UNP_VALUE, message = MESSAGE_MIN_SIZE_UNP)
    @Max(value = MAX_UNP_VALUE, message = MESSAGE_MAX_SIZE_UNP)
    @JsonProperty(UNP)
    private Integer unp;
    @Pattern(regexp = PATTERN_FOR_IBAN, message = MESSAGE_REGEXP_IBAN)
    @JsonProperty(IBANBY_BYN)
    @NotNull(message = MESSAGE_NOT_NULL_IBAN_BYN)
    private String iban;
    @JsonProperty(TYPE_LEGAL)
    @NotNull(message = MESSAGE_NOT_NULL_TYPE_LEGAL)
    private TypeEnumDTO typeLegal;
    @Max(value = MAX_TOTAL_EMPLOYEES_VALUE, message = MESSAGE_MAX_TOTAL_EMPLOYEE)
    @NotNull(message = MESSAGE_NOT_NULL_TOTAL_EMPLOYEE)
    @JsonProperty(TOTAL_EMPLOYEES)
    private Integer totalEmployees;
}
