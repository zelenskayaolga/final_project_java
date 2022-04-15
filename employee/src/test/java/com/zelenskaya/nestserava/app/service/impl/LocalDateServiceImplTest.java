package com.zelenskaya.nestserava.app.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static com.zelenskaya.nestserava.app.service.impl.EmployeeServiceTestConstants.TIME_ZONE;

@ExtendWith(MockitoExtension.class)
class LocalDateServiceImplTest {

    @InjectMocks
    private LocalDateServiceImpl localDateService;

    @Test
    void shouldReturnLocalDate() {
        LocalDate localDate = localDateService.dateTimeWithZone(TIME_ZONE);
        Assertions.assertNotNull(localDate);
    }
}
