package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.repository.SessionRepository;
import com.zelenskaya.nestserava.app.repository.UserRepository;
import com.zelenskaya.nestserava.app.repository.model.Session;
import com.zelenskaya.nestserava.app.repository.model.User;
import com.zelenskaya.nestserava.app.service.ValidatorSessionUsernameService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ValidatorSessionUsernameServiceImpl implements ValidatorSessionUsernameService {
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    @Override
    public boolean isValid(String username, String jwt) {
        Optional<Session> session = sessionRepository.findByJwt(jwt);
        if (session.isPresent()) {
            Long userId = session.get().getUser().getId();
            User user = userRepository.findById(userId);
            return user.getLogin().equals(username);
        } else {
            log.debug("The session with the specified id was not found");
        }
        return false;
    }
}
