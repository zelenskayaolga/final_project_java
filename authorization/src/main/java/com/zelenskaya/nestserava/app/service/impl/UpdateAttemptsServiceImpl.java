package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.repository.AttemptsRepository;
import com.zelenskaya.nestserava.app.repository.model.Attempts;
import com.zelenskaya.nestserava.app.repository.model.User;
import com.zelenskaya.nestserava.app.service.SelectUserByUsernameService;
import com.zelenskaya.nestserava.app.service.UpdateAttemptsService;
import com.zelenskaya.nestserava.app.service.model.AttemptsDTO;
import com.zelenskaya.nestserava.app.service.model.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UpdateAttemptsServiceImpl implements UpdateAttemptsService {
    private final SelectUserByUsernameService userService;
    private final AttemptsRepository attemptsRepository;

    @Override
    @Transactional
    public AttemptsDTO updateByUsername(String username) {
        UserDTO userDTO = userService.findByUsername(username);
        if (userDTO != null) {
            Attempts attemptsById = attemptsRepository.findById(userDTO.getId());
            attemptsById.setCount(attemptsById.getCount() + 1);
            User user = convertToUser(userDTO);
            user.setAttempts(attemptsById);
            attemptsRepository.update(attemptsById);
            return convertToDTO(attemptsById);
        }
        return null;
    }

    @Override
    @Transactional
    public void updateToZero(String username) {
        UserDTO userDTO = userService.findByUsername(username);
        if (userDTO != null) {
            Attempts attemptsById = attemptsRepository.findById(userDTO.getId());
            attemptsById.setCount(0);
            User user = convertToUser(userDTO);
            user.setAttempts(attemptsById);
            attemptsRepository.update(attemptsById);
            convertToDTO(attemptsById);
        }
    }

    private AttemptsDTO convertToDTO(Attempts attemptsById) {
        AttemptsDTO attemptsDTO = new AttemptsDTO();
        attemptsDTO.setQuantity(attemptsById.getCount());
        return attemptsDTO;
    }

    private User convertToUser(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setLogin(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setEMail(userDTO.getUsermail());
        user.setFirstName(user.getFirstName());
        return user;
    }
}
