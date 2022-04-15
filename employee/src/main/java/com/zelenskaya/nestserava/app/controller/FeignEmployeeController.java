package com.zelenskaya.nestserava.app.controller;

import com.zelenskaya.nestserava.app.service.SelectEmployeeByNameService;
import com.zelenskaya.nestserava.app.service.model.AddEmployeeDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "${mapping.version}/employee",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class FeignEmployeeController {
    private final SelectEmployeeByNameService selectEmployeeByNameService;

    @GetMapping
    public ResponseEntity<Object> getByNameLegal(@RequestParam("Full_Name_Individual") String nameEmployee) {
        List<AddEmployeeDTO> employeesByName = selectEmployeeByNameService.getByFullNameIndividual(nameEmployee);
        if (!employeesByName.isEmpty()) {
            return ResponseEntity.ok(employeesByName);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
