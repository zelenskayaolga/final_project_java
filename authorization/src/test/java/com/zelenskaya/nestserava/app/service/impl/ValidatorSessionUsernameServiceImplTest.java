package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.repository.SessionRepository;
import com.zelenskaya.nestserava.app.repository.UserRepository;
import com.zelenskaya.nestserava.app.repository.model.*;
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
class ValidatorSessionUsernameServiceImplTest {

    @InjectMocks
    private ValidatorSessionUsernameServiceImpl validatorSessionUsernameService;

    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private UserRepository userRepository;

    @Test
    void shouldReturnValidUsername() {
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

        Session session = new Session();
        session.setJwtToken(jwt);
        session.setCreationDate(localDateTime);
        session.setStatus(status);
        session.setUser(user);

        when(sessionRepository.findByJwt(jwt)).thenReturn(Optional.of(session));
        when(userRepository.findById(session.getUser().getId())).thenReturn(user);
        boolean isUsername = validatorSessionUsernameService.isValid("username", jwt);
        Assertions.assertTrue(user.getLogin().equals("username"));
    }
}