package com.zelenskaya.nestserava.app.repository.model;

import lombok.Data;

import java.util.List;

@Data
public class SearchEmployee {
    private List<Long> legalId;
    private String fullNameIndividual;
}