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

import static com.zelenskaya.nestserava.app.service.impl.AuthorizationServiceTestConstants.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SelectUserByIdServiceImplTest {

    @InjectMocks
    private SelectUserByIdServiceImpl selectUserByIdService;

    @Mock
    private UserRepository userRepository;

    @Test
    void shouldReturnUserById() {
        Long userId = 1L;

        User user = new User();
        user.setId(userId);
        user.setLogin(RIGHT_USERNAME);
        user.setPassword(ENCODE_PASSWORD);
        user.setEMail(RIGHT_USERMAIL);
        user.setFirstName(RIGHT_FIRST_NAME);

        when(userRepository.findById(userId)).thenReturn(user);
        UserDTO userById = selectUserByIdService.findById(userId);
        Assertions.assertEquals(user.getId(), userById.getId());
    }
}