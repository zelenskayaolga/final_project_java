package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.repository.AttemptsRepository;
import com.zelenskaya.nestserava.app.repository.RoleRepository;
import com.zelenskaya.nestserava.app.repository.StatusRepository;
import com.zelenskaya.nestserava.app.repository.UserDetailsRepository;
import com.zelenskaya.nestserava.app.repository.UserRepository;
import com.zelenskaya.nestserava.app.repository.model.Attempts;
import com.zelenskaya.nestserava.app.repository.model.Role;
import com.zelenskaya.nestserava.app.repository.model.RoleEnum;
import com.zelenskaya.nestserava.app.repository.model.Status;
import com.zelenskaya.nestserava.app.repository.model.StatusEnum;
import com.zelenskaya.nestserava.app.repository.model.User;
import com.zelenskaya.nestserava.app.repository.model.UserDetails;
import com.zelenskaya.nestserava.app.service.AddUserService;
import com.zelenskaya.nestserava.app.service.LocalDateTimeService;
import com.zelenskaya.nestserava.app.service.config.AuthServiceConfig;
import com.zelenskaya.nestserava.app.service.model.AddedUserDTO;
import com.zelenskaya.nestserava.app.service.model.RoleEnumDTO;
import com.zelenskaya.nestserava.app.service.model.StatusEnumDTO;
import com.zelenskaya.nestserava.app.service.model.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AddUserServiceImpl implements AddUserService {
    private final UserRepository userRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final RoleRepository roleRepository;
    private final StatusRepository statusRepository;
    private final PasswordEncoder encoder;
    private final LocalDateTimeService localDateTimeService;
    private final AuthServiceConfig serviceConfig;
    private final AttemptsRepository attemptsRepository;

    @Override
    @Transactional
    public AddedUserDTO add(UserDTO userDTO) {
        User user = convertToUser(userDTO);
        UserDetails userDetails = generateUserDetails();
        user.setUserDetails(userDetails);
        userDetails.setUser(user);
        Attempts attempts = generateAttempts();
        user.setAttempts(attempts);
        attempts.setUser(user);
        attemptsRepository.add(attempts);
        userDetailsRepository.add(userDetails);
        userRepository.add(user);
        return convertToDTO(user);
    }

    private Attempts generateAttempts() {
        Attempts attempts = new Attempts();
        attempts.setCount(0);
        return attempts;
    }

    private Role generateRole() {
        String roleString = RoleEnumDTO.ROLE_EMPLOYEE.name();
        Optional<Role> role = roleRepository.findByRole(RoleEnum.valueOf(roleString));
        return role.orElse(null);
    }

    private Status generateStatus() {
        String statusString = StatusEnumDTO.ACTIVE.name();
        Optional<Status> status = statusRepository.findByStatus(StatusEnum.valueOf(statusString));
        return status.orElse(null);
    }

    private UserDetails generateUserDetails() {
        LocalDateTime ldt = localDateTimeService.dateTimeWithZone(serviceConfig.getZoneTime());
        UserDetails userDetails = new UserDetails();
        userDetails.setCreationDate(ldt);
        return userDetails;
    }

    private AddedUserDTO convertToDTO(User user) {
        AddedUserDTO addedUserDTO = new AddedUserDTO();
        addedUserDTO.setUserId(user.getId());
        StatusEnum statusEnum = user.getStatus().getStatus();
        addedUserDTO.setStatus(StatusEnumDTO.valueOf(statusEnum.name()));
        return addedUserDTO;
    }

    private User convertToUser(UserDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.getUsername());
        String encodePassword = encoder.encode(userDTO.getPassword());
        user.setPassword(encodePassword);
        user.setEMail(userDTO.getUsermail());
        user.setFirstName(userDTO.getFirstName());
        Status status = generateStatus();
        user.setStatus(status);
        Role role = generateRole();
        user.setRole(role);
        return user;
    }
}

