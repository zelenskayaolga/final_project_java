package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.controller.security.filter.ParseJwt;
import com.zelenskaya.nestserava.app.controller.security.util.JwtUtilsConversion;
import com.zelenskaya.nestserava.app.repository.ApplicationDetailsRepository;
import com.zelenskaya.nestserava.app.repository.model.ApplicationDetails;
import com.zelenskaya.nestserava.app.service.GetUserByIdService;
import com.zelenskaya.nestserava.app.service.LocalDateService;
import com.zelenskaya.nestserava.app.service.config.ServiceConfig;
import com.zelenskaya.nestserava.app.service.model.UsernameDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateApplicationDetailsServiceImplTest {

    @InjectMocks
    private UpdateApplicationDetailsServiceImpl updateApplicationDetailsService;

    @Mock
    private ApplicationDetailsRepository applicationDetailsRepository;
    @Mock
    private GetUserByIdService getUserByUsernameService;
    @Mock
    private LocalDateService localDateService;
    @Mock
    private ServiceConfig serviceConfig;
    @Mock
    private JwtUtilsConversion jwtUtils;
    @Mock
    private ParseJwt parseJwt;

    @Test
    void shouldReturn() {
        Long id = 1L;
        String jwt = "JWT";
        String name = "username";

        HttpServletRequest request = mock(HttpServletRequest.class);

        ResponseEntity<Object> responseEntity = ResponseEntity.status(HttpStatus.OK).body(Map.of(
                name, "username", id, 1L
        ));

        LocalDate localDate = LocalDate.now();

        ApplicationDetails applicationDetails = new ApplicationDetails();
        applicationDetails.setUserId(id);
        applicationDetails.setUpdateDate(localDate);

        ApplicationDetails updatedApplicationDetails = new ApplicationDetails();
        applicationDetails.setUserId(applicationDetails.getUserId());
        applicationDetails.setUpdateDate(localDate);

        UsernameDTO usernameDTO = new UsernameDTO();
        usernameDTO.setUsername("userName");

        when(applicationDetailsRepository.findById(applicationDetails.getUserId()))
                .thenReturn(Optional.of(applicationDetails));
        when(serviceConfig.getZoneTime()).thenReturn("Europe/Minsk");
        when(localDateService.dateTimeWithZone(serviceConfig.getZoneTime())).thenReturn(localDate);
        when(applicationDetailsRepository.save(applicationDetails)).thenReturn(updatedApplicationDetails);
        when(parseJwt.parse(request)).thenReturn(jwt);
        when(jwtUtils.getIdFromJwtToken(jwt)).thenReturn(id);
        when(getUserByUsernameService.getUserById(updatedApplicationDetails.getUserId())).thenReturn(responseEntity);
        UsernameDTO username = updateApplicationDetailsService.updateDate(id, request);
        Assertions.assertNotNull(username);
    }
}
