package com.zelenskaya.nestserava.app.controllers;

import com.zelenskaya.nestserava.app.service.IsActiveSessionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
        value = "${mapping.version}/session",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
@AllArgsConstructor
public class SessionController {
    private final IsActiveSessionService isActiveSessionService;

    @PostMapping
    public ResponseEntity<Object> authenticateUser(@RequestBody String jwtToken) {
        boolean isActiveSession = isActiveSessionService.isActive(jwtToken);
        if (isActiveSession) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
