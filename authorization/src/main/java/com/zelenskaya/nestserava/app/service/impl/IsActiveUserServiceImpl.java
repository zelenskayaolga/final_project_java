package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.repository.UserRepository;
import com.zelenskaya.nestserava.app.repository.model.User;
import com.zelenskaya.nestserava.app.service.IsActiveUserService;
import com.zelenskaya.nestserava.app.service.model.StatusEnumDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class IsActiveUserServiceImpl implements IsActiveUserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public boolean isActiveUserByUsername(String username) {
        Optional<User> user = userRepository.getByUsername(username);
        if (user.isPresent()) {
            String statusOfUser = user.get().getStatus().getStatus().name();
            return statusOfUser.equals(StatusEnumDTO.ACTIVE.name());
        } else {
            user = userRepository.getByUsermail(username);
            if (user.isPresent()) {
                String statusOfUser = user.get().getStatus().getStatus().name();
                return statusOfUser.equals(StatusEnumDTO.ACTIVE.name());
            }
        }
        return false;
    }
}
