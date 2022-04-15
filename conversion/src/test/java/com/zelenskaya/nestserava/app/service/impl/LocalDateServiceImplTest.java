package com.zelenskaya.nestserava.app.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
class LocalDateServiceImplTest {

    @InjectMocks
    private LocalDateServiceImpl localDateService;

    @Test
    void shouldReturnLocalDate() {
        LocalDate localDate = localDateService.dateTimeWithZone("Europe/Minsk");
        Assertions.assertNotNull(localDate);
    }
}
