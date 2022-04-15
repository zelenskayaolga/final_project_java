package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.repository.SessionRepository;
import com.zelenskaya.nestserava.app.repository.StatusRepository;
import com.zelenskaya.nestserava.app.repository.UserRepository;
import com.zelenskaya.nestserava.app.repository.model.Session;
import com.zelenskaya.nestserava.app.repository.model.Status;
import com.zelenskaya.nestserava.app.repository.model.StatusEnum;
import com.zelenskaya.nestserava.app.repository.model.User;
import com.zelenskaya.nestserava.app.service.BlockUserService;
import com.zelenskaya.nestserava.app.service.LocalDateTimeService;
import com.zelenskaya.nestserava.app.service.config.AuthServiceConfig;
import com.zelenskaya.nestserava.app.service.model.StatusEnumDTO;
import com.zelenskaya.nestserava.app.service.model.UserDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class BlockUserServiceImpl implements BlockUserService {
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final StatusRepository statusRepository;
    private final LocalDateTimeService localDateTimeService;
    private final AuthServiceConfig authServiceConfig;

    @Override
    @Transactional
    public UserDTO blockByUsername(String username) {
        Optional<User> userByUsername = userRepository.getByUsername(username);
        if (userByUsername.isPresent()) {
            Status status = generateStatus();
            userByUsername.get().setStatus(status);
            userRepository.update(userByUsername.get());
            Set<Session> sessions = generateSession(userByUsername.get());
            for (Session session : sessions) {
                session.setUser(userByUsername.get());
            }
            Set<Session> blockedSessions = sessions.stream()
                    .map(sessionRepository::update)
                    .collect(Collectors.toSet());
            log.info("Amount of blocked sessions is " + blockedSessions.size());
        } else {
            userByUsername = userRepository.getByUsermail(username);
            if (userByUsername.isPresent()) {
                Status status = generateStatus();
                userByUsername.get().setStatus(status);
                userRepository.update(userByUsername.get());
            } else {
                return null;
            }
        }
        return null;
    }

    private Set<Session> generateSession(User user) {
        List<Session> sessionsList = userRepository.getByUserId(user.getId());
        Status status = generateStatus();
        for (Session session : sessionsList) {
            session.setStatus(status);
            session.setDestructionDate(localDateTimeService.dateTimeWithZone(authServiceConfig.getZoneTime()));
        }
        return new HashSet<>(sessionsList);
    }

    private Status generateStatus() {
        String statusString = StatusEnumDTO.DISABLED.name();
        Optional<Status> status = statusRepository.findByStatus(StatusEnum.valueOf(statusString));
        return status.orElse(null);
    }
}
