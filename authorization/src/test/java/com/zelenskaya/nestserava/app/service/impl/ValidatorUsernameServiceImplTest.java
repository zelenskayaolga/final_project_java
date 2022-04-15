package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidatorUsernameServiceImplTest {

    @InjectMocks
    private ValidatorUsernameServiceImpl validatorUsernameService;

    @Mock
    private UserRepository userRepository;

    @Test
    void shouldReturnTrueWhenUserByUsernameIsUnique() {
        String username = "username";
        when(userRepository.isUsername(username)).thenReturn(true);
        boolean isUniqueUserMail = validatorUsernameService.isUniqueUsername(username);
        Assertions.assertTrue(userRepository.isUsername(username));
    }
}