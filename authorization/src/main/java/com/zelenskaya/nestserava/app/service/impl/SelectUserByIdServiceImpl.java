package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.repository.UserRepository;
import com.zelenskaya.nestserava.app.repository.model.User;
import com.zelenskaya.nestserava.app.service.SelectUserByIdService;
import com.zelenskaya.nestserava.app.service.model.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SelectUserByIdServiceImpl implements SelectUserByIdService {
    private final UserRepository userRepository;

    @Override
    public UserDTO findById(Long userId) {
        User userById = userRepository.findById(userId);
        return convertToDTO(userById);
    }

    private UserDTO convertToDTO(User userById) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userById.getId());
        userDTO.setUsername(userById.getLogin());
        userDTO.setPassword(userById.getPassword());
        userDTO.setUsermail(userById.getEMail());
        userDTO.setFirstName(userById.getFirstName());
        return userDTO;
    }
}
