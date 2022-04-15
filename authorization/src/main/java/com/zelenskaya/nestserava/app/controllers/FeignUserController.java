package com.zelenskaya.nestserava.app.controllers;

import com.zelenskaya.nestserava.app.service.SelectUserByIdService;
import com.zelenskaya.nestserava.app.service.model.UserDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
        value = "${mapping.version}/user",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
@AllArgsConstructor
@Slf4j
public class FeignUserController {
    private final SelectUserByIdService selectUserByIdService;

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable Long userId) {
        UserDTO userById = selectUserByIdService.findById(userId);
        if (userById != null) {
            return ResponseEntity.ok().body(userById);
        }
        return ResponseEntity.badRequest().body("Сотрудник банка с указанным id не найден");
    }
}
