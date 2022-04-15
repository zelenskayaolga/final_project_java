package com.zelenskaya.nestserava.app.controller;

import com.zelenskaya.nestserava.app.service.AddEmployeeService;
import com.zelenskaya.nestserava.app.service.exception.ServiceEmployeeException;
import com.zelenskaya.nestserava.app.service.exception.ServiceExceptionConflict;
import com.zelenskaya.nestserava.app.service.model.AddEmployeeDTO;
import com.zelenskaya.nestserava.app.service.model.AddedEmployeeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(
        value = "${mapping.version}/employees",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class AddEmployeeController {
    private static final String MESSAGE_KEY = "message";
    private final AddEmployeeService addEmployeeService;

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody @Validated AddEmployeeDTO addEmployeeDTO,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return getResponseEntityBadRequest();
        }
        try {
            AddedEmployeeDTO addedEmployeeDTO = addEmployeeService.add(addEmployeeDTO);
            return ResponseEntity.ok().body(Map.of("Сотрудник успешно создан", addedEmployeeDTO));
        } catch (ServiceExceptionConflict e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of(MESSAGE_KEY, "Сотрудник существует"));
        } catch (ServiceEmployeeException e) {
            return getResponseEntityBadRequest();
        }
    }

    private ResponseEntity<Object> getResponseEntityBadRequest() {
        return ResponseEntity.badRequest()
                .body(Map.of(MESSAGE_KEY, "Неверно заданы параметры"));
    }
}