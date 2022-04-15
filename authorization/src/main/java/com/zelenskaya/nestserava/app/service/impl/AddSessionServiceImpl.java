package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.repository.SessionRepository;
import com.zelenskaya.nestserava.app.repository.StatusRepository;
import com.zelenskaya.nestserava.app.repository.TimeoutRepository;
import com.zelenskaya.nestserava.app.repository.UserRepository;
import com.zelenskaya.nestserava.app.repository.model.Session;
import com.zelenskaya.nestserava.app.repository.model.Status;
import com.zelenskaya.nestserava.app.repository.model.StatusEnum;
import com.zelenskaya.nestserava.app.repository.model.Timeout;
import com.zelenskaya.nestserava.app.repository.model.User;
import com.zelenskaya.nestserava.app.service.AddSessionService;
import com.zelenskaya.nestserava.app.service.LocalDateTimeService;
import com.zelenskaya.nestserava.app.service.config.AuthServiceConfig;
import com.zelenskaya.nestserava.app.service.model.AuthDTO;
import com.zelenskaya.nestserava.app.service.model.StatusEnumDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class AddSessionServiceImpl implements AddSessionService {
    private final SessionRepository sessionRepository;
    private final StatusRepository statusRepository;
    private final UserRepository userRepository;
    private final TimeoutRepository timeoutRepository;
    private final LocalDateTimeService localDateTimeService;
    private final AuthServiceConfig serviceConfig;

    @Override
    @Transactional
    public AuthDTO addWithUsername(String jwtToken, String username) {
        Set<Session> sessions = new HashSet<>();
        Session session = generateSession(jwtToken);
        sessions.add(session);
        Optional<User> user = userRepository.getByUsername(username);
        if (user.isEmpty()) {
            user = userRepository.getByUsermail(username);
        }
        user.ifPresent(value -> value.setSessions(sessions));
        user.ifPresent(session::setUser);
        Optional<Timeout> timeout = timeoutRepository.findByPeriod(serviceConfig.getJwtExpirationHours());
        timeout.ifPresent(session::setTimeout);
        sessionRepository.add(session);
        return convertToDTO(session);
    }

    private AuthDTO convertToDTO(Session session) {
        AuthDTO authDTO = new AuthDTO();
        authDTO.setSessionId(session.getJwtToken());
        return authDTO;
    }

    private Session generateSession(String jwt) {
        LocalDateTime ldt = localDateTimeService.dateTimeWithZone(serviceConfig.getZoneTime());
        Session session = new Session();
        session.setJwtToken(jwt);
        session.setCreationDate(ldt);
        Status status = generateStatus();
        session.setStatus(status);
        return session;
    }

    private Status generateStatus() {
        String statusString = StatusEnumDTO.ACTIVE.name();
        Optional<Status> status = statusRepository.findByStatus(StatusEnum.valueOf(statusString));
        return status.orElse(null);
    }
}
