package com.zelenskaya.nestserava.app.controllers;

import com.zelenskaya.nestserava.app.service.CloseSessionService;
import com.zelenskaya.nestserava.app.service.model.LogoutDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(
        value = "${mapping.version}/auth/logout",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class LogoutController {
    private static final String MESSAGE_KEY = "message";
    private final CloseSessionService closeSessionService;

    @PostMapping
    public ResponseEntity<Object> closeSession(@RequestBody LogoutDTO logoutDTO) {
        boolean isClosedSession = closeSessionService.close(logoutDTO);
        if (isClosedSession) {
            return ResponseEntity.ok().body(Map.of(MESSAGE_KEY,
                    "Сессия с указанным sessionId закрыта"));
        } else {
            return ResponseEntity.ok().body(Map.of(MESSAGE_KEY,
                    "Сессия с указанным sessionId не активна. Все активные сессии закрыты"));
        }
    }
}
