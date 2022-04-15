package com.zelenskaya.nestserava.app.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "GetLegalByName", url = "${url.service.company.legal}")
public interface GetLegalService {
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> getLegalByName(@RequestParam("Name_Legal") String nameLegal);
}