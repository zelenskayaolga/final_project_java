package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.repository.*;
import com.zelenskaya.nestserava.app.repository.model.*;
import com.zelenskaya.nestserava.app.service.LocalDateTimeService;
import com.zelenskaya.nestserava.app.service.config.AuthServiceConfig;
import com.zelenskaya.nestserava.app.service.model.AddedUserDTO;
import com.zelenskaya.nestserava.app.service.model.RoleEnumDTO;
import com.zelenskaya.nestserava.app.service.model.StatusEnumDTO;
import com.zelenskaya.nestserava.app.service.model.UserDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.zelenskaya.nestserava.app.service.impl.AuthorizationServiceTestConstants.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddUserServiceImplTest {

    @InjectMocks
    private AddUserServiceImpl addUserService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserDetailsRepository userDetailsRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private StatusRepository statusRepository;
    @Mock
    private PasswordEncoder encoder;
    @Mock
    private LocalDateTimeService localDateTimeService;
    @Mock
    private AuthServiceConfig serviceConfig;
    @Mock
    private AttemptsRepository attemptsRepository;

    @Test
    void shouldReturnAddedUserDTOWhenValidInput() {
        String roleString = RoleEnumDTO.ROLE_EMPLOYEE.name();
        String statusString = StatusEnumDTO.ACTIVE.name();

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

        User user = new User();
        user.setId(RIGHT_ID);
        user.setLogin(RIGHT_USERNAME);
        user.setPassword(ENCODE_PASSWORD);
        user.setEMail(RIGHT_USERMAIL);
        user.setFirstName(RIGHT_FIRST_NAME);
        user.setStatus(status);
        user.setRole(role);
        user.setAttempts(attempts);
        user.setUserDetails(userDetails);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(RIGHT_ID);
        userDTO.setUsername(RIGHT_USERNAME);
        userDTO.setPassword(RIGHT_PASSWORD);
        userDTO.setUsermail(RIGHT_USERMAIL);
        userDTO.setFirstName(RIGHT_FIRST_NAME);
        userDTO.setRole(RoleEnumDTO.valueOf(ROLE_EMPLOYEE));

        AddedUserDTO addedUserDTO = new AddedUserDTO();
        addedUserDTO.setUserId(RIGHT_ID);
        addedUserDTO.setStatus(StatusEnumDTO.valueOf(STATUS_ACTIVE));

        when(encoder.encode(userDTO.getPassword())).thenReturn(ENCODE_PASSWORD);
        when(roleRepository.findByRole(RoleEnum.valueOf(roleString))).thenReturn(Optional.of(role));
        when(statusRepository.findByStatus(StatusEnum.valueOf(statusString))).thenReturn(Optional.of(status));
        when(localDateTimeService.dateTimeWithZone(serviceConfig.getZoneTime())).thenReturn(localDateTime);
        AddedUserDTO addedUser = addUserService.add(userDTO);
        Assertions.assertEquals(addedUserDTO.getStatus(), addedUser.getStatus());
    }
}
