package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.service.SelectUserByUsernameService;
import com.zelenskaya.nestserava.app.service.model.RoleEnumDTO;
import com.zelenskaya.nestserava.app.service.model.UserDTO;
import com.zelenskaya.nestserava.app.service.model.UserDetailsImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

import static com.zelenskaya.nestserava.app.service.impl.AuthorizationServiceTestConstants.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailServiceImplTest {

    @InjectMocks
    private UserDetailServiceImpl userDetailService;

    @Mock
    private SelectUserByUsernameService selectUserByUsernameService;

    @Test
    void shouldReturnUserByUsernameWhenUsernameIsNotNull() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(RIGHT_ID);
        userDTO.setUsername(RIGHT_USERNAME);
        userDTO.setPassword(RIGHT_PASSWORD);
        userDTO.setUsermail(RIGHT_USERMAIL);
        userDTO.setFirstName(RIGHT_FIRST_NAME);
        userDTO.setRole(RoleEnumDTO.valueOf(ROLE_EMPLOYEE));

        UserDetails userDetails = new UserDetailsImpl(
                RIGHT_ID,
                RIGHT_USERNAME,
                RIGHT_PASSWORD,
                RIGHT_USERMAIL,
                RIGHT_FIRST_NAME,
                Collections.singletonList(new SimpleGrantedAuthority(ROLE_EMPLOYEE))
        );

        when(selectUserByUsernameService.findByUsername(userDTO.getUsername())).thenReturn(userDTO);
        UserDetails user = userDetailService.loadUserByUsername(RIGHT_USERNAME);
        Assertions.assertEquals(userDTO.getUsername(), user.getUsername());
    }
}