package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.repository.SessionRepository;
import com.zelenskaya.nestserava.app.repository.model.Session;
import com.zelenskaya.nestserava.app.repository.model.StatusEnum;
import com.zelenskaya.nestserava.app.service.ValidateSessionService;
import com.zelenskaya.nestserava.app.service.config.AuthServiceConfig;
import com.zelenskaya.nestserava.app.service.exceptions.ServiceAuthException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ValidateSessionServiceImpl implements ValidateSessionService {
    private final SessionRepository sessionRepository;
    private final UpdateDateTimeStatusServiceImpl updateDateService;
    private final AuthServiceConfig serviceConfig;

    @Override
    @Transactional
    public boolean isActive(String jwt) {
        Optional<Session> session = sessionRepository.findByJwt(jwt);
        boolean isActiveSession;
        if (session.isPresent()) {
            Integer period = session.get().getTimeout().getPeriod();
            LocalDateTime creationDate = session.get().getCreationDate();
            LocalDateTime exitDate = creationDate.plus(Duration.of(period, ChronoUnit.HOURS));
            LocalDateTime today = LocalDateTime.now(ZoneId.of(serviceConfig.getZoneTime()));
            isActiveSession = (!today.isBefore(creationDate)) && (today.isBefore(exitDate));
            if (!isActiveSession) {
                updateDateService.setExitDestructionDates(serviceConfig.getZoneTime(), jwt);
            }
            String status = session.get().getStatus().getStatus().name();
            if (isActiveSession && status.equals(StatusEnum.ACTIVE.name())) {
                return true;
            }
            if (isActiveSession && status.equals(StatusEnum.DISABLED.name())) {
                return false;
            }
        } else {
            throw new ServiceAuthException("Сессия с указанным id не найдена");
        }
        return false;
    }
}
