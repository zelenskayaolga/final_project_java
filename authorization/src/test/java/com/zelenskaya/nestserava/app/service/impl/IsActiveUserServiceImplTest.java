package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.repository.UserRepository;
import com.zelenskaya.nestserava.app.repository.model.Status;
import com.zelenskaya.nestserava.app.repository.model.StatusEnum;
import com.zelenskaya.nestserava.app.repository.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.zelenskaya.nestserava.app.service.impl.AuthorizationServiceTestConstants.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IsActiveUserServiceImplTest {

    @InjectMocks
    private IsActiveUserServiceImpl isActiveUserService;

    @Mock
    private UserRepository userRepository;

    @Test
    void shouldReturnTrueWhenUserIsActiveByUsername() {
        String username = "username";

        Status status = new Status();
        status.setId(RIGHT_ID);
        status.setStatus(StatusEnum.ACTIVE);

        User user = new User();
        user.setId(RIGHT_ID);
        user.setLogin(RIGHT_USERNAME);
        user.setPassword(RIGHT_PASSWORD);
        user.setEMail(RIGHT_USERMAIL);
        user.setFirstName(RIGHT_FIRST_NAME);
        user.setStatus(status);

        when(userRepository.getByUsername(user.getLogin())).thenReturn(Optional.of(user));
        boolean isActive = isActiveUserService.isActiveUserByUsername(username);
        Assertions.assertTrue(isActive);
    }

    @Test
    void shouldReturnTrueWhenUserIsActiveByUsermail() {
        String username = "maiko@gmail.com";

        Status status = new Status();
        status.setId(RIGHT_ID);
        status.setStatus(StatusEnum.ACTIVE);

        User user = new User();
        user.setId(RIGHT_ID);
        user.setLogin(RIGHT_USERNAME);
        user.setPassword(RIGHT_PASSWORD);
        user.setEMail(RIGHT_USERMAIL);
        user.setFirstName(RIGHT_FIRST_NAME);
        user.setStatus(status);

        when(userRepository.getByUsermail(user.getEMail())).thenReturn(Optional.of(user));
        boolean isActive = isActiveUserService.isActiveUserByUsername(username);
        Assertions.assertTrue(isActive);
    }
}