package com.zelenskaya.nestserava.app.service.model;

import lombok.Data;

import javax.validation.constraints.Size;

import static com.zelenskaya.nestserava.app.service.model.ServiceConstants.MAX_IBAN_LEGAL_LENGTH;
import static com.zelenskaya.nestserava.app.service.model.ServiceConstants.MAX_NAME_LEGAL_LENGTH;
import static com.zelenskaya.nestserava.app.service.model.ServiceConstants.MAX_UNP_LEGAL_LENGTH;
import static com.zelenskaya.nestserava.app.service.model.ServiceConstants.MIN_SEARCH_LEGAL_LENGTH;

@Data
public class SearchDTO {
    @Size(min = MIN_SEARCH_LEGAL_LENGTH, max = MAX_NAME_LEGAL_LENGTH)
    private String legalName;
    @Size(min = MIN_SEARCH_LEGAL_LENGTH, max = MAX_UNP_LEGAL_LENGTH)
    private String unp;
    @Size(min = MIN_SEARCH_LEGAL_LENGTH, max = MAX_IBAN_LEGAL_LENGTH)
    private String ibanByByn;
}