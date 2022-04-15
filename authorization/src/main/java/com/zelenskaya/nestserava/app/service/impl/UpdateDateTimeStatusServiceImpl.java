package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.repository.SessionRepository;
import com.zelenskaya.nestserava.app.repository.StatusRepository;
import com.zelenskaya.nestserava.app.repository.UserDetailsRepository;
import com.zelenskaya.nestserava.app.repository.model.Session;
import com.zelenskaya.nestserava.app.repository.model.Status;
import com.zelenskaya.nestserava.app.repository.model.StatusEnum;
import com.zelenskaya.nestserava.app.repository.model.UserDetails;
import com.zelenskaya.nestserava.app.service.LocalDateTimeService;
import com.zelenskaya.nestserava.app.service.UpdateDateTimeStatusService;
import com.zelenskaya.nestserava.app.service.model.StatusEnumDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UpdateDateTimeStatusServiceImpl implements UpdateDateTimeStatusService {
    private final LocalDateTimeService localDateTimeService;
    private final SessionRepository sessionRepository;
    private final StatusRepository statusRepository;
    private final UserDetailsRepository userDetailsRepository;

    @Override
    @Transactional
    public boolean setExitDestructionDates(String timeZone, String jwtToken) {
        LocalDateTime localDateTime = localDateTimeService.dateTimeWithZone(timeZone);
        Optional<Session> session = sessionRepository.findByJwt(jwtToken);
        session.ifPresent(value -> value.setDestructionDate(localDateTime));
        Status status = generateStatus();
        session.ifPresent(value -> value.setStatus(status));
        session.ifPresent(sessionRepository::update);
        if (session.isPresent()) {
            UserDetails userDetails = generateUserDetails(session.get());
            UserDetails update = userDetailsRepository.update(userDetails);
            return update != null;
        }
        return false;
    }

    private UserDetails generateUserDetails(Session session) {
        Long userId = session.getUser().getId();
        UserDetails userDetails = userDetailsRepository.findById(userId);
        if (userDetails != null) {
            userDetails.setExitDate(session.getDestructionDate());
        }
        return userDetails;
    }

    private Status generateStatus() {
        String statusString = StatusEnumDTO.DISABLED.name();
        Optional<Status> status = statusRepository.findByStatus(StatusEnum.valueOf(statusString));
        return status.orElse(null);
    }
}
