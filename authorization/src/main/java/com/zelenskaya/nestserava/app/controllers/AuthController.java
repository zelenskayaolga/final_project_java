package com.zelenskaya.nestserava.app.controllers;

import com.zelenskaya.nestserava.app.controllers.security.util.JwtUtils;
import com.zelenskaya.nestserava.app.service.AddSessionService;
import com.zelenskaya.nestserava.app.service.BlockUserService;
import com.zelenskaya.nestserava.app.service.SelectUserByUsernameService;
import com.zelenskaya.nestserava.app.service.UpdateAttemptsService;
import com.zelenskaya.nestserava.app.service.ValidatorAuthenticationService;
import com.zelenskaya.nestserava.app.service.model.AttemptsDTO;
import com.zelenskaya.nestserava.app.service.model.AuthDTO;
import com.zelenskaya.nestserava.app.service.model.LoginDTO;
import com.zelenskaya.nestserava.app.service.model.UserDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(
        value = "${mapping.version}/auth/login",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
@AllArgsConstructor
@Slf4j
public class AuthController {
    private static final String MESSAGE_KEY = "message";
    private final ValidatorAuthenticationService validatorAuthenticationService;
    private final SelectUserByUsernameService selectUserByUsernameService;
    private final AddSessionService addSessionService;
    private final UpdateAttemptsService updateAttemptsService;
    private final BlockUserService blockUserService;
    private final MessageSource messageSource;
    private final JwtUtils jwtUtils;

    @PostMapping
    public ResponseEntity<Object> authenticateUser(@RequestBody @Validated LoginDTO login,
                                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, List<String>> errorMap = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.groupingBy(FieldError::getField,
                            Collectors.mapping(e -> messageSource.getMessage(e, LocaleContextHolder.getLocale()),
                                    Collectors.toList())));
            log.error("Invalid data:" + errorMap);
            return ResponseEntity.badRequest().body(Map.of(MESSAGE_KEY,
                    "Логин или пароль введены не верно"));
        }
        String username = login.getUsername();
        boolean isValidByUsername = validatorAuthenticationService.isValidUsername(
                username,
                login.getPassword()
        );
        if (isValidByUsername) {
            UserDTO userByUsername = selectUserByUsernameService.findByUsername(username);
            String jwt = jwtUtils.generateJwtToken(username, userByUsername.getId());
            updateAttemptsService.updateToZero(username);
            AuthDTO addedSession = addSessionService.addWithUsername(jwt, username);
            return ResponseEntity.ok().body(Map.of("sessionId", addedSession));
        }
        AttemptsDTO attemptsByUsername = updateAttemptsService.updateByUsername(username);
        if (attemptsByUsername != null) {
            log.error("Number of invalid attempts is: " + attemptsByUsername.getQuantity());
            if (attemptsByUsername.getQuantity() >= 5) {
                UserDTO userDTO = blockUserService.blockByUsername(username);
                if (userDTO != null) {
                    log.debug("The user with id " + userDTO.getId() + " is blocked");
                }
                return ResponseEntity.badRequest().body(Map.of(MESSAGE_KEY,
                        "Ваша учетная запись заблокирована. Обратитесь к Администратору"));
            }
        }
        return ResponseEntity.badRequest().body(Map.of(MESSAGE_KEY,
                "Логин или пароль введены не верно"));
    }
}
