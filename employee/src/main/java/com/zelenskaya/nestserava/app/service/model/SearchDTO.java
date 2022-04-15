package com.zelenskaya.nestserava.app.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import static com.zelenskaya.nestserava.app.service.model.ServiceModelConstants.*;

@Data
public class SearchDTO {
    @JsonProperty(NAME_LEGAL)
    private String legalName;
    @JsonProperty(UNP)
    private String unp;
    @JsonProperty(FULL_NAME_INDIVIDUAL)
    private String fullNameIndividual;
}