package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.repository.SessionRepository;
import com.zelenskaya.nestserava.app.repository.model.*;
import com.zelenskaya.nestserava.app.service.config.AuthServiceConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.zelenskaya.nestserava.app.service.impl.AuthorizationServiceTestConstants.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidateSessionServiceImplTest {

    @InjectMocks
    private ValidateSessionServiceImpl validateSessionService;

    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private UpdateDateTimeStatusServiceImpl updateDateService;
    @Mock
    private AuthServiceConfig serviceConfig;

    @Test
    void shouldReturnTrueWhenSessionIsActive() {
        String jwt = "JWT";

        Status status = new Status();
        status.setId(RIGHT_ID);
        status.setStatus(StatusEnum.ACTIVE);

        LocalDateTime localDateTime = LocalDateTime.now();
        UserDetails userDetails = new UserDetails();
        userDetails.setCreationDate(localDateTime);

        User user = new User();
        user.setId(RIGHT_ID);
        user.setLogin("username");
        user.setPassword(ENCODE_PASSWORD);
        user.setEMail(RIGHT_USERMAIL);
        user.setFirstName(RIGHT_FIRST_NAME);
        user.setStatus(status);

        Timeout timeout = new Timeout();
        timeout.setId(RIGHT_ID);
        timeout.setPeriod(1);

        Session session = new Session();
        session.setJwtToken(jwt);
        session.setCreationDate(localDateTime);
        session.setStatus(status);
        session.setUser(user);
        session.setTimeout(timeout);

        when(sessionRepository.findByJwt(jwt)).thenReturn(Optional.of(session));
        when(serviceConfig.getZoneTime()).thenReturn(TIME_ZONE);
        boolean isActive = validateSessionService.isActive(jwt);
        Assertions.assertTrue(isActive);
    }

    @Test
    void shouldReturnFalseWhenSessionIsNotActive() {
        String jwt = "JWT";

        Status status = new Status();
        status.setId(RIGHT_ID);
        status.setStatus(StatusEnum.DISABLED);

        LocalDateTime localDateTime = LocalDateTime.now();
        UserDetails userDetails = new UserDetails();
        userDetails.setCreationDate(localDateTime);

        User user = new User();
        user.setId(RIGHT_ID);
        user.setLogin("username");
        user.setPassword(ENCODE_PASSWORD);
        user.setEMail(RIGHT_USERMAIL);
        user.setFirstName(RIGHT_FIRST_NAME);
        user.setStatus(status);

        Timeout timeout = new Timeout();
        timeout.setId(RIGHT_ID);
        timeout.setPeriod(1);

        Session session = new Session();
        session.setJwtToken(jwt);
        session.setCreationDate(localDateTime);
        session.setStatus(status);
        session.setUser(user);
        session.setTimeout(timeout);

        when(sessionRepository.findByJwt(jwt)).thenReturn(Optional.of(session));
        when(serviceConfig.getZoneTime()).thenReturn(TIME_ZONE);
        boolean isActive = validateSessionService.isActive(jwt);
        Assertions.assertFalse(isActive);
    }
}