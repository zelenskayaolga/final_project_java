package com.zelenskaya.nestserava.app.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "GetEmployeeById", url = "${url.service.employees}")
public interface GetEmployeeByIdService {
    @GetMapping(
            value = "/{employeeId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<Object> getEmployeeById(@PathVariable Long employeeId);
}