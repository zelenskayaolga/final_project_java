package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.repository.SessionRepository;
import com.zelenskaya.nestserava.app.repository.UserRepository;
import com.zelenskaya.nestserava.app.repository.model.User;
import com.zelenskaya.nestserava.app.service.CloseSessionService;
import com.zelenskaya.nestserava.app.service.UpdateDateTimeStatusService;
import com.zelenskaya.nestserava.app.service.ValidateSessionService;
import com.zelenskaya.nestserava.app.service.ValidatorSessionUsernameService;
import com.zelenskaya.nestserava.app.service.config.AuthServiceConfig;
import com.zelenskaya.nestserava.app.service.exceptions.ServiceAuthException;
import com.zelenskaya.nestserava.app.service.model.LogoutDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CloseSessionServiceImpl implements CloseSessionService {
    private final ValidatorSessionUsernameService validatorSessionService;
    private final ValidateSessionService validateSessionService;
    private final SessionRepository sessionRepository;
    private final UpdateDateTimeStatusService updateDateService;
    private final AuthServiceConfig authServiceConfig;
    private final UserRepository userRepository;

    @Override
    public boolean close(LogoutDTO logoutDTO) {
        boolean isValid = validatorSessionService.isValid(
                logoutDTO.getUsername(),
                logoutDTO.getSessionId()
        );
        if (isValid) {
            boolean isActiveSession = validateSessionService.isActive(logoutDTO.getSessionId());
            if (isActiveSession) {
                updateDateService.setExitDestructionDates(
                        authServiceConfig.getZoneTime(),
                        logoutDTO.getSessionId()
                );
                return true;
            } else {
                List<String> tokens = generateActiveTokens(logoutDTO.getUsername());
                for (String token : tokens) {
                    updateDateService.setExitDestructionDates(
                            authServiceConfig.getZoneTime(), token
                    );
                }
                return false;
            }
        } else {
            throw new ServiceAuthException("Проверьте правильность введённых данных");
        }
    }

    private List<String> generateActiveTokens(String username) {
        List<String> tokens = new ArrayList<>();
        Optional<User> user = userRepository.getByUsername(username);
        if (user.isPresent()) {
            tokens = sessionRepository.getJwtByUserId(user.get().getId());
        }
        return tokens;
    }
}
