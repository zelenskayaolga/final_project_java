package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.repository.AttemptsRepository;
import com.zelenskaya.nestserava.app.repository.model.Attempts;
import com.zelenskaya.nestserava.app.service.SelectUserByUsernameService;
import com.zelenskaya.nestserava.app.service.model.AttemptsDTO;
import com.zelenskaya.nestserava.app.service.model.RoleEnumDTO;
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
class UpdateAttemptsServiceImplTest {

    @InjectMocks
    private UpdateAttemptsServiceImpl updateAttemptsService;

    @Mock
    private SelectUserByUsernameService userService;
    @Mock
    private AttemptsRepository attemptsRepository;

    @Test
    void shouldReturnUpdatedByUsernameAttemptsDTO() {
        String username = "username";

        Attempts attempts = new Attempts();
        attempts.setCount(RIGHT_COUNT_OF_ATTEMPTS);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(RIGHT_ID);
        userDTO.setUsername(RIGHT_USERNAME);
        userDTO.setPassword(RIGHT_PASSWORD);
        userDTO.setUsermail(RIGHT_USERMAIL);
        userDTO.setFirstName(RIGHT_FIRST_NAME);
        userDTO.setRole(RoleEnumDTO.valueOf(ROLE_EMPLOYEE));

        when(userService.findByUsername(username)).thenReturn(userDTO);
        when(attemptsRepository.findById(userDTO.getId())).thenReturn(attempts);
        AttemptsDTO updatedAttemptsDTO = updateAttemptsService.updateByUsername(username);
        Assertions.assertNotNull(updatedAttemptsDTO);
    }

    @Test
    void shouldReturnNullWhenAttemptsDTOWasNotUpdatedByUsername() {
        String username = "username";

        when(userService.findByUsername(username)).thenReturn(null);
        AttemptsDTO updatedAttemptsDTO = updateAttemptsService.updateByUsername(username);
        Assertions.assertNull(updatedAttemptsDTO);
    }
}