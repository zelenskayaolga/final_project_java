package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.repository.UserDetailsRepository;
import com.zelenskaya.nestserava.app.repository.UserRepository;
import com.zelenskaya.nestserava.app.repository.model.User;
import com.zelenskaya.nestserava.app.repository.model.UserDetails;
import com.zelenskaya.nestserava.app.service.LocalDateTimeService;
import com.zelenskaya.nestserava.app.service.config.AuthServiceConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateUserDetailsServiceImplTest {

    @InjectMocks
    private UpdateUserDetailsServiceImpl updateUserDetailsService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserDetailsRepository userDetailsRepository;
    @Mock
    private LocalDateTimeService localDateTimeService;
    @Mock
    private AuthServiceConfig serviceConfig;

    @Test
    void shouldReturnUpdatedUserDetailsByUsernameWhenValidInput() {
        String username = "username";
        Long userId = 1L;
        int countOfDays = 1;

        LocalDateTime localDateTime = LocalDateTime.now();

        User user = new User();
        user.setId(userId);
        user.setLogin(username);

        UserDetails userDetails = new UserDetails();
        userDetails.setUserId(userId);
        userDetails.setCreationDate(localDateTime.minusDays(countOfDays));
        userDetails.setUser(user);

        UserDetails updatedUserDetails = new UserDetails();
        updatedUserDetails.setUserId(userId);
        updatedUserDetails.setCreationDate(localDateTime.minusDays(countOfDays));
        updatedUserDetails.setAuthorizationDate(localDateTime);
        updatedUserDetails.setUser(user);


        when(userRepository.getByUsername(username)).thenReturn(Optional.of(user));
        when(localDateTimeService.dateTimeWithZone(serviceConfig.getZoneTime())).thenReturn(localDateTime);
        when(userDetailsRepository.findById(userId)).thenReturn(userDetails);
        when(userDetailsRepository.update(userDetails)).thenReturn(updatedUserDetails);
        boolean isUpdateAuthorizationDate = updateUserDetailsService.updateAuthorizationDateByUsername(username);
        Assertions.assertTrue(isUpdateAuthorizationDate);
    }

    @Test
    void shouldReturnUpdatedUserDetailsByUsermailWhenValidInput() {
        String username = "maiko@gmail.com";
        Long userId = 1L;
        int countOfDays = 1;

        LocalDateTime localDateTime = LocalDateTime.now();

        User user = new User();
        user.setId(userId);
        user.setLogin(username);

        UserDetails userDetails = new UserDetails();
        userDetails.setUserId(userId);
        userDetails.setCreationDate(localDateTime.minusDays(countOfDays));
        userDetails.setUser(user);

        UserDetails updatedUserDetails = new UserDetails();
        updatedUserDetails.setUserId(userId);
        updatedUserDetails.setCreationDate(localDateTime.minusDays(countOfDays));
        updatedUserDetails.setAuthorizationDate(localDateTime);
        updatedUserDetails.setUser(user);

        when(userRepository.getByUsermail(username)).thenReturn(Optional.of(user));
        when(localDateTimeService.dateTimeWithZone(serviceConfig.getZoneTime())).thenReturn(localDateTime);
        when(userDetailsRepository.findById(userId)).thenReturn(userDetails);
        when(userDetailsRepository.update(userDetails)).thenReturn(updatedUserDetails);
        boolean isUpdateAuthorizationDate = updateUserDetailsService.updateAuthorizationDateByUsername(username);
        Assertions.assertTrue(isUpdateAuthorizationDate);
    }
}