package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.repository.SessionRepository;
import com.zelenskaya.nestserava.app.repository.StatusRepository;
import com.zelenskaya.nestserava.app.repository.UserDetailsRepository;
import com.zelenskaya.nestserava.app.repository.model.*;
import com.zelenskaya.nestserava.app.service.LocalDateTimeService;
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
class UpdateDateTimeStatusServiceImplTest {

    @InjectMocks
    private UpdateDateTimeStatusServiceImpl updateDateTimeStatusService;

    @Mock
    private LocalDateTimeService localDateTimeService;
    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private StatusRepository statusRepository;
    @Mock
    private UserDetailsRepository userDetailsRepository;

    @Test
    void shouldReturnTrueWhenDateTimeStatusWasUpdated() {
        String jwt = "JWT";
        Long userId = 1L;
        String statusString = StatusEnumDTO.DISABLED.name();

        LocalDateTime localDateTime = LocalDateTime.now();

        Status status = new Status();
        status.setId(2L);
        status.setStatus(StatusEnum.DISABLED);

        UserDetails userDetails = new UserDetails();
        userDetails.setCreationDate(localDateTime);

        User user = new User();
        user.setId(userId);
        user.setLogin(RIGHT_USERNAME);
        user.setPassword(ENCODE_PASSWORD);
        user.setEMail(RIGHT_USERMAIL);
        user.setFirstName(RIGHT_FIRST_NAME);
        user.setStatus(status);
        user.setUserDetails(userDetails);

        Session session = new Session();
        session.setJwtToken(jwt);
        session.setCreationDate(localDateTime);
        session.setStatus(status);
        session.setUser(user);

        when(localDateTimeService.dateTimeWithZone(TIME_ZONE)).thenReturn(localDateTime);
        when(sessionRepository.findByJwt(jwt)).thenReturn(Optional.of(session));
        when(statusRepository.findByStatus(StatusEnum.valueOf(statusString))).thenReturn(Optional.of(status));
        when(sessionRepository.update(session)).thenReturn(session);
        when(userDetailsRepository.findById(userId)).thenReturn(userDetails);
        when(userDetailsRepository.update(userDetails)).thenReturn(userDetails);
        boolean isUpdatedDateTimeStatus = updateDateTimeStatusService.setExitDestructionDates(TIME_ZONE, jwt);
        Assertions.assertTrue(isUpdatedDateTimeStatus);
    }
}