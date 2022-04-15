package com.zelenskaya.nestserava.app.controller;

import com.zelenskaya.nestserava.app.service.GetApplicationConvService;
import com.zelenskaya.nestserava.app.service.exceptions.ServiceConversionException;
import com.zelenskaya.nestserava.app.service.model.ApplicationConvDTO;
import com.zelenskaya.nestserava.app.service.model.PaginationDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;
import java.util.Collections;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "${mapping.version}/applications",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
)
public class GetApplicationConvController {
    private final ConversionControllerConstants constants;
    private final GetApplicationConvService getApplicationConvService;

    @GetMapping
    public ResponseEntity<Object> get(PaginationDTO paginationDTO, BindingResult result) {
        if (result.hasErrors()) {
            result.getErrorCount();
            return getResponseEntityBadRequest();
        } else {
            try {
                List<ApplicationConvDTO> applicationConvs = getApplicationConvService.get(paginationDTO);
                if (!applicationConvs.isEmpty()) {
                    return getResponseEntityOk(applicationConvs);
                } else {
                    return getResponseEntityNotFound();
                }
            } catch (ServiceConversionException e) {
                return getResponseEntityBadRequest();
            } catch (RuntimeException e) {
                return getResponseEntityServerError();
            }
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getById(@PathVariable @Positive Long id) {
        try {
            ApplicationConvDTO applicationConvDTO = getApplicationConvService.getById(id);
            if (applicationConvDTO != null) {
                return getResponseEntityOk(Collections.singletonList(applicationConvDTO));
            } else {
                return getResponseEntityNotExist();
            }
        } catch (RuntimeException e) {
            return getResponseEntityServerError();
        }
    }

    private ResponseEntity<Object> getResponseEntityBadRequest() {
        return getResponseEntity(constants.getMessage400(), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> getResponseEntityNotFound() {
        return getResponseEntity(constants.getMessage404NotFound(), HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<Object> getResponseEntityOk(List<ApplicationConvDTO> bodyValue) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(bodyValue);
    }

    private ResponseEntity<Object> getResponseEntityServerError() {
        return getResponseEntity(constants.getMessage500(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Object> getResponseEntity(String bodyValue, HttpStatus httpStatus) {
        return ResponseEntity.status(httpStatus)
                .body(Collections.singletonMap(constants.getMessageKey(), bodyValue));
    }

    private ResponseEntity<Object> getResponseEntityNotExist() {
        return getResponseEntity(constants.getMessage404NotExist(), HttpStatus.NOT_FOUND);
    }
}