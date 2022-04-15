package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.repository.UserRepository;
import com.zelenskaya.nestserava.app.repository.model.User;
import com.zelenskaya.nestserava.app.service.ValidatorAuthenticationService;
import com.zelenskaya.nestserava.app.service.model.UserDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ValidatorAuthenticationServiceImpl implements ValidatorAuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public boolean isValidUsername(String username, String password) {
        Optional<User> user = userRepository.getByUsername(username);
        if (user.isPresent()) {
            UserDTO userDTO = user.map(this::convertToDTOWithUsername).orElse(null);
            return passwordEncoder.matches(password, userDTO.getPassword());
        } else {
            Optional<User> userByUsername = userRepository.getByUsermail(username);
            UserDTO userDTO = userByUsername.map(this::convertToDTOWithUsermail).orElse(null);
            if (userDTO != null) {
                return passwordEncoder.matches(password, userDTO.getPassword());
            } else {
                return false;
            }
        }
    }

    private UserDTO convertToDTOWithUsermail(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getEMail());
        userDTO.setPassword(user.getPassword());
        return userDTO;
    }

    private UserDTO convertToDTOWithUsername(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getLogin());
        userDTO.setPassword(user.getPassword());
        return userDTO;
    }
}
