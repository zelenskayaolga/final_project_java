package com.zelenskaya.nestserava.app.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static com.zelenskaya.nestserava.app.service.impl.AuthorizationServiceTestConstants.TIME_ZONE;

@ExtendWith(MockitoExtension.class)
class LocalDateTimeServiceImplTest {

    @InjectMocks
    private LocalDateTimeServiceImpl localDateTimeService;

    @Test
    void shouldReturnLocalDate() {
        LocalDateTime localDateTime = localDateTimeService.dateTimeWithZone(TIME_ZONE);
        Assertions.assertNotNull(localDateTime);
    }
}
