package com.zelenskaya.nestserava.app.service.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

import static com.zelenskaya.nestserava.app.service.model.ServiceModelConstants.*;

@Getter
@Setter
@EqualsAndHashCode
public class AddEmployeeDTO {
    @JsonProperty(EMPLOYEE_ID)
    private Long id;
    @JsonProperty(FULL_NAME_INDIVIDUAL)
    @NotBlank(message = MESSAGE_NOT_BLANK_NAME)
    private String name;
    @JsonProperty(RECRUITMENT_DATE)
    @JsonFormat(pattern = PATTERN_DATE)
    @NotNull(message = MESSAGE_NOT_NULL_RECRUITMENT_DATE)
    private LocalDate recruitmentDate;
    @JsonProperty(TERMINATION_DATE)
    @JsonFormat(pattern = PATTERN_DATE)
    @NotNull(message = MESSAGE_NOT_NULL_TERMINATION_DATE)
    private LocalDate terminationDate;
    @JsonProperty(NAME_LEGAL)
    @NotBlank(message = MESSAGE_NOT_BLANK_NAME_LEGAL)
    private String nameLegal;
    @JsonProperty(PERSON_IBAN_BYN)
    @Pattern(regexp = REGEXP_FOR_BYN,
            message = MESSAGE_PATTERN_IBAN_BY_BYN)
    @NotNull(message = MESSAGE_NOT_NULL_IBAN_BYN)
    private String ibanByn;
    @JsonProperty(PERSON_IBAN_CURRENCY)
    @Pattern(regexp = REGEXP_FOR_CURRENCY,
            message = MESSAGE_PATTERN_IBAN_BY_CURRENCY)
    @NotNull(message = MESSAGE_NOT_NULL_IBAN_CURRENCY)
    private String ibanCurrency;
    @JsonIgnore
    private PositionEnumDTO position;
}
