package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.repository.UserRepository;
import com.zelenskaya.nestserava.app.service.ValidatorUserMailService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ValidatorUserMailServiceImpl implements ValidatorUserMailService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public boolean isUniqueUserMail(String usermail) {
        return userRepository.isUsermail(usermail);
    }
}

