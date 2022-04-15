package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.repository.RoleRepository;
import com.zelenskaya.nestserava.app.repository.UserRepository;
import com.zelenskaya.nestserava.app.repository.model.*;
import com.zelenskaya.nestserava.app.service.model.UserDTO;
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
class SelectUserByUsernameServiceImplTest {

    @InjectMocks
    private SelectUserByUsernameServiceImpl selectUserByUsernameService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;

    @Test
    void shouldReturnUserByUsername() {
        String username = "username";

        Role role = new Role();
        role.setId(RIGHT_ID);
        role.setRole(RoleEnum.ROLE_EMPLOYEE);

        Status status = new Status();
        status.setId(RIGHT_ID);
        status.setStatus(StatusEnum.ACTIVE);

        User user = new User();
        user.setLogin(username);
        user.setPassword(ENCODE_PASSWORD);
        user.setEMail(RIGHT_USERMAIL);
        user.setFirstName(RIGHT_FIRST_NAME);
        user.setStatus(status);
        user.setRole(role);

        when(userRepository.getByUsername(user.getLogin())).thenReturn(Optional.of(user));
        when(roleRepository.findByRole(user.getRole().getRole())).thenReturn(Optional.of(role));
        UserDTO userByUsername = selectUserByUsernameService.findByUsername(username);
        Assertions.assertEquals(user.getLogin(), userByUsername.getUsername());
    }

    @Test
    void shouldReturnUserByUsermail() {
        String username = "maiko@gmail.com";

        Role role = new Role();
        role.setId(RIGHT_ID);
        role.setRole(RoleEnum.ROLE_EMPLOYEE);

        Status status = new Status();
        status.setId(RIGHT_ID);
        status.setStatus(StatusEnum.ACTIVE);

        User user = new User();
        user.setPassword(ENCODE_PASSWORD);
        user.setEMail(username);
        user.setFirstName(RIGHT_FIRST_NAME);
        user.setStatus(status);
        user.setRole(role);

        when(roleRepository.findByRole(user.getRole().getRole())).thenReturn(Optional.of(role));
        when(userRepository.getByUsermail(username)).thenReturn(Optional.of(user));
        UserDTO userByUsername = selectUserByUsernameService.findByUsername(username);
        Assertions.assertEquals(user.getEMail(), userByUsername.getUsermail());
    }
}