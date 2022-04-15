package com.zelenskaya.nestserava.app.controllers;

import com.zelenskaya.nestserava.app.service.SearchLegalService;
import com.zelenskaya.nestserava.app.service.SelectLegalService;
import com.zelenskaya.nestserava.app.service.exceptions.ServiceLegalException;
import com.zelenskaya.nestserava.app.service.model.LegalDTO;
import com.zelenskaya.nestserava.app.service.model.PaginationDTO;
import com.zelenskaya.nestserava.app.service.model.SearchDTO;
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
@RequestMapping(
        value = "${mapping.version}/legals",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class GetLegalController {
    private final LegalControllerConstants constants;
    private final SelectLegalService selectLegalService;
    private final SearchLegalService searchService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getById(@PathVariable @Positive Long id) {
        try {
            LegalDTO legalById = selectLegalService.getById(id);
            if (legalById != null) {
                return getResponseEntityOk(Collections.singletonList(legalById));
            } else {
                return getResponseEntityNotExist();
            }
        } catch (RuntimeException e) {
            return getResponseEntityServerError();
        }
    }

    @GetMapping
    public ResponseEntity<Object> getLegalsSearch(
            SearchDTO searchDTO,
            PaginationDTO paginationDTO,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            result.getErrorCount();
            return getResponseEntityBadRequest();
        } else {
            try {
                List<LegalDTO> legals = searchService.get(searchDTO, paginationDTO);
                if (!legals.isEmpty()) {
                    return getResponseEntityOk(legals);
                } else {
                    return getResponseEntityNotFound();
                }
            } catch (ServiceLegalException e) {
                return getResponseEntityBadRequest();
            } catch (RuntimeException e) {
                return getResponseEntityServerError();
            }
        }
    }

    private ResponseEntity<Object> getResponseEntityOk(List<LegalDTO> bodyValue) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(bodyValue);
    }

    private ResponseEntity<Object> getResponseEntityBadRequest() {
        return getResponseEntity(constants.getMessage400(), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> getResponseEntityNotExist() {
        return getResponseEntity(constants.getMessage404NotExist(), HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<Object> getResponseEntityNotFound() {
        return getResponseEntity(constants.getMessage404NotFound(), HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<Object> getResponseEntityServerError() {
        return getResponseEntity(constants.getMessage500(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Object> getResponseEntity(String bodyValue, HttpStatus httpStatus) {
        return ResponseEntity.status(httpStatus)
                .body(Collections.singletonMap(constants.getMessageKey(), bodyValue));
    }
}
