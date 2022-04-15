package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.service.LocalDateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class LocalDateServiceImpl implements LocalDateService {
    @Override
    @Transactional
    public LocalDate dateTimeWithZone(String timeZone) {
        Instant nowUtc = Instant.now();
        ZoneId europeMinsk = ZoneId.of(timeZone);
        ZonedDateTime nowEuropeMinsk = ZonedDateTime.ofInstant(nowUtc, europeMinsk);
        return nowEuropeMinsk.toLocalDate();
    }
}