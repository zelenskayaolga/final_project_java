package com.zelenskaya.nestserava.app.controllers;

import com.zelenskaya.nestserava.app.service.SelectLegalService;
import com.zelenskaya.nestserava.app.service.exceptions.ServiceLegalException;
import com.zelenskaya.nestserava.app.service.model.LegalDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(value = "${mapping.version}/legal",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class FeignLegalController {
    private final SelectLegalService selectLegalService;

    @GetMapping
    public ResponseEntity<Object> getByNameLegal(@RequestParam("Name_Legal") String nameLegal) {
        LegalDTO legalByName;
        try {
            legalByName = selectLegalService.getByName(nameLegal);
            return ResponseEntity.ok(legalByName);
        } catch (ServiceLegalException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
