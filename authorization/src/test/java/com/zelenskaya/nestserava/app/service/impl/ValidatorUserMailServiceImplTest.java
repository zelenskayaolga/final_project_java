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
class ValidatorUserMailServiceImplTest {

    @InjectMocks
    private ValidatorUserMailServiceImpl validatorUserMailService;

    @Mock
    private UserRepository userRepository;

    @Test
    void shouldReturnTrueWhenUserByUserMailIsUnique() {
        String usermail = "usermail@gmail.com";
        when(userRepository.isUsermail(usermail)).thenReturn(true);
        boolean isUniqueUserMail = validatorUserMailService.isUniqueUserMail(usermail);
        Assertions.assertTrue(userRepository.isUsermail(usermail));
    }
}