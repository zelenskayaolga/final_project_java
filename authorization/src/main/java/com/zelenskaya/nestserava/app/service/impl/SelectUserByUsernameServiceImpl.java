package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.repository.RoleRepository;
import com.zelenskaya.nestserava.app.repository.UserRepository;
import com.zelenskaya.nestserava.app.repository.model.Role;
import com.zelenskaya.nestserava.app.repository.model.User;
import com.zelenskaya.nestserava.app.service.SelectUserByUsernameService;
import com.zelenskaya.nestserava.app.service.model.RoleEnumDTO;
import com.zelenskaya.nestserava.app.service.model.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class SelectUserByUsernameServiceImpl implements SelectUserByUsernameService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public UserDTO findByUsername(String username) {
        Optional<User> userByUsername = userRepository.getByUsername(username);
        if (userByUsername.isPresent()) {
            return userByUsername.map(this::convertToDTO).orElse(null);
        } else {
            userByUsername = userRepository.getByUsermail(username);
            if (userByUsername.isPresent()) {
                return userByUsername.map(this::convertToDTO).orElse(null);
            } else {
                return null;
            }
        }
    }

    private UserDTO convertToDTO(User userByUsername) {
        UserDTO userDTO = null;
        Optional<Role> roleByUsername = roleRepository.findByRole(userByUsername.getRole().getRole());
        if (roleByUsername.isPresent()) {
            String role = roleByUsername.get().getRole().name();
            userDTO = new UserDTO();
            userDTO.setId(userByUsername.getId());
            userDTO.setUsername(userByUsername.getLogin());
            userDTO.setPassword(userByUsername.getPassword());
            userDTO.setUsermail(userByUsername.getEMail());
            userDTO.setFirstName(userByUsername.getFirstName());
            userDTO.setRole(RoleEnumDTO.valueOf(role));
        }
        return userDTO;
    }
}