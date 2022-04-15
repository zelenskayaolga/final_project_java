package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.repository.SessionRepository;
import com.zelenskaya.nestserava.app.repository.model.Session;
import com.zelenskaya.nestserava.app.repository.model.Status;
import com.zelenskaya.nestserava.app.repository.model.StatusEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.zelenskaya.nestserava.app.service.impl.AuthorizationServiceTestConstants.RIGHT_ID;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IsActiveSessionServiceImplTest {

    @InjectMocks
    private IsActiveSessionServiceImpl isActiveSessionService;

    @Mock
    private SessionRepository sessionRepository;

    @Test
    void shouldReturnTrueWhenSessionIsActive() {
        Status status = new Status();
        status.setId(RIGHT_ID);
        status.setStatus(StatusEnum.ACTIVE);

        Session session = new Session();
        session.setJwtToken("JWT");
        session.setStatus(status);

        when(sessionRepository.findByJwt("JWT")).thenReturn(Optional.of(session));
        boolean isActive = isActiveSessionService.isActive("JWT");
        Assertions.assertTrue(isActive);
    }

    @Test
    void shouldReturnFalseWhenSessionIsNotActive() {
        Status status = new Status();
        status.setId(RIGHT_ID);
        status.setStatus(StatusEnum.DISABLED);

        Session session = new Session();
        session.setJwtToken("JWT");
        session.setStatus(status);

        when(sessionRepository.findByJwt("JWT")).thenReturn(Optional.of(session));
        boolean isActive = isActiveSessionService.isActive("JWT");
        Assertions.assertFalse(isActive);
    }
}