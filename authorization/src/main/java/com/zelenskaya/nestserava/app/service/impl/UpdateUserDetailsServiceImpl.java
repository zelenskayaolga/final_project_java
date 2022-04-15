package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.repository.UserDetailsRepository;
import com.zelenskaya.nestserava.app.repository.UserRepository;
import com.zelenskaya.nestserava.app.repository.model.User;
import com.zelenskaya.nestserava.app.repository.model.UserDetails;
import com.zelenskaya.nestserava.app.service.LocalDateTimeService;
import com.zelenskaya.nestserava.app.service.UpdateUserDetailsService;
import com.zelenskaya.nestserava.app.service.config.AuthServiceConfig;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UpdateUserDetailsServiceImpl implements UpdateUserDetailsService {
    private final UserRepository userRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final LocalDateTimeService localDateTimeService;
    private final AuthServiceConfig serviceConfig;

    @Override
    @Transactional
    public boolean updateAuthorizationDateByUsername(String username) {
        Optional<User> userByUsername = userRepository.getByUsername(username);
        if (userByUsername.isPresent()) {
            return update(userByUsername.get());
        } else {
            userByUsername = userRepository.getByUsermail(username);
            if (userByUsername.isPresent()) {
                return update(userByUsername.get());
            }
        }
        return false;
    }

    private boolean update(User user) {
        LocalDateTime localDateTime = localDateTimeService.dateTimeWithZone(serviceConfig.getZoneTime());
        UserDetails userDetails = generateUserDetails(user, localDateTime);
        UserDetails updatedUserDetails = userDetailsRepository.update(userDetails);
        return updatedUserDetails != null;
    }

    private UserDetails generateUserDetails(User user, LocalDateTime localDateTime) {
        UserDetails userDetails;
        Long userId = user.getId();
        userDetails = userDetailsRepository.findById(userId);
        if (userDetails != null) {
            userDetails.setAuthorizationDate(localDateTime);
        }
        return userDetails;
    }
}
