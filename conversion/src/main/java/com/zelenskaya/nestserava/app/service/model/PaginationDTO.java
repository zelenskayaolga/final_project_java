package com.zelenskaya.nestserava.app.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import static com.zelenskaya.nestserava.app.service.model.ServiceConstants.CUSTOMIZES_PAGE;

@Data
public class PaginationDTO {
    private PaginationEnumDTO pagination;
    private Integer page;
    @JsonProperty(CUSTOMIZES_PAGE)
    private Integer customizedPage;
}