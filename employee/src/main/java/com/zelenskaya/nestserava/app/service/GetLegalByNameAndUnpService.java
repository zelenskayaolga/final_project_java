package com.zelenskaya.nestserava.app.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "GetLegalByNameAndUnp", url = "${url.service.company.legals}")
public interface GetLegalByNameAndUnpService {
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> getLegalByNameAndUnp(
            @RequestParam(name = "legalName") String legalName,
            @RequestParam(name = "unp") String unp
    );
}