package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.repository.SessionRepository;
import com.zelenskaya.nestserava.app.repository.UserRepository;
import com.zelenskaya.nestserava.app.repository.model.User;
import com.zelenskaya.nestserava.app.service.LocalDateTimeService;
import com.zelenskaya.nestserava.app.service.UpdateDateTimeStatusService;
import com.zelenskaya.nestserava.app.service.ValidateSessionService;
import com.zelenskaya.nestserava.app.service.ValidatorSessionUsernameService;
import com.zelenskaya.nestserava.app.service.config.AuthServiceConfig;
import com.zelenskaya.nestserava.app.service.model.LogoutDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.zelenskaya.nestserava.app.service.impl.AuthorizationServiceTestConstants.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CloseSessionServiceImplTest {

    @InjectMocks
    private CloseSessionServiceImpl closeSessionService;

    @Mock
    private ValidatorSessionUsernameService validatorSessionService;
    @Mock
    private ValidateSessionService validateSessionService;
    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private UpdateDateTimeStatusService updateDateService;
    @Mock
    private LocalDateTimeService localDateTimeService;
    @Mock
    private AuthServiceConfig authServiceConfig;
    @Mock
    private UserRepository userRepository;

    @Test
    void shouldReturnTrue() {
        String accessToken = "JWT";

        LogoutDTO logoutDTO = new LogoutDTO();
        logoutDTO.setUsername(RIGHT_USERNAME);
        logoutDTO.setSessionId(accessToken);

        when(validatorSessionService.isValid(logoutDTO.getUsername(), logoutDTO.getSessionId())).thenReturn(true);
        when(validateSessionService.isActive(logoutDTO.getSessionId())).thenReturn(true);
        when(updateDateService.setExitDestructionDates(authServiceConfig.getZoneTime(), logoutDTO.getSessionId()))
                .thenReturn(true);
        boolean closedSession = closeSessionService.close(logoutDTO);
        Assertions.assertTrue(updateDateService.setExitDestructionDates(
                authServiceConfig.getZoneTime(), logoutDTO.getSessionId()
        ));
    }

    @Test
    void shouldReturnFalse() {
        String accessToken = "JWT";
        String username = "username";

        LogoutDTO logoutDTO = new LogoutDTO();
        logoutDTO.setUsername(username);
        logoutDTO.setSessionId(accessToken);

        User user = new User();
        user.setId(RIGHT_ID);
        user.setLogin(username);
        user.setPassword(ENCODE_PASSWORD);
        user.setEMail(RIGHT_USERMAIL);
        user.setFirstName(RIGHT_FIRST_NAME);

        List<String> tokens = new ArrayList<>();
        String token = "newJWT";
        tokens.add(token);

        when(validatorSessionService.isValid(logoutDTO.getUsername(), logoutDTO.getSessionId())).thenReturn(true);
        when(validateSessionService.isActive(logoutDTO.getSessionId())).thenReturn(false);
        when(userRepository.getByUsername(username)).thenReturn(Optional.of(user));
        when(sessionRepository.getJwtByUserId(user.getId())).thenReturn(tokens);
        when(updateDateService.setExitDestructionDates(authServiceConfig.getZoneTime(), token)).thenReturn(false);
        boolean isCloseSession = closeSessionService.close(logoutDTO);
        Assertions.assertFalse(updateDateService.setExitDestructionDates(authServiceConfig.getZoneTime(), token));
    }
}