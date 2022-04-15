package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.service.LocalDateTimeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
@AllArgsConstructor
public class LocalDateTimeServiceImpl implements LocalDateTimeService {
    @Override
    public LocalDateTime dateTimeWithZone(String timeZone) {
        Instant nowUtc = Instant.now();
        ZoneId europeMinsk = ZoneId.of(timeZone);
        ZonedDateTime nowEuropeMinsk = ZonedDateTime.ofInstant(nowUtc, europeMinsk);
        return nowEuropeMinsk.toLocalDateTime();
    }
}
