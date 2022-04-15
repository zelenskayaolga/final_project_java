package com.zelenskaya.nestserava.app.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import static com.zelenskaya.nestserava.app.service.model.ServiceModelConstants.CUSTOMIZED_PAG;

@Data
public class PaginationDTO {
    private PaginationEnumDTO pagination;
    private Integer page;
    @JsonProperty(CUSTOMIZED_PAG)
    private Integer customizedPage;
}