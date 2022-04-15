package com.zelenskaya.nestserava.app.controllers;

import com.zelenskaya.nestserava.app.service.AddLegalService;
import com.zelenskaya.nestserava.app.service.exceptions.ServiceLegalException;
import com.zelenskaya.nestserava.app.service.model.LegalDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@AllArgsConstructor
@RequestMapping(
        value = "${mapping.version}/legals",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class AddLegalController {
    private final AddLegalService addLegalService;
    private final LegalControllerConstants constants;

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody @Validated LegalDTO legalDTO,
                                      BindingResult result) {
        if (result.hasErrors()) {
            return getResponseEntityBadRequest();
        } else {
            try {
                addLegalService.add(legalDTO);
                return getResponseEntity(constants.getMessage201(), HttpStatus.CREATED);
            } catch (ServiceLegalException e) {
                return getResponseEntity(getMessage409WithParameters(legalDTO), HttpStatus.CONFLICT);
            } catch (RuntimeException e) {
                return getResponseEntityServerError();
            }
        }
    }

    private ResponseEntity<Object> getResponseEntityBadRequest() {
        return getResponseEntity(constants.getMessage400(), HttpStatus.BAD_REQUEST);
    }

    private String getMessage409WithParameters(LegalDTO legalDTO) {
        return constants.getMessage409() +
                legalDTO.getNameLegal() +
                constants.getMessage409Separator() +
                legalDTO.getUnp() +
                constants.getMessage409Separator() +
                legalDTO.getIban();
    }

    private ResponseEntity<Object> getResponseEntityServerError() {
        return getResponseEntity(constants.getMessage500(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Object> getResponseEntity(String bodyValue, HttpStatus httpStatus) {
        return ResponseEntity.status(httpStatus)
                .body(Collections.singletonMap(constants.getMessageKey(), bodyValue));
    }
}