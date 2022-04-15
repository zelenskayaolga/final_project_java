package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.repository.UserRepository;
import com.zelenskaya.nestserava.app.repository.model.User;
import com.zelenskaya.nestserava.app.service.model.UserDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.zelenskaya.nestserava.app.service.impl.AuthorizationServiceTestConstants.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidatorAuthenticationServiceImplTest {

    @InjectMocks
    private ValidatorAuthenticationServiceImpl validatorAuthenticationService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldReturnTrueWhenValidUsername() {
        String username = "username";
        String password = "Maiko11!K";

        User user = new User();
        user.setLogin(RIGHT_USERNAME);
        user.setPassword(RIGHT_PASSWORD);

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(RIGHT_USERNAME);
        userDTO.setPassword(RIGHT_PASSWORD);

        when(userRepository.getByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, userDTO.getPassword())).thenReturn(true);
        boolean isValidUsername = validatorAuthenticationService.isValidUsername(username, password);
        Assertions.assertTrue(passwordEncoder.matches(password, userDTO.getPassword()));
    }

    @Test
    void shouldReturnTrueWhenValidUsernameByUsermail() {
        String username = "username";
        String password = "maiko@gmail.com";

        User user = new User();
        user.setLogin(RIGHT_USERMAIL);
        user.setPassword(RIGHT_PASSWORD);

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(RIGHT_USERMAIL);
        userDTO.setPassword(RIGHT_PASSWORD);

        when(userRepository.getByUsermail(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, userDTO.getPassword())).thenReturn(true);
        boolean isValidUsername = validatorAuthenticationService.isValidUsername(username, password);
        Assertions.assertTrue(passwordEncoder.matches(password, userDTO.getPassword()));
    }
}