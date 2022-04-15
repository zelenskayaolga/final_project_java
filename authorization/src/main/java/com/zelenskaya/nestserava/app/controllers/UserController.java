package com.zelenskaya.nestserava.app.controllers;

import com.zelenskaya.nestserava.app.service.AddUserService;
import com.zelenskaya.nestserava.app.service.model.AddedUserDTO;
import com.zelenskaya.nestserava.app.service.model.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping(
        value = "${mapping.version}/auth/signin",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class UserController {
    private final AddUserService addUserService;

    @PostMapping
    public ResponseEntity<Object> addUser(@RequestBody @Validated UserDTO userDTO) {
        AddedUserDTO addedUser = addUserService.add(userDTO);
        if (addedUser != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("Создан новый пользователь", addedUser));
        }
        return new ResponseEntity<>((Map.of("message", "Новый пользователь не создан")), HttpStatus.BAD_REQUEST);
    }
}
