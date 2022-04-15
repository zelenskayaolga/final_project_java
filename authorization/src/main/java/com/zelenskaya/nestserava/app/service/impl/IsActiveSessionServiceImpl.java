package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.repository.SessionRepository;
import com.zelenskaya.nestserava.app.repository.model.Session;
import com.zelenskaya.nestserava.app.repository.model.StatusEnum;
import com.zelenskaya.nestserava.app.service.IsActiveSessionService;
import com.zelenskaya.nestserava.app.service.exceptions.ServiceAuthException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class IsActiveSessionServiceImpl implements IsActiveSessionService {
    private final SessionRepository sessionRepository;

    @Override
    @Transactional
    public boolean isActive(String jwt) {
        Optional<Session> sessionByJwt = sessionRepository.findByJwt(jwt);
        if (sessionByJwt.isPresent()) {
            String statusSession = sessionByJwt.get().getStatus().getStatus().name();
            return statusSession.equals(StatusEnum.ACTIVE.name());
        } else {
            throw new ServiceAuthException("Session is not found");
        }
    }
}
