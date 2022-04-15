package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.repository.UserRepository;
import com.zelenskaya.nestserava.app.service.ValidatorUsernameService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ValidatorUsernameServiceImpl implements ValidatorUsernameService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public boolean isUniqueUsername(String username) {
        return userRepository.isUsername(username);
    }
}
