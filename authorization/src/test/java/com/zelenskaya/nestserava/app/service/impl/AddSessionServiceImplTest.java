package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.repository.SessionRepository;
import com.zelenskaya.nestserava.app.repository.StatusRepository;
import com.zelenskaya.nestserava.app.repository.TimeoutRepository;
import com.zelenskaya.nestserava.app.repository.UserRepository;
import com.zelenskaya.nestserava.app.repository.model.*;
import com.zelenskaya.nestserava.app.service.LocalDateTimeService;
import com.zelenskaya.nestserava.app.service.config.AuthServiceConfig;
import com.zelenskaya.nestserava.app.service.model.AuthDTO;
import com.zelenskaya.nestserava.app.service.model.StatusEnumDTO;
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
class AddSessionServiceImplTest {

    @InjectMocks
    private AddSessionServiceImpl addSessionService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private StatusRepository statusRepository;
    @Mock
    private TimeoutRepository timeoutRepository;
    @Mock
    private LocalDateTimeService localDateTimeService;
    @Mock
    private AuthServiceConfig serviceConfig;

    @Test
    void shouldReturnAuthDTOWhenValidInput() {
        String statusString = StatusEnumDTO.ACTIVE.name();
        String jwt = "JWT";

        Role role = new Role();
        role.setId(RIGHT_ID);
        role.setRole(RoleEnum.ROLE_EMPLOYEE);

        Status status = new Status();
        status.setId(RIGHT_ID);
        status.setStatus(StatusEnum.ACTIVE);

        Attempts attempts = new Attempts();
        attempts.setCount(RIGHT_COUNT_OF_ATTEMPTS);

        LocalDateTime localDateTime = LocalDateTime.now();
        UserDetails userDetails = new UserDetails();
        userDetails.setCreationDate(localDateTime);

        Session session = new Session();
        session.setJwtToken(jwt);
        session.setCreationDate(localDateTime);
        session.setStatus(status);

        User user = new User();
        user.setLogin(RIGHT_USERNAME);
        user.setPassword(ENCODE_PASSWORD);
        user.setEMail(RIGHT_USERMAIL);
        user.setFirstName(RIGHT_FIRST_NAME);
        user.setStatus(status);
        user.setRole(role);
        user.setAttempts(attempts);
        user.setUserDetails(userDetails);

        AuthDTO authDTO = new AuthDTO();
        authDTO.setSessionId(jwt);

        Timeout timeout = new Timeout();
        timeout.setId(1L);
        timeout.setPeriod(1);

        when(userRepository.getByUsername(user.getLogin())).thenReturn(Optional.of(user));
        when(timeoutRepository.findByPeriod(serviceConfig.getJwtExpirationHours())).thenReturn(Optional.of(timeout));
        when(localDateTimeService.dateTimeWithZone(serviceConfig.getZoneTime())).thenReturn(localDateTime);
        when(statusRepository.findByStatus(StatusEnum.valueOf(statusString))).thenReturn(Optional.of(status));
        AuthDTO auth = addSessionService.addWithUsername(jwt, RIGHT_USERNAME);
        Assertions.assertEquals(authDTO.getSessionId(), auth.getSessionId());
    }
}
