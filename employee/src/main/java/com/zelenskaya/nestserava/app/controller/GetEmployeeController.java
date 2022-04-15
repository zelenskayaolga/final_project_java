package com.zelenskaya.nestserava.app.controller;

import com.zelenskaya.nestserava.app.service.SearchEmployeeService;
import com.zelenskaya.nestserava.app.service.SelectEmployeeByIdService;
import com.zelenskaya.nestserava.app.service.exception.ServiceEmployeeException;
import com.zelenskaya.nestserava.app.service.model.AddEmployeeDTO;
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

import java.util.Collections;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(
        value = "${mapping.version}/employees",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class GetEmployeeController {
    private final EmployeeControllerConstants constants;
    private final SearchEmployeeService searchService;
    private final SelectEmployeeByIdService selectEmployeeService;

    @GetMapping(value = "/{employeeId}")
    public ResponseEntity<Object> getById(@PathVariable Long employeeId) {
        AddEmployeeDTO employeeById = selectEmployeeService.getById(employeeId);
        return getResponseEntityOk(Collections.singletonList(employeeById));
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
                List<AddEmployeeDTO> employees = searchService.get(searchDTO, paginationDTO);
                if (!employees.isEmpty()) {
                    return getResponseEntityOk(employees);
                } else {
                    return getResponseEntityNotFound();
                }
            } catch (ServiceEmployeeException e) {
                return getResponseEntityBadRequest();
            } catch (RuntimeException e) {
                return getResponseEntityServerError();
            }
        }
    }

    private ResponseEntity<Object> getResponseEntityBadRequest() {
        return getResponseEntity(constants.getMessage400(), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> getResponseEntityNotFound() {
        return getResponseEntity(constants.getMessage404NotFound(), HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<Object> getResponseEntityOk(List<AddEmployeeDTO> bodyValue) {
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
}
